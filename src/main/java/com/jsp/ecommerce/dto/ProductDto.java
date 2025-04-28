package com.jsp.ecommerce.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto 
{
	@Size(min = 3,max = 50,message ="Name should be between 3 to 50 characters" )
	private String name;
	
	@Size(min=15, max=100,message = "* Description should be  15 to 100 characters")
	private String description;
	
	private MultipartFile image;
	@Min(value = 100,message = "* minimum price must be 100")
	@Max(value = 100000,message = "* maximum price allowed is 1,00,000")
	private double price;
	
	@Min(value = 1, message = "* Atleast 1 stock is required")
	@Max(value = 100, message = "* At max 100 stocks are available")
	private int stock;
	@NotEmpty(message = "* It is required")
	private String category;
}
