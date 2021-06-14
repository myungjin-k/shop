package me.myungjin.shop.order.rabbitmq.config;


import lombok.*;
import me.myungjin.shop.order.model.OrderMaster;

import java.util.List;
import java.util.stream.Collectors;

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

    public OrderMessageData of(OrderMaster entity) {
        this.orderId = entity.getId();
        this.itemNameAbbr = entity.getItemNameAbbr();
        this.totalAmount = entity.getTotalAmount();
        this.status = entity.getStatus().name();
        this.items = entity.getItems()
                .stream()
                .map(item -> new OrderItemMessageData(item.getItemId(), null, item.getCount()))
                .collect(Collectors.toList());
        return this;
    }

}
