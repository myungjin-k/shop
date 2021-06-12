package me.myungjin.shop.item.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemSubRepository extends JpaRepository<ItemSub, String> {

    List<ItemSub> findAllByOption(ItemOption option);

}
