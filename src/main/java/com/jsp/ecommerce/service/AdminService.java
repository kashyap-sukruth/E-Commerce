package com.jsp.ecommerce.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.UserDto;

import jakarta.servlet.http.HttpSession;

public interface AdminService {
	public String register(UserDto userDto, Model model);

	public String register(UserDto userDto, BindingResult result,HttpSession session);

	public String sumbitOtp(int otp, HttpSession session);

	public String loadHome(HttpSession session);

	public String viewProducts(HttpSession session, Model model);

	public String approveProduct(Long id, HttpSession session);

	public String rejectProduct(Long id, String reason, HttpSession session);

//	public String rejectProduct(Long id, Model model, HttpSession session);

}
