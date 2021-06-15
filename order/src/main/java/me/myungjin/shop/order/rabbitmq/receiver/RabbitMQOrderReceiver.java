package me.myungjin.shop.order.rabbitmq.receiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.order.model.OrderStatus;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import me.myungjin.shop.order.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

    @RabbitListener(id = "order.reject", queues = "order.reject")
    public void handleOrderRejectNotice(MyTask myTask) {
        log.info("Order Reject Queue Received and Handle: {}", myTask);
        orderService.cancel(myTask.getData().getOrderId());
    }

    @RabbitListener(id = "order.accept.notice", queues = "order.accept.notice")
    public void handleAcceptNotice(Channel channel, MyTask myTask) throws IOException {
        AMQP.Queue.DeclareOk response = channel.queueDeclarePassive("order.accept");
        log.info("Order accept notice Queue Received and Handle: {}", myTask.toString());
        orderService.updateStatus(myTask.getData().getOrderId(), OrderStatus.valueOf(myTask.getData().getStatus()));
    }

    @RabbitListener(id = "order.served", queues = "order.served")
    public void handleServedNotice(MyTask myTask) throws InterruptedException {
        log.info("Order Served Queue Received and Handle: {}", myTask.toString());
        orderService.updateStatus(myTask.getData().getOrderId(), OrderStatus.valueOf(myTask.getData().getStatus()));

        log.info("pick up ....");
        Thread.sleep(10000);
        log.info("Got Ordered Menu!");

        messagePublisher.publish("order.pickup", myTask);
    }


}
