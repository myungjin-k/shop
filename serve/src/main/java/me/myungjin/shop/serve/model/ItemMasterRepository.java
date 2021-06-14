package me.myungjin.shop.serve.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemMasterRepository extends JpaRepository<ItemMaster, String> {

    List<ItemMaster> findAllByCategory(Category category);

}
