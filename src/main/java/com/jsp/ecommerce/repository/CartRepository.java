package com.jsp.ecommerce.repository;

import com.jsp.ecommerce.entity.Cart;
import com.jsp.ecommerce.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart,Long>
{
    Cart findByCustomer(Customer customer);
}
