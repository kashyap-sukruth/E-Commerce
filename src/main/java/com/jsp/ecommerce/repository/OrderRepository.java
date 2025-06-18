package com.jsp.ecommerce.repository;

import com.jsp.ecommerce.entity.Customer;
import com.jsp.ecommerce.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository  extends JpaRepository<Orders,Long> {
    List<Orders> findByCustomer(Customer customer);
    List<Orders> findByPaymentStatus(Enum paymentStatus);
}
