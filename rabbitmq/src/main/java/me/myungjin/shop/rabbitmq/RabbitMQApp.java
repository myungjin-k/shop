package me.myungjin.shop.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RabbitMQApp {
    public static void main(String ar[]) {
        SpringApplication.run(RabbitMQApp.class, ar);
    }

    /*@RabbitListener(id = "order.sample", queues = "order.sample")
    public void handle(MyTask task) {
        log.info("mydata handle:: {}", task.toString());
    }*/
}
