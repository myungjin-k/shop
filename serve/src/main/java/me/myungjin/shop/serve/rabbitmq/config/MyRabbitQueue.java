package me.myungjin.shop.serve.rabbitmq.config;

import lombok.Getter;

@Getter
public enum MyRabbitQueue {

    SAMPLE_TASK("order.sample"),
    REQUEST("order.request"), // 주문 요청
    ACCEPT("order.accept"), // 주문 승인
    ACCEPT_NOTICE("order.accept.notice"),
    REJECT("order.reject"),  // 주문 거절
    SERVED("order.served"), // 제조 완료
    PICKUP("order.pickup"), // 픽업 완료
    EMPTY("order.empty"),
    COMPLETE("order.complete")
    ;

    private String queueName;

    MyRabbitQueue(String queueName) {
        this.queueName = queueName;
    }

    public static MyRabbitQueue of(String name) {
        for(MyRabbitQueue queue : MyRabbitQueue.values()) {
            if(name.equalsIgnoreCase(queue.queueName)) {
                return queue;
            }
        }
        return EMPTY;
    }

}
