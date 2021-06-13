package me.myungjin.shop.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.order.model.OrderItem;
import me.myungjin.shop.order.model.OrderMaster;
import me.myungjin.shop.order.model.OrderMasterRepository;
import me.myungjin.shop.order.model.OrderStatus;
import me.myungjin.shop.order.rabbitmq.config.MyTask;
import me.myungjin.shop.order.rabbitmq.sender.RabbitMessagePublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMasterRepository orderMasterRepository;

    private final RabbitMessagePublisher messagePublisher;

    @Transactional(readOnly = true)
    public List<OrderMaster> findAllOrderDesc() {
        return orderMasterRepository.findAllDesc();
    }


    @Transactional
    public OrderMaster order(OrderMaster orderMaster, List<OrderItem> orderItems) {
        //messagePublisher.publish(MyRabbitQueue.REQUEST.getQueueName(), new MyTask("send new order", orderItems));
        for(OrderItem item : orderItems) {
            orderMaster.addItem(item);
        }
        OrderMaster saved = save(orderMaster);
        return saved;
    }

    @Transactional
    public OrderMaster order2(MyTask myTask) {
        log.info("order save logic...");
        Map<String, Object> data = (Map<String, Object>) myTask.getData();
        OrderMaster orderMaster = OrderMaster.builder()
                .itemNameAbbr((String) data.get("itemNameAbbr"))
                .status(OrderStatus.REQUESTED)
                .totalAmount((int) data.get("totalAmount"))
                .build();

        Map<String, Integer> items = (Map<String, Integer>) (((Map<String, Object>) myTask.getData()).get("items"));
        for(String itemId : items.keySet()) {
            int count = items.get(itemId);
            OrderItem item = OrderItem.builder()
                        .count(count)
                        .itemId(itemId)
                        .build();
            orderMaster.addItem(item);
        }
        OrderMaster saved = save(orderMaster);
        return saved;
    }

    @Transactional
    public OrderMaster cancel(String orderId) {
        return findMasterByIdWithItems(orderId)
                .map(master -> {
                    master.cancel();
                    return save(master);
                }).orElseThrow(() -> new IllegalArgumentException("invalid order id=" + orderId));
    }

    @Transactional
    public OrderMaster findById(String orderId) {
        return findMasterById(orderId).orElseThrow(() -> new IllegalArgumentException("invalid order id=" + orderId));
    }

    private OrderMaster save(OrderMaster entity) {
        return orderMasterRepository.save(entity);
    }

    private Optional<OrderMaster> findMasterById(String id){
        return orderMasterRepository.findById(id);
    }

    private Optional<OrderMaster> findMasterByIdWithItems(String id){
        return orderMasterRepository.findByIdWithItems(id);
    }

}
