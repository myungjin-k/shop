package me.myungjin.shop.order.web;

import lombok.RequiredArgsConstructor;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import me.myungjin.shop.order.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final RabbitMessagePublisher messagePublisher;

    @GetMapping("/order")
    public String order() {
        /*OrderMaster master = OrderMaster.builder()
                .itemNameAbbr("item 1/item 2")
                .status(OrderStatus.REQUESTED)
                .totalAmount(3000)
                .build();
        List<OrderItem> items = Arrays.asList(
                new OrderItem(1, null, null, null, "TEST_ITEM_ID_1"),
                new OrderItem(1, null, null, null, "TEST_ITEM_ID_2")
        );*/
        //orderService.order(master, items);
        Map<String, Object> map = new HashMap<>();
        map.put("itemNameAbbr", "item 1/item 2");
        map.put("totalAmount", 3000);
        Map<String, Integer> itemMap = new HashMap<>();
        itemMap.put("TEST_ITEM_ID_1", 1);
        itemMap.put("TEST_ITEM_ID_2", 1);
        map.put("items", itemMap);
        messagePublisher.publish("order.request", new MyTask("Publish Order Request", map));
        return "order requested";
    }

}
