package com.jsp.ecommerce.entity;

import java.security.PrivateKey;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.jsp.ecommerce.helper.OrderStatus;
import com.jsp.ecommerce.helper.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private LocalDateTime craetedAt;

	@ManyToOne
	private Customer customer;
}
