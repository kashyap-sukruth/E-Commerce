package com.jsp.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsp.ecommerce.entity.Admin;
import com.jsp.ecommerce.entity.Customer;
import com.jsp.ecommerce.entity.Merchant;
import com.jsp.ecommerce.helper.AES;
import com.jsp.ecommerce.repository.AdminRepository;
import com.jsp.ecommerce.repository.CustomerRepository;
import com.jsp.ecommerce.repository.MerchantRepository;

import jakarta.servlet.http.HttpSession;
@Service
public class GeneralServiceimpl implements GeneralService
{
	@Autowired
	AdminRepository adminRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	MerchantRepository merchantRepository;

	@Override
	public String login(String email, String Password, HttpSession session) 
	{
		
		Admin admin=adminRepository.findByEmail(email);
		Merchant merchant=merchantRepository.findByEmail(email);
		Customer customer=customerRepository.findByEmail(email);
		
		if(admin==null && merchant==null && customer==null)
		{
			session.setAttribute("fail", "Invalid Email");
			return "redirect:/login";
		}
		
		if(admin!=null)
		{
			if (AES.encrypt(admin.getPassword()).equals(Password))
			{
				session.setAttribute("admin", admin);
				session.setAttribute("pass", "login success");
				return "redirect:/admin-home.html";
			}
			else {
				session.setAttribute("fail", "Invalid Password");
				return "redirect:/login";
			}
				
		}
		
		if (merchant != null) {
			if (AES.decrypt(merchant.getPassword()).equals(Password)) 
			{
				session.setAttribute("merchant", merchant);
				session.setAttribute("pass", "Login Success");
				return "redirect:/merchant/home";
			} else {
				session.setAttribute("fail", "Invalid Password");
				return "redirect:/login";
			}
		}
		if (customer != null) {
			if (AES.decrypt(customer.getPassword()).equals(Password)) {
				session.setAttribute("customer", customer);
				session.setAttribute("pass", "Login Success");
				return "redirect:/customer/home";
			} else {
				session.setAttribute("fail", "Invalid Password");
				return "redirect:/login";
			}
		}
		return "redirect:/login";
		
	}

	@Override
	public String logout(HttpSession session) {
		// TODO Auto-generated method stub
		return null;
	}

}
