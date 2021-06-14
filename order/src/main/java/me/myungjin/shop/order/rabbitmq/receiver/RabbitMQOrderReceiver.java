package me.myungjin.shop.order.rabbitmq.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.order.model.OrderStatus;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import me.myungjin.shop.order.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQOrderReceiver {

    private final OrderService orderService;

    private final RabbitMessagePublisher messagePublisher;
/*
    @RabbitListener(id = "order.received", queues = "order.received")
    public void handleStockUpdated(MyTask myTask) {
        log.info("Order Received Queue Received and Handle: {}", myTask);
        orderService.order2(myTask);
    }*/

    @RabbitListener(id = "item.stock.insufficient", queues = "item.stock.insufficient")
    public void handleStockInsufficient(MyTask myTask) {
        throw new RuntimeException("Insufficient Stock Queue Received and Handle:" + myTask.toString() );
    }

    @RabbitListener(id = "item.notfound", queues = "item.notfound")
    public void handleItemNotfound(MyTask myTask) {
        throw new RuntimeException("Item Notfound Queue Received and Handle:" + myTask.toString() );
    }

    @Async
    @RabbitListener(id = "order.served", queues = "order.served")
    public void handleServed(MyTask myTask) throws InterruptedException {
        log.info("Order Served Queue Received and Handle: {}", myTask.toString());
        orderService.updateStatus(myTask.getData().getOrderId(), OrderStatus.valueOf(myTask.getData().getStatus()));

        log.info("pick up ....");
        Thread.sleep(10000);

        messagePublisher.publish("order.pickup", myTask);
    }

    @Async
    @RabbitListener(id = "order.accept.notice", queues = "order.accept.notice")
    public void handleAcceptNotice(MyTask myTask) {
        log.info("Order accept notice Queue Received and Handle: {}", myTask.toString());
        orderService.updateStatus(myTask.getData().getOrderId(), OrderStatus.valueOf(myTask.getData().getStatus()));
    }


}
