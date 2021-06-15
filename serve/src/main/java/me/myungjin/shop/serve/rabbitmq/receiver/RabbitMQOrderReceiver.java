package me.myungjin.shop.serve.rabbitmq.receiver;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.serve.model.ItemOption;
import me.myungjin.shop.serve.rabbitmq.config.MyTask;
import me.myungjin.shop.serve.rabbitmq.config.OrderItemMessageData;
import me.myungjin.shop.serve.rabbitmq.sender.RabbitMessagePublisher;
import me.myungjin.shop.serve.service.ItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQOrderReceiver{

    private final ItemService itemService;

    private final RabbitMessagePublisher messagePublisher;




    @Transactional
    @RabbitListener(id = "order.request", queues = "order.request")
    public void handleOrderRequest(Channel channel, MyTask myTask, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.info("Order Request Queue Received and Handle: {}", myTask.toString());
        try {
            List<OrderItemMessageData> orderItems = (myTask.getData()).getItems();
            for(OrderItemMessageData orderItem : orderItems) {
                ItemOption item = itemService.updateStock(orderItem.getItemId(), -orderItem.getCount());
                orderItem.setItemName(item.getOptionName());
            }
            log.info("Item Stock Updated");
            myTask.getData().setStatus("PROCESSING");

            messagePublisher.publish("order.accept", myTask);
            messagePublisher.publish("order.accept.notice", myTask);
        } catch (RuntimeException e) {
            messagePublisher.publish("order.reject", myTask);
            log.error("Order Cannot be Accepted. cause :{}", e.getMessage());
        }
    }

    @Async
    public void sendAcceptNotice(long cnt, MyTask myTask) {
        myTask.setMsg(myTask.getMsg() + ": " + cnt);
        messagePublisher.publish("order.accept.notice", myTask);
    }


    @RabbitListener(id = "order.accept", queues = "order.accept", ackMode = "MANUAL")
    public void handleOrderAccept(Channel channel, MyTask myTask, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws InterruptedException, IOException {

        log.info("Order Accept Queue Received and Handle: {}", myTask.toString());
        AMQP.Queue.DeclareOk response = channel.queueDeclarePassive("order.accept");
        //sendAcceptNotice(response.getMessageCount(), myTask);

        log.info("making ....");
        Thread.sleep(60000);
        log.info("Ordered Menu is Ready!");

        myTask.getData().setStatus("SERVED");
        channel.basicAck(tag, false);

        messagePublisher.publish("order.served", myTask);
    }

    @RabbitListener(id = "order.pickup", queues = "order.pickup")
    public void handleOrderPickup(MyTask myTask) {
        log.info("Order pickup Queue Received and Handle: {}", myTask.toString());
        log.info("Order Completed!!");
    }

}
