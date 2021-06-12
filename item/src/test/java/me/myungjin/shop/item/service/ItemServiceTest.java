package me.myungjin.shop.item.service;

import me.myungjin.shop.item.model.Category;
import me.myungjin.shop.item.model.ItemMaster;
import me.myungjin.shop.item.model.ItemOption;
import me.myungjin.shop.item.model.ItemSub;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemService itemService;

    private Category newCategory;

    private ItemMaster newItemMaster;

    private ItemOption newItemOption;

    private ItemSub newItemSub;

    @BeforeAll
    void setup() {
        newCategory = Category.builder()
                .cateName("TEST_CATEGORY")
                .build();
        newItemMaster = ItemMaster.builder()
                .itemName("TEST_ITEM")
                .build();
        newItemOption = ItemOption.builder()
                .optionName("TEST_OPTION")
                .build();
        newItemSub = ItemSub.builder()
                .subName("TEST_SUB")
                .price(2000)
                .stock(50)
                .build();
    }

    @Test
    @Order(1)
    void 카테고리를_등록한다() {
        Category saved = itemService.saveCategory(newCategory);

        assertThat(saved).isNotNull();
        log.info("Category: {}", saved);
        newCategory = saved;
    }

    @Test
    @Order(2)
    void 카테고리를_조회한다() {
        Category result = itemService.findCategory(newCategory.getCateCode());

        assertThat(result).isNotNull();
        assertThat(newCategory.getCateCode()).isEqualTo(result.getCateCode());
        log.info("Category: {}", result);
    }

    @Test
    @Order(3)
    void 카테고리_이름을_수정한다() {
        String name = "수정";

        Category updated = itemService.updateCategoryName(newCategory.getCateCode(), name);

        assertThat(updated).isNotNull();
        assertThat(name).isEqualTo(updated.getCateName());
        log.info("Category Updated: {}", updated);
    }

    @Test
    @Order(4)
    void 카테고리별_상품_리스트를_조회한다() {
        List<ItemMaster> masterList = Arrays.asList(
                new ItemMaster("상품1", null, null, null),
                new ItemMaster("상품2", null, null, null)
        );
        masterList.forEach(m -> itemService.saveMaster(newCategory.getCateCode(), m));

        List<ItemMaster> results = itemService.getAllByCateCode(newCategory.getCateCode());

        assertThat(results.size()).isEqualTo(2);
        results.forEach(m -> log.info("ItemMaster: {}", m));
    }

    @Test
    @Order(5)
    void 상품마스터를_등록한다() {

        ItemMaster saved = itemService.saveMaster(newCategory.getCateCode(), newItemMaster);

        assertThat(saved).isNotNull();
        log.info("ItemMaster: {}", saved);
        newItemMaster = saved;
    }

    @Test
    @Order(6)
    void 상품마스터를_조회한다() {

        ItemMaster result = itemService.findMaster(newItemMaster.getId());

        assertThat(result).isNotNull();
        assertThat(newItemMaster.getId()).isEqualTo(result.getId());
        log.info("ItemMaster: {}", result);
    }

    @Test
    @Order(7)
    void 상품마스터를_수정한다() {
        String name = "수정";

        ItemMaster updated = itemService.updateItemMaster(newItemMaster.getId(), newCategory.getCateCode(), name);

        assertThat(updated).isNotNull();
        assertThat(name).isEqualTo(updated.getItemName());
        //assertThat(newCategory.getCateCode()).isEqualTo(updated.getCategory().getCateCode());
        log.info("ItemMaster Updated: {}", updated);
    }

    @Test
    @Order(8)
    void 마스터별_옵션_리스트를_조회한다() {
        List<ItemOption> optionList = Arrays.asList(
                new ItemOption("옵션1", null, null, null),
                new ItemOption("옵션2", null, null, null)
        );
        optionList.forEach(m -> itemService.saveOption(newItemMaster.getId(), m));


        List<ItemOption> results = itemService.getAllByItemId(newItemMaster.getId());

        assertThat(results.size()).isEqualTo(2);
        results.forEach(m -> log.info("ItemOption: {}", m));
    }

    @Test
    @Order(9)
    void 상품옵션을_등록한다() {
        ItemOption saved = itemService.saveOption(newItemMaster.getId(), newItemOption);

        assertThat(saved).isNotNull();
        log.info("ItemOption: {}", saved);
        newItemOption = saved;
    }

    @Test
    @Order(10)
    void 상품옵션을_조회한다() {
        ItemOption result = itemService.findOption(newItemOption.getId());

        assertThat(result).isNotNull();
        assertThat(newItemOption.getId()).isEqualTo(result.getId());
        log.info("ItemOption: {}", result);
    }

    @Test
    @Order(11)
    void 상품옵션을_수정한다() {
        String name = "수정";
        ItemMaster master2 = itemService.saveMaster(newCategory.getCateCode(), new ItemMaster("TEST_ITEM_2", null, null, null));
        log.info("ItemOption: {}", newItemOption);

        ItemOption updated = itemService.updateItemOption(newItemOption.getId(), master2.getId(), name);

        assertThat(updated).isNotNull();
        assertThat(name).isEqualTo(updated.getOptionName());
        assertThat(master2.getId()).isEqualTo(updated.getMaster().getId());
        log.info("ItemOption Updated: {}", updated);
    }

    @Test
    @Order(12)
    void 옵션별_서브_리스트를_조회한다() {
        List<ItemSub> subList = Arrays.asList(
                new ItemSub("서브1", 1000, 10, null, null, null),
                new ItemSub("서브2", 1500, 10, null, null, null)
        );
        subList.forEach(m -> itemService.saveSub(newItemOption.getId(), m));


        List<ItemSub> results = itemService.getAllByOptionId(newItemOption.getId());

        assertThat(results.size()).isEqualTo(2);
        results.forEach(m -> log.info("ItemSub: {}", m));
    }

    @Test
    @Order(13)
    void 상품서브를_등록한다() {
        ItemSub saved = itemService.saveSub(newItemOption.getId(), newItemSub);

        assertThat(saved).isNotNull();
        log.info("ItemOption: {}", saved);
        newItemSub = saved;
    }

    @Test
    @Order(14)
    void 상품서브를_조회한다() {
        ItemSub result = itemService.findSub(newItemSub.getId());

        assertThat(result).isNotNull();
        assertThat(newItemSub.getId()).isEqualTo(result.getId());
        log.info("ItemSub: {}", result);
    }

    @Test
    @Order(15)
    void 상품서브를_수정한다() {
        String name = "수정";
        int price = 1200;
        ItemOption option2 = itemService.saveOption(newItemMaster.getId(), new ItemOption("TEST_OPTION_2", null, null, null));
        log.info("ItemSub: {}", newItemSub);

        ItemSub updated = itemService.updateItemSub(newItemSub.getId(), option2.getId(), price, name);

        assertThat(updated).isNotNull();
        assertThat(name).isEqualTo(updated.getSubName());
        assertThat(price).isEqualTo(updated.getPrice());
        assertThat(option2.getId()).isEqualTo(updated.getOption().getId());
        log.info("ItemSub Updated: {}", updated);
    }

    @Test
    @Order(16)
    void 상품_재고를_수정한다() {
        int amount = -1;
        int beforeStock = newItemSub.getStock();
        log.info("ItemSub: {}", newItemSub);

        ItemSub updated = itemService.updateStock(newItemSub.getId(), amount);

        assertThat(updated).isNotNull();
        assertThat(updated.getStock()).isEqualTo(beforeStock + amount);
        log.info("ItemSub Updated: {}", updated);
    }

    @Test
    @Order(17)
    void 상품서브를_삭제한다() {
        ItemSub result = itemService.deleteSub(newItemSub.getId());

        assertThat(result).isNotNull();
    }

    @Test
    @Order(18)
    void 상품옵션을_삭제한다() {
        ItemOption result = itemService.deleteOption(newItemOption.getId());

        assertThat(result).isNotNull();
    }

    @Test
    @Order(19)
    void 상품마스터를_삭제한다() {
        ItemMaster result = itemService.deleteMaster(newItemMaster.getId());
        assertThat(result).isNotNull();
    }

    @Test
    @Order(20)
    void 카테고리를_삭제한다() {
        Category result = itemService.deleteCategory(newCategory.getCateCode());

        assertThat(result).isNotNull();
    }
}
