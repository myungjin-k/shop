package me.myungjin.shop.order.rabbitmq.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import me.myungjin.shop.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQOrderReceiver {

    private final OrderService orderService;

    @RabbitListener(id = "item.stock.updated", queues = "item.stock.updated")
    public void handleStockUpdated(MyTask myTask) {
        log.info("Received Stock Updated : {}", myTask);
        orderService.order2(myTask);
    }

    @RabbitListener(id = "item.stock.insufficient", queues = "item.stock.insufficient")
    public void handleStockInsufficient(MyTask myTask) {
        throw new RuntimeException("Received Insufficient Stock :" + myTask.toString() );
    }

    @RabbitListener(id = "item.notfound", queues = "item.notfound")
    public void handleItemNotfound(MyTask myTask) {
        throw new RuntimeException("Received Item Notfound :" + myTask.toString() );
    }

}
