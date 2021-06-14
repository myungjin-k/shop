package me.myungjin.shop.serve.rabbitmq.config;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemMessageData {

    private String itemId;

    @Setter
    private String itemName;

    private int count;

}
