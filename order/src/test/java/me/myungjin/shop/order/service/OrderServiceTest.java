package me.myungjin.shop.order.service;

import me.myungjin.shop.order.model.OrderItem;
import me.myungjin.shop.order.model.OrderMaster;
import me.myungjin.shop.order.model.OrderStatus;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    private OrderMaster master;

    private List<OrderItem> items;

    @BeforeAll
    void setup() {
        master = OrderMaster.builder()
                .itemNameAbbr("item 1/item 2")
                .status(OrderStatus.REQUESTED)
                .totalAmount(3000)
                .build();
        items = Arrays.asList(
                new OrderItem(1, null, null, null, "TEST_ITEM_ID_1"),
                new OrderItem(1, null, null, null, "TEST_ITEM_ID_2")
        );
    }

    @Test
    @Order(1)
    void 주문을_저장한다() {
        OrderMaster saved = orderService.order(master, items);

        assertThat(saved).isNotNull();
        log.info("Saved Order Master: {}", saved);
        log.info("Saved Order Items: {}", saved.getItems());
        master = saved;
        items = saved.getItems();
    }

    @Test
    @Order(2)
    void 주문을_취소한다() {
        OrderMaster canceled = orderService.cancel(master.getId());

        assertThat(canceled).isNotNull();
        log.info("Canceled Order Master: {}", canceled);
    }
}
