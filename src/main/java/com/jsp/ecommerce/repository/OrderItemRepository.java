package com.jsp.ecommerce.repository;

import com.jsp.ecommerce.entity.Cart;
import com.jsp.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository  extends JpaRepository<OrderItem,Long>
{
    List<OrderItem> findByCart(Cart cart);
    List<OrderItem> findByOrdersId(Long orderId);
}
