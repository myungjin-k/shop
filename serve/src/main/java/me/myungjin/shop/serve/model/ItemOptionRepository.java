package me.myungjin.shop.serve.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption, String> {

    List<ItemOption> findAllByMaster(ItemMaster master);

}
