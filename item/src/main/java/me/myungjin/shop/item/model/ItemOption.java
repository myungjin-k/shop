package me.myungjin.shop.item.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "item_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"master", "subs"})
public class ItemOption {

    @Id
    @GeneratedValue(generator = "option_id")
    @GenericGenerator(name = "option_id", strategy = "uuid2")
    private String id;

    @Column(name = "option_name", nullable = false, unique = true)
    private String optionName;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemMaster master;

    @JsonManagedReference
    @OneToMany(mappedBy = "option", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemSub> subs = new ArrayList<>();

    @Builder
    public ItemOption(String optionName, LocalDateTime createAt, LocalDateTime updateAt, ItemMaster master) {
        this.optionName = optionName;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = defaultIfNull(updateAt, now());
        this.master = master;
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

    public void update(ItemMaster master, String optionName) {
        this.optionName = optionName;
        this.master = master;
        this.updateAt = now();
    }
}
