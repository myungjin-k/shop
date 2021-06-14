package me.myungjin.shop.serve.rabbitmq;


import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@SpringBootTest
public class RabbitMQTest {

    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Test
    void 주문_요청_메세지를_보내고_응답받는다() {
        testRabbitTemplate.convertSendAndReceive("shop", "test", "Do Something!!");
    }

    @Configuration
    @EnableRabbit
    public static class Config {

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Bean
        public ConnectionFactory connectionFactory() {
            ConnectionFactory connectionFactory = mock(ConnectionFactory.class);
            Connection connection = mock(Connection.class);
            connectionFactory.createConnection();
            connection.createChannel(anyBoolean());
            return connectionFactory;
        }

        @Bean
        public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
            SimpleRabbitListenerContainerFactory listenerContainerFactory = new SimpleRabbitListenerContainerFactory();
            listenerContainerFactory.setConnectionFactory(connectionFactory());
            return listenerContainerFactory;
        }

        @RabbitListener(queues = "test")
        public void handleTest(String message) {
            log.info("Test Message Received: {}", message);
        }

        @Bean
        public TestRabbitTemplate rabbitTemplate() {
            TestRabbitTemplate testRabbitTemplate = new TestRabbitTemplate(connectionFactory());
            testRabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            return testRabbitTemplate;
        }

        @Bean
        public SimpleMessageListenerContainer simpleMessageListenerContainer() {
            SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
            container.setQueueNames("test");
           // container.setMessageListener(new MessageListenerAdapter());
            return container;
        }


    }

}
