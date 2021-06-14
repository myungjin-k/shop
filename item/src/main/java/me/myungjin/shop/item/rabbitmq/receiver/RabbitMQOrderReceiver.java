package me.myungjin.shop.item.rabbitmq.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.item.model.ItemOption;
import me.myungjin.shop.item.rabbitmq.config.MyTask;
import me.myungjin.shop.item.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.item.service.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQOrderReceiver{

    private final ItemService itemService;

    private final RabbitMessagePublisher messagePublisher;

    @Transactional
    @RabbitListener(id = "order.request", queues = "order.request")
    public void handleOrderRequest(MyTask myTask) {
        log.info("Order Request Queue Received and Handle: {}", myTask.toString());
        Map<String, Integer> items = (Map<String, Integer>) (((Map<String, Object>) myTask.getData()).get("items"));
        try {
            for(String key : items.keySet()) {
                int cnt = items.get(key);
                ItemOption item = itemService.updateStock(key, -cnt);
                log.info("Item Stock Updated : {}", item);
            }
            messagePublisher.publish("item.stock.updated", myTask);
        } catch (IllegalArgumentException e) {
            messagePublisher.publish("item.notfound", myTask);
        } catch (RuntimeException e) {
            messagePublisher.publish("item.stock.insufficient", myTask);
        }
    }
}
