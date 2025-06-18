package com.jsp.ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserDto 
{
	@Size(min = 5, max = 55, message = "* Name should be 5~15 charecters")
	private String name;
	@NotEmpty(message = "* Email is Required")
	@Email(message = "* Check Email format")
	private String email;
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "* Password should contain atleast 8 charecter, one uppercase, one lowercase, one number and one special charecter")
	private String password;
	@Pattern(regexp = "^.*(?=.{8,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "* Password should contain atleast 8 charecter, one uppercase, one lowercase, one number and one special charecter")
	private String confirmPassword;
	@AssertTrue(message = "* Check terms and Condition in order to proceed")
	private boolean terms;
	@Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
	private String phoneNo;
}
