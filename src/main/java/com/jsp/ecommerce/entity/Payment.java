package com.jsp.ecommerce.entity;

import java.time.LocalDateTime;

import com.jsp.ecommerce.helper.PaymentStatus;
import org.hibernate.annotations.CreationTimestamp;

import com.jsp.ecommerce.helper.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Payment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String paymentId;
	
	@Column(nullable = false)
	private Double amount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private PaymentStatus status;
		
	@CreationTimestamp
	private LocalDateTime createdAt;
	@OneToOne
	private Orders orders;
	
}
