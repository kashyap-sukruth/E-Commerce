package com.jsp.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.entity.Customer;
import com.jsp.ecommerce.helper.Status;

public interface CustomerRepository extends JpaRepository<Customer, Long> 
{
	boolean existsByEmail(String email);
	
	Customer findByEmail(String email);

	

}
