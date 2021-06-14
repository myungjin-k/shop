package me.myungjin.shop.item.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@Table(name = "category")
@EqualsAndHashCode(of = "cateCode")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "items")
public class Category {

    @Id
    @GeneratedValue(generator = "cateId")
    @GenericGenerator(name = "cateId", strategy = "uuid2")
    private String cateCode;

    @Column(name = "cate_name", nullable = false, unique = true)
    private String cateName;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @JsonManagedReference
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ItemMaster> items = new ArrayList<>();

    @Builder
    public Category(String cateName, LocalDateTime createAt, LocalDateTime updateAt) {
        checkNotNull(cateName,  "cateName must be provided.");
        this.cateName = cateName;
        this.createAt = defaultIfNull(createAt, now());
        this.updateAt = updateAt;
    }

    public Optional<LocalDateTime> getUpdateAt() {
        return ofNullable(updateAt);
    }

    public void update(String cateName){
        this.cateName = cateName;
        this.updateAt = now();
    }

}
