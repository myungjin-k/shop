package me.myungjin.shop.item.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableRabbit
@Configuration
public class RabbitMQConfig {
    @Resource
    private RabbitProperties rabbitProperties;

    public static final String RABBIT_EXECHAGNGE_NAME = "shop";

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("myTask", MyTask.class);
        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }


    @Bean
    public MessageConverter rabbitMessageConverter() {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper());
        return jsonConverter;
    }

    @Bean
    public ConnectionFactory rabbitConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        log.info("userName:: {}", rabbitProperties.getUsername());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);

        return connectionFactory;
    }

    /**
     * Admin 설정 관련 부분
     * RabbitMQ에 admin 권한이 있는 계정으로 접속한 후에
     * exchange와 queue를 등록하고 매핑해준다.
     * @param rabbitConnectionFactory
     * @return
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory rabbitConnectionFactory) {
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);

        // exchange 등록
        rabbitExchange(rabbitAdmin);
        ///queue 자동 등록
        for (MyRabbitQueue myRabbitQueue : MyRabbitQueue.values()) {
            rabbitAdmin.declareQueue(new Queue(myRabbitQueue.getQueueName(), true));
            rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(myRabbitQueue.getQueueName(), true))
                    .to(rabbitExchange(rabbitAdmin)).with(myRabbitQueue.getQueueName()));
        }

        rabbitAdmin.afterPropertiesSet();
        return rabbitAdmin;
    }

    @Bean
    TopicExchange rabbitExchange(RabbitAdmin rabbitAdmin) {
        TopicExchange topicExchange = new TopicExchange(RABBIT_EXECHAGNGE_NAME);
        topicExchange.setAdminsThatShouldDeclare(rabbitAdmin);
        return topicExchange;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory rabbitConnectionFactory,
                                         MessageConverter rabbitMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(rabbitConnectionFactory);
        rabbitTemplate.setMessageConverter(rabbitMessageConverter);
        rabbitTemplate.setExchange(RABBIT_EXECHAGNGE_NAME);

        rabbitTemplate.setConfirmCallback(((correlationData, ack, cause) -> {
            if(ack) {
                log.info("success");
            }else{
                log.error("error {}", cause);
            }
        }));

        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(rabbitMessageConverter());
        factory.setChannelTransacted(true);
        /*factory.setAdviceChain(RetryInterceptorBuilder
                .stateless()
                .maxAttempts(MAX_TRY_COUNT)
                .recoverer(new RabbitMqExceptionHandler())
                .backOffOptions(INITIAL_INTERVAL, MULTIPLIER, MAX_INTERVAL)
                .build());*/
        return factory;
    }
}

