package me.myungjin.shop.order.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "order_item")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "master")
public class OrderItem {

    @Id
    @GeneratedValue(generator = "order_item_id")
    @GenericGenerator(name = "order_item_id", strategy = "uuid2")
    private String id;

    @Column(name = "count", nullable = false, updatable = false)
    private int count;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderMaster master;

    @Column(name = "item_id", nullable = false, updatable = false)
    private String itemId;

    @Builder
    public OrderItem(int count, LocalDateTime createAt, LocalDateTime updateAt, OrderMaster master, String itemId) {
        this.count = count;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = defaultIfNull(updateAt, now());
        this.master = master;
        this.itemId = itemId;
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

}
