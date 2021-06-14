package me.myungjin.shop.serve.model;

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
@Table(name = "item_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = "master")
public class ItemOption {

    @Id
    @GeneratedValue(generator = "option_id")
    @GenericGenerator(name = "option_id", strategy = "uuid2")
    private String id;

    @Column(name = "option_name", nullable = false, unique = true)
    private String optionName;

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
    @JoinColumn(name = "item_id", nullable = false)
    private ItemMaster master;

    @Builder
    public ItemOption(String optionName, int price, int stock,
                      LocalDateTime createAt, LocalDateTime updateAt, ItemMaster master) {
        this.optionName = optionName;
        this.price = price;
        this.stock = stock;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = updateAt;
        this.master = master;
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

    public void updateDefaultInfo(ItemMaster master, ItemOption option) {
        this.optionName = option.optionName;
        this.price = option.price;
        this.stock = option.stock;
        this.master = master;
        this.updateAt = now();
    }

    public void updateStock(int amount) {
        this.stock += amount;
        this.updateAt = now();
    }


}
