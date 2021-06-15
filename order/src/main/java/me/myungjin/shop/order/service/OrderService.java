package me.myungjin.shop.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.myungjin.shop.order.model.OrderItem;
import me.myungjin.shop.order.model.OrderMaster;
import me.myungjin.shop.order.model.OrderMasterRepository;
import me.myungjin.shop.order.model.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMasterRepository orderMasterRepository;

    @Transactional(readOnly = true)
    public List<OrderMaster> findAllOrderDesc() {
        return orderMasterRepository.findAllDesc();
    }


    @Transactional
    public OrderMaster order(OrderMaster orderMaster, List<OrderItem> orderItems) {
        for(OrderItem item : orderItems) {
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
    public OrderMaster updateStatus(String orderId, OrderStatus status) {
        return findMasterById(orderId)
                .map(master -> {
                    master.updateStatus(status);
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
