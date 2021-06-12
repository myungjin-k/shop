package me.myungjin.shop.order.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderMasterRepository extends JpaRepository<OrderMaster, String> {

    @Query("select om from OrderMaster om order by om.createAt desc")
    List<OrderMaster> findAllDesc();

    @Query("select om from OrderMaster om join fetch om.items")
    Optional<OrderMaster> findByIdWithItems(String id);
}
