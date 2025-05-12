package com.jsp.ecommerce.repository;

import com.jsp.ecommerce.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepsoitory extends JpaRepository<Payment,Long> {

}
