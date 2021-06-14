package me.myungjin.shop.order.rabbitmq.config;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemMessageData {

    private String itemId;

    private String itemName;

    private int count;

}
