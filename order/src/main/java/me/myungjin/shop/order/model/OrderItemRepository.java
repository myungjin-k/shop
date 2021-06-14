package me.myungjin.shop.order.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {

    List<OrderItem> findAllByMaster(OrderMaster master);

}
