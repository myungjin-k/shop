package me.myungjin.shop.order.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "order_master")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class OrderMaster {

    @Id
    @GeneratedValue(generator = "order_id")
    @GenericGenerator(name = "order_id", strategy = "uuid2")
    private String id;

    @Column(name = "item_name_abbr", nullable = false, updatable = false)
    private String itemNameAbbr;

    @Column(name = "total_amount", nullable = false, updatable = false)
    private int totalAmount;

    @Enumerated
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();


    @Builder
    public OrderMaster(String itemNameAbbr, int totalAmount, OrderStatus status, LocalDateTime createAt, LocalDateTime updateAt) {
        this.itemNameAbbr = itemNameAbbr;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = defaultIfNull(updateAt, now());
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
        this.updateAt = now();
    }

    public void addItem(OrderItem item){
        item.setMaster(this);
        this.items.add(item);
    }
}
