package me.myungjin.shop.serve.rabbitmq.receiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.serve.model.ItemOption;
import me.myungjin.shop.serve.rabbitmq.config.MyTask;
import me.myungjin.shop.serve.rabbitmq.config.OrderItemMessageData;
import me.myungjin.shop.serve.rabbitmq.config.OrderMessageData;
import me.myungjin.shop.serve.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.serve.service.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQOrderReceiver{

    private final ItemService itemService;

    private final RabbitMessagePublisher messagePublisher;

    @Async
    @Transactional
    @RabbitListener(id = "order.request", queues = "order.request")
    public void handleOrderRequest(MyTask myTask) throws InterruptedException {
        Thread.sleep(10000);
        log.info("Order Request Queue Received and Handle: {}", myTask.toString());
        try {
            List<OrderItemMessageData> orderItems = (myTask.getData()).getItems();
            for(OrderItemMessageData orderItem : orderItems) {
                ItemOption item = itemService.updateStock(orderItem.getItemId(), -orderItem.getCount());
                orderItem.setItemName(item.getOptionName());
                log.info("Item Stock Updated : {}", item);
            }
            myTask.getData().setStatus("PROCESSING");
            messagePublisher.publish("order.accept", myTask);
            messagePublisher.publish("order.accept.notice", myTask);
        } catch (IllegalArgumentException e) {
            messagePublisher.publish("item.notfound", myTask);
        } catch (ClassCastException e) {
            log.error("invalid data type", e);
        } catch (RuntimeException e) {
            messagePublisher.publish("item.stock.insufficient", myTask);
        }
    }

    @RabbitListener(id = "order.accept", queues = "order.accept")
    public synchronized void handleOrderAccept(MyTask myTask) throws InterruptedException {
        log.info("Order Accept Queue Received and Handle: {}", myTask.toString());
        log.info("making ....");
        Thread.sleep(20000);
        log.info("Ordered Menu is Ready!");
        myTask.getData().setStatus("SERVED");

        messagePublisher.publish("order.served", myTask);
    }

    @RabbitListener(id = "order.pickup", queues = "order.pickup")
    public synchronized void handleOrderComplete(MyTask myTask) {
        log.info("Order pickup Queue Received and Handle: {}", myTask.toString());
        log.info("Order Completed!!");
    }

}
