package me.myungjin.shop.serve.rabbitmq.config;

import lombok.Getter;

@Getter
public enum MyRabbitQueue {

    SAMPLE_TASK("order.sample"),
    REQUEST("order.request"),
    ACCEPT("order.accept"),
    ACCEPT_NOTICE("order.accept.notice"),
    CANCEL("order.cancel"),
    EMPTY("order.empty"),
    STOCK_INSUFFICIENT("item.stock.insufficient"),
    ITEM_NOTFOUND("item.notfound"),
    SERVED("order.served"),
    COMPLETE("order.complete"),
    PICKUP("order.pickup")
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
