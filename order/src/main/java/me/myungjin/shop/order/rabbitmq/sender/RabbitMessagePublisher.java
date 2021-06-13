package me.myungjin.shop.order.rabbitmq.sender;

import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMessagePublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void publish(String routingKey, MyTask myTask) {
        try {
            rabbitTemplate.convertAndSend("shop", routingKey, myTask);
        } catch (Exception e) {
            log.error("error", e);
        }
    }


}
