package me.myungjin.shop.order.web;

import lombok.RequiredArgsConstructor;
import me.myungjin.shop.order.model.OrderItem;
import me.myungjin.shop.order.model.OrderMaster;
import me.myungjin.shop.order.model.OrderStatus;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import me.myungjin.shop.order.rabbitmq.config.OrderItemMessageData;
import me.myungjin.shop.order.rabbitmq.config.OrderMessageData;
import me.myungjin.shop.order.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.order.service.OrderService;
import org.hibernate.criterion.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final RabbitMessagePublisher messagePublisher;

    private final OrderService orderService;

    @Transactional
    @GetMapping("/order")
    public ResponseEntity order() {

        OrderMaster master = OrderMaster.builder()
                .itemNameAbbr("onion, blueberry")
                .status(OrderStatus.REQUESTED)
                .totalAmount(3000)
                .build();

        List<OrderItem> items = Arrays.asList(
                new OrderItem(1, null, null, null, "test_option_id_1"),
                new OrderItem(1, null, null, null, "test_option_id_2")
        );
        OrderMaster result = orderService.order(master, items);
        try {
            messagePublisher.publish("order.request", new MyTask("Publish Order Request", new OrderMessageData().of(result)));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<>("order rejected", HttpStatus.ACCEPTED);
        }
    }

}
