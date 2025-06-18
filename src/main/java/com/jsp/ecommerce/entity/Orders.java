package com.jsp.ecommerce.entity;

import java.security.PrivateKey;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import com.jsp.ecommerce.helper.OrderStatus;
import com.jsp.ecommerce.helper.PaymentStatus;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Orders {
	@Id
	@GeneratedValue(generator = "orderId")
	@SequenceGenerator(initialValue = 101001,allocationSize = 1,name = "orderId")
	private Long id;
	
	@Column(nullable = false)
	private Double totalAmount;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@CreationTimestamp
	private LocalDateTime createdAt;

	@ManyToOne
	private Customer customer;


	@Column(nullable = false)
	private Long mobile;
	@Column(nullable = false)
	private String address;
}
