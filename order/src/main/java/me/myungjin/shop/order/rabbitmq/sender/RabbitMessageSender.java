package me.myungjin.shop.order.rabbitmq.sender;

import lombok.RequiredArgsConstructor;
import me.myungjin.shop.order.rabbitmq.config.MyRabbitQueue;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
public abstract class RabbitMessageSender {
    private final RabbitTemplate rabbitTemplate;

    public void send(MyRabbitQueue queueName, Object data) {
        Assert.isNull(data, "message can't be null");
        Message msg = rabbitTemplate.getMessageConverter().toMessage(data, null);
        rabbitTemplate.send(String.valueOf(queueName), msg);
    }
}
