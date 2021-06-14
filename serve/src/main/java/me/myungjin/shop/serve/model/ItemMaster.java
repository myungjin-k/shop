package me.myungjin.shop.serve.model;

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
@Table(name = "item_master")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Getter
@ToString(exclude = {"category", "options"})
public class ItemMaster {

    @Id
    @GeneratedValue(generator = "item_id")
    @GenericGenerator(name = "item_id", strategy = "uuid2")
    private String id;

    @Column(name = "item_name", nullable = false, unique = true)
    private String itemName;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Setter
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "cate_code", nullable = false)
    private Category category;

    @JsonManagedReference
    @OneToMany(mappedBy = "master", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemOption> options = new ArrayList<>();

    @Builder
    public ItemMaster(String itemName, LocalDateTime createAt, LocalDateTime updateAt, Category category) {
        this.itemName = itemName;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = updateAt;
        this.category = category;
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

    public void update(Category category, String itemName) {
        this.itemName = itemName;
        this.category = category;
        this.updateAt = now();
    }
}
