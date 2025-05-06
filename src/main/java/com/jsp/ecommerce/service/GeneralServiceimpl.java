package com.jsp.ecommerce.service;

import java.nio.channels.Pipe.SourceChannel;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.UserDto;
import com.jsp.ecommerce.entity.Admin;
import com.jsp.ecommerce.entity.Customer;
import com.jsp.ecommerce.entity.Merchant;
import com.jsp.ecommerce.helper.AES;
import com.jsp.ecommerce.repository.AdminRepository;
import com.jsp.ecommerce.repository.CustomerRepository;
import com.jsp.ecommerce.repository.MerchantRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class GeneralServiceimpl implements GeneralService {
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Override
	public String login(String email, String Password, HttpSession session) {

		Admin admin = adminRepository.findByEmail(email);
		Merchant merchant = merchantRepository.findByEmail(email);
		Customer customer = customerRepository.findByEmail(email);

		if (admin == null && merchant == null && customer == null) {
			session.setAttribute("fail", "Invalid Email");
			return "redirect:/login";
		}

		if (admin != null) {
			if (AES.decrypt(admin.getPassword()).equals(Password)) {
				session.setAttribute("admin", admin);
				session.setAttribute("pass", "login success");
				return "redirect:/admin/home";
			} else {
				session.setAttribute("fail", "Invalid Password");
				return "redirect:/login";
			}

		}

		if (merchant != null) {
			if (AES.decrypt(merchant.getPassword()).equals(Password)) {
				System.out.println(AES.decrypt(merchant.getPassword()));
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
		session.removeAttribute("admin");
		session.removeAttribute("merchant");
		session.removeAttribute("customer");
		session.setAttribute("pass", "Logout Success");
		return "redirect:/";
	}

	@Override
	public String loadForgotPassword(Model model) {
		model.addAttribute("userDto", new UserDto());
		return "forgot-password.html";
	}

	@Override
	public String resetPassword(UserDto userDto, BindingResult result, String email, Model model) {
		{
			Admin admin = adminRepository.findByEmail(email);
			Merchant merchant = merchantRepository.findByEmail(email);
			Customer customer = customerRepository.findByEmail(email);
			System.out.println(merchant);
			System.out.println(admin);
			System.out.println(customer);

			if (admin == null && merchant == null && customer == null) {
				model.addAttribute("fail", "Invalid Email,please enter a vaild email id");
				return "forgot-password.html";
			}

			if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
				System.out.println("Password mismatch detected");
				model.addAttribute("fail", "password and confirm password not matching");
				return "forgot-password.html";
			}
//			if (result.hasErrors()) {
//				return "forgot-password.html";
//			}

			if (admin != null) {
				System.out.println(AES.decrypt(admin.getPassword()));
				admin.setPassword(AES.encrypt(userDto.getConfirmPassword()));
//				admin.setPassword(userDto.getConfirmPassword());
				adminRepository.save(admin);
				model.addAttribute("pass", "password update success");
				return "login.html";

			}

			if (merchant != null) {

				System.out.println(AES.decrypt(merchant.getPassword()));
				merchant.setPassword(AES.encrypt(userDto.getConfirmPassword()));
//				merchant.setPassword(userDto.getConfirmPassword());
				merchantRepository.save(merchant);
				model.addAttribute("pass", "password update success");
				return "login.html";

			}
			if (customer != null) {

				customer.setPassword(AES.encrypt(userDto.getConfirmPassword()));
//				customer.setPassword(userDto.getConfirmPassword());
				customerRepository.save(customer);
				model.addAttribute("success", "Your password has been successfully reset.");
				return "login.html";

			}

			return "login.html";
		}

	}
}
