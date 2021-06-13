package me.myungjin.shop.order.rabbitmq.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;


@NoArgsConstructor
@ToString(exclude = "DEFAULT_MSG")
@Getter
@Setter
public class MyTask {

    private String taskId;

    private String msg;

    private Object data;

    @JsonIgnore
    private String DEFAULT_MSG = "you guys do something";

    public MyTask(String msg, Object data) {
        this.taskId = UUID.randomUUID().toString();
        this.msg = isEmpty(msg) ? DEFAULT_MSG : msg;
        this.data = data;
    }

}
