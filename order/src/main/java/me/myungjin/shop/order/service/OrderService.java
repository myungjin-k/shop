package me.myungjin.shop.order.service;

import lombok.RequiredArgsConstructor;
import me.myungjin.shop.order.model.OrderItem;
import me.myungjin.shop.order.model.OrderMaster;
import me.myungjin.shop.order.model.OrderMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        return save(orderMaster);
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
