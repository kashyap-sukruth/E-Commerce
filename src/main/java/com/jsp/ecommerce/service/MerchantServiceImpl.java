package com.jsp.ecommerce.service;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.ProductDto;
import com.jsp.ecommerce.dto.UserDto;
import com.jsp.ecommerce.entity.Customer;
import com.jsp.ecommerce.entity.Merchant;
import com.jsp.ecommerce.entity.Product;
import com.jsp.ecommerce.helper.AES;
import com.jsp.ecommerce.helper.CloudinaryHelper;
import com.jsp.ecommerce.helper.EmailSender;
import com.jsp.ecommerce.helper.Status;
import com.jsp.ecommerce.repository.AdminRepository;
import com.jsp.ecommerce.repository.CustomerRepository;
import com.jsp.ecommerce.repository.MerchantRepository;
import com.jsp.ecommerce.repository.ProductRepository;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Service
public class MerchantServiceImpl implements MerchantService {
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	EmailSender emailSender;

	@Autowired
	Merchant merchant;

	@Autowired
	CloudinaryHelper cloudinaryHelper;

	public String register(UserDto userDto, Model model) {
		model.addAttribute("userdto", userDto);
		return "merchant-register.html";
	}

	@Override
	public String register(UserDto userDto, BindingResult result, HttpSession session) {
//		if (!userDto.getEmail().toLowerCase().endsWith("@gmail.com")) 
//		    result.rejectValue("email", "error.email", "* Only Gmail addresses are allowed");
//		

		if (!userDto.getPassword().equals(userDto.getConfirmPassword()))
			result.rejectValue("confirmPassword", "error.confirmPassword",
					"* Password and Confirm password not matching");

		if (adminRepository.existsByEmail(userDto.getEmail()) || customerRepository.existsByEmail(userDto.getEmail())
				|| merchantRepository.existsByEmail(userDto.getEmail()))
			result.rejectValue("email", "error.email", "* Email Already Exists");

		if (result.hasErrors()) {
			return "merchant-register.html";
		}

		int otp = new Random().nextInt(100000, 1000000);
		System.out.println("otp is" + otp);
		emailSender.sendEmail(userDto, otp);

		session.setAttribute("otp", otp);
		session.setAttribute("userDto", userDto);
		session.setAttribute("pass", "Otp Sent Success");
		return "redirect:/merchant/otp";
	}

	@Override
	public String sumbitOtp(int otp, HttpSession session) {
		int generatedOtp = (int) session.getAttribute("otp");

		if (generatedOtp == otp) {
			UserDto dto = (UserDto) session.getAttribute("userDto");
			Merchant merchant1 = new Merchant();
			merchant1.setName(dto.getName());
			merchant1.setEmail(dto.getEmail());
			merchant1.setPassword(AES.encrypt(dto.getPassword()));
			merchantRepository.save(merchant1);
			session.setAttribute("pass", "Account created success");
			session.removeAttribute("otp");
			session.removeAttribute("userDto");
			return "redirect:/";
		} else {
			session.setAttribute("fail", "Otp Missmatch");
			return "redirect:/merchant/otp";
		}

	}

	@Override
	public String loadHome(HttpSession session) {
		Merchant merchant = (Merchant) session.getAttribute("merchant");
		if (merchant != null)
			return "merchant-home.html";
		else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}

	@Override
	public String loadAddProduct(HttpSession httpSession, Model model, ProductDto productDto) {
		Merchant merchant = (Merchant) httpSession.getAttribute("merchant");
		if (merchant != null) {
			model.addAttribute("productDto", productDto);
			return "add-product.html";
		} else {
			httpSession.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}

	}

	@Override
	public String addProduct(HttpSession session, @Valid ProductDto productDto, BindingResult result) {
		Merchant merchant = (Merchant) session.getAttribute("merchant");

		if (merchant != null) 
		{
			if (productDto.getImage().isEmpty()) 
				result.rejectValue("image", "error.image", "* select one image");
			
			if(result.hasErrors())
				return "add-product.html";		
				
		else {
			
			Product product=new Product();
			product.setName(productDto.getName());
			product.setDescription(productDto.getDescription());
			product.setPrice(productDto.getPrice());
			product.setStock(productDto.getStock());
			product.setMerchant(merchant);
			product.setStatus(Status.PENDING);
			product.setCategory(productDto.getCategory());		
			product.setImageUrl(cloudinaryHelper.saveImage(productDto.getImage()));
			productRepository.save(product);
			
			session.setAttribute("pass", "product added success");
			return "redirect:/merchant/home";

	}
}
			 else {
					session.setAttribute("fail", "Invalid Session, First Login to Access");
					return "redirect:/login";
	
}
}

	@Override
	public String manageProducts(HttpSession session, Model model) {
		Merchant merchant = (Merchant) session.getAttribute("merchant");
		if (merchant != null) {
		List<Product> products=	productRepository.findByMerchant_id(merchant.getId());
		if (products.isEmpty()) {
			session.setAttribute("fail", "No Products Added Yet");
			return "redirect:/merchant/home";
	}
		 else {
			model.addAttribute("products", products);
			return "manage-products.html";
		}
}
		else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}
}
	
	