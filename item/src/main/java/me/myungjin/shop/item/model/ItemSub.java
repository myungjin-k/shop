package me.myungjin.shop.item.model;

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
@Table(name = "item_sub")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString
public class ItemSub {

    @Id
    @GeneratedValue(generator = "sub_id")
    @GenericGenerator(name = "sub_id", strategy = "uuid2")
    private String id;

    @Column(name = "sub_name", nullable = false, unique = true)
    private String subName;

    @Column(name = "price")
    private int price;

    @Column(name = "stock")
    private int stock;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private ItemOption option;

    @Builder
    public ItemSub(String subName, int price, int stock, LocalDateTime createAt, LocalDateTime updateAt, ItemOption option) {
        this.subName = subName;
        this.price = price;
        this.stock = stock;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = defaultIfNull(updateAt, now());
        this.option = option;
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

    public void updateInfo(String subName, int price, ItemOption option) {
        this.subName = subName;
        this.price = price;
        this.option = option;
        this.updateAt = now();
    }

    public void updateStock(int amount){
        this.stock += amount;
        this.updateAt = now();
    }
}
