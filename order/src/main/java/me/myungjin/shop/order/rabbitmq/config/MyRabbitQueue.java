package me.myungjin.shop.order.rabbitmq.config;

import lombok.Getter;

@Getter
public enum MyRabbitQueue {

    SAMPLE_TASK("order.sample"),
    REQUEST("order.request"),
    CANCEL("order.cancel"),
    EMPTY("order.empty"),
    STOCK_UPDATED("item.stock.updated"),
    STOCK_INSUFFICIENT("item.stock.insufficient"),
    ITEM_NOTFOUND("item.notfound")
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
