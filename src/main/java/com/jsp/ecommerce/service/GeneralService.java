package com.jsp.ecommerce.service;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.UserDto;

import jakarta.servlet.http.HttpSession;

public interface GeneralService 
{
	String login(String email,String Password,HttpSession session);

	String logout(HttpSession session);

	String loadForgotPassword(Model model);

	String resetPassword(UserDto userDto,BindingResult  result,String email,Model model);

}
