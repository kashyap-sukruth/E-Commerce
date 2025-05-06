package com.jsp.ecommerce.controller;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsp.ecommerce.dto.UserDto;
import com.jsp.ecommerce.service.GeneralService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class GeneralController 
{
	
	@Autowired
	GeneralService generalService;
	@GetMapping("/login")
	public String loadLogin()
	{
		return "login.html";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam("email") String email,@RequestParam("password") String password,HttpSession session)
	{
		return generalService.login(email, password, session);
	}
	

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		return generalService.logout(session);
	}
	
	@GetMapping("/forgotPassword")
	public String loadForgotPassword(Model model)
	{
	return	generalService.loadForgotPassword(model);

	}
	@PostMapping("/reset-password")
	public String resetPassword(@Valid UserDto userDto,BindingResult  result,@RequestParam("email")String email,Model model)
	{
		return generalService.resetPassword(userDto,result,email,model);
	}
}
