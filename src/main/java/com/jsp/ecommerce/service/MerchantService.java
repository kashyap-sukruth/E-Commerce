package com.jsp.ecommerce.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.ProductDto;
import com.jsp.ecommerce.dto.UserDto;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

public interface MerchantService {

	public String register(UserDto userDto, Model model);

	public String register(UserDto userDto, BindingResult result,HttpSession session);

	public String sumbitOtp(int otp, HttpSession session);

	public String loadHome(HttpSession session);

	public String loadAddProduct(HttpSession httpSession, Model model, ProductDto productDto);

	public String addProduct(@Valid ProductDto productDto,BindingResult result,HttpSession session);

	public String manageProducts(HttpSession session, Model model);

	public String loadExistingProduct(Long id, Model model, HttpSession session);

	public String updateProduct( ProductDto productDto, BindingResult result, HttpSession session, Long id,
			Model model);

	public String deleteById(Long id, HttpSession session);
}
