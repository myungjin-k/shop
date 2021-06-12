package me.myungjin.shop.rabbitmq.sender;

import lombok.RequiredArgsConstructor;
import me.myungjin.shop.rabbitmq.config.MyRabbitQueue;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.google.common.base.Preconditions.checkArgument;

@Component
@RequiredArgsConstructor
public class RabbitMessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(MyRabbitQueue queueName, Object data) {

        checkArgument(data != null, "message can't be null");
        Message msg = rabbitTemplate.getMessageConverter().toMessage(data, null);
        rabbitTemplate.send(String.valueOf(queueName), msg);
    }
}
