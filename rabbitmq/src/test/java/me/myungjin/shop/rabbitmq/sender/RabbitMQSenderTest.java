package me.myungjin.shop.rabbitmq.sender;

import me.myungjin.shop.rabbitmq.MyTask;
import me.myungjin.shop.rabbitmq.config.MyRabbitQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RabbitMQSenderTest {

    @Autowired
    //private RabbitMessagePublisher publisher;
    private RabbitMessageSender sender;

    @Test
    public void sendMsg() {
        String msg = "you guys do something!!!";
        //publisher.publish(MyRabbitQueue.SAMPLE_TASK.getQueueName(), new MyTask(msg));
        sender.send(MyRabbitQueue.SAMPLE_TASK, new MyTask(msg));
    }
}
