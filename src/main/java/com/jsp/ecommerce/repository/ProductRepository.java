package com.jsp.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.ecommerce.entity.Product;
import com.jsp.ecommerce.helper.Status;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByMerchant_id(Long id);

	List<Product> findByStatus(Status approved, Sort name);

	List<Product> findByStatusAndCategory(Status status, String category, Sort name);

	List<Product> findByStatusAndNameLike(Status status, String s, Sort name);

    List<Product> findByStatusAndCategoryAndStockGreaterThan(Status status, String category,Integer stock, Sort name);
}
