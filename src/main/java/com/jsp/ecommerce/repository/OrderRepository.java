package com.jsp.ecommerce.repository;

import com.jsp.ecommerce.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Orders,Long> {
}
