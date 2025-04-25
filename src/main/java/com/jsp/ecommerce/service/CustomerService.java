package com.jsp.ecommerce.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.UserDto;

import jakarta.servlet.http.HttpSession;

public interface CustomerService
{

	public String register(UserDto userDto, Model model);

    public String register(UserDto userDto, BindingResult result,HttpSession session);

	public String sumbitOtp(int otp, HttpSession session);

	public String loadHome(HttpSession session);



}
