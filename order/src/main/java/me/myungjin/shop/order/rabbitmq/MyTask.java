package me.myungjin.shop.order.rabbitmq;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static org.apache.commons.lang3.StringUtils.isEmpty;


@NoArgsConstructor
@ToString(exclude = "DEFAULT_MSG")
@Getter
@Setter
public class MyTask {

    private String taskId;

    private String msg;

    @JsonIgnore
    private String DEFAULT_MSG = "you guys do something";

    public MyTask(String msg) {
        this.taskId = UUID.randomUUID().toString();
        this.msg = isEmpty(msg) ? DEFAULT_MSG : msg;
    }

}
