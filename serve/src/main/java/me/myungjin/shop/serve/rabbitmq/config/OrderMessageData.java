package me.myungjin.shop.serve.rabbitmq.config;


import lombok.*;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessageData {

    private String orderId;

    private String itemNameAbbr;

    private int totalAmount;

    @Setter
    private String status;

    private List<OrderItemMessageData> items;

}
