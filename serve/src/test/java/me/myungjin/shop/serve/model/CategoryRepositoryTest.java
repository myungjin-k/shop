package me.myungjin.shop.serve.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class CategoryRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void 카테고리를_등록한다() {
        Category category = Category.builder()
                .cateName("음료")
                .build();
        Category saved = categoryRepository.save(category);

        assertThat(saved.getCateCode()).isEqualTo(category.getCateCode());

        log.info("Category: {}", saved);
    }

    @Test
    void 카테고리를_등록하고_조회한다() {
        Category category = Category.builder()
                .cateName("음료")
                .build();
        category = categoryRepository.save(category);

        Category result = categoryRepository.findById(category.getCateCode())
                .orElse(null);
        assertThat(result).isNotNull();
        assertThat(category.getCateCode()).isEqualTo(result.getCateCode());

        log.info("Category: {}", result);
    }

    @Test
    void 카테고리를_등록하고_이름을_수정한_후_조회한다() {
        Category category = Category.builder()
                .cateName("음료")
                .build();
        category = categoryRepository.save(category);

        log.info("Category: {}", category);

        category.update("음료_수정");
        category = categoryRepository.save(category);

        Category result = categoryRepository.findById(category.getCateCode())
                .orElse(null);
        assertThat(result).isNotNull();
        assertThat(category.getCateCode()).isEqualTo(result.getCateCode());

        log.info("Category Updated: {}", result);
    }


    @Test
    void 카테고리를_등록한_후_삭제한다() {
        Category category = Category.builder()
                .cateName("음료")
                .build();
        category = categoryRepository.save(category);

        log.info("Category: {}", category);

        categoryRepository.delete(category);

        Category result = categoryRepository.findById(category.getCateCode())
                .orElse(null);

        assertThat(result).isNull();
    }
}
