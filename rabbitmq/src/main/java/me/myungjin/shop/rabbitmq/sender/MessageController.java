package me.myungjin.shop.rabbitmq.sender;

import me.myungjin.shop.rabbitmq.MyTask;
import me.myungjin.shop.rabbitmq.config.RabbitMQConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    RabbitMessagePublisher rabbitMessagePublisher;

    @GetMapping("/send")
    public String sendData(@RequestParam(defaultValue = "") String msg) {
        rabbitMessagePublisher.publish(RabbitMQConfig.RABBIT_EXECHAGNGE_NAME, new MyTask(msg));

        return "send Data";
    }
}
