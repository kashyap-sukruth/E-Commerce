package com.jsp.ecommerce.controller;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.jsp.ecommerce.dto.ProductDto;
import com.jsp.ecommerce.dto.UserDto;
import com.jsp.ecommerce.service.MerchantService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/merchant")
public class MerchantController {
	@Autowired
	MerchantService merchantService;

	@GetMapping("/register")
	public String loadRegister(UserDto userDto, Model model) {
		return merchantService.register(userDto, model);
	}

	@PostMapping("/register")
	public String register(@Valid UserDto userDto, BindingResult result, HttpSession session) {
		return merchantService.register(userDto, result, session);
	}

	@GetMapping("/otp")
	public String loadOtp() {
		return "merchant-otp.html";
	}

	@PostMapping("/otp")
	public String submitOtp(@ModelAttribute("otp") int otp, HttpSession session) {
		return merchantService.sumbitOtp(otp, session);
	}

	@GetMapping("/home")
	public String loadHome(HttpSession session) {
		return merchantService.loadHome(session);
	}

	@GetMapping("/add-product")
	public String loadAddProduct(ProductDto productDto, HttpSession httpSession, Model model) {
		return merchantService.loadAddProduct(httpSession, model, productDto);
	}

	@PostMapping("/add-product")
	public String addProduct(@Valid ProductDto productDto, BindingResult result, HttpSession session) {
		return merchantService.addProduct(productDto, result, session);
	}

	@GetMapping("/manage-products")
	public String manageProducts(HttpSession session, Model model) {
		return merchantService.manageProducts(session, model);
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable("id") Long id, Model model, HttpSession session) {
		return merchantService.loadExistingProduct(id, model, session);
	}

	@PostMapping("/update-product")
	public String upadteProduct(@Valid ProductDto productDto, BindingResult result, HttpSession session,
			@RequestParam("id") Long id, Model model) {
		return merchantService.updateProduct(productDto, result, session, id, model);
	}

	@GetMapping("/delete/{id}")
	public String deleteProduct(@PathVariable("id") Long id, HttpSession session) {
		return merchantService.deleteById(id, session);
	}

	@GetMapping("/manage-profile")
	public String manageProfile(HttpSession session,Model model)
	{
		return  merchantService.manageProfile(session,model);
	}

	@PostMapping("/manage-profile")
	public String manageProfile(HttpSession session, @ModelAttribute UserDto dto,
								@RequestParam("mobile") String mobile) {
		return merchantService.manageProfile(session, dto, mobile);
	}
}
