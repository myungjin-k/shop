package me.myungjin.shop.item.service;

import lombok.RequiredArgsConstructor;
import me.myungjin.shop.item.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final CategoryRepository categoryRepository;

    private final ItemMasterRepository itemMasterRepository;

    private final ItemOptionRepository itemOptionRepository;

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    public Category saveCategory(Category category) {
        return save(category);
    }

    @Transactional(readOnly = true)
    public Category findCategory(String code) {
        return findCategoryById(code)
                .orElseThrow(() -> new IllegalArgumentException("invalid cate code = " + code));
    }

    @Transactional
    public Category deleteCategory(String code) {
        Category cate = findCategoryById(code)
                .orElseThrow(() -> new IllegalArgumentException("invalid cate code = " + code));
        categoryRepository.delete(cate);
        return cate;
    }

    @Transactional
    public Category updateCategoryName(String code, String name) {
        return categoryRepository.findById(code)
                .map(cate -> {
                    cate.update(name);
                    return save(cate);
                }).orElseThrow(() -> new IllegalArgumentException("invalid cate code = " + code));
    }


    @Transactional(readOnly = true)
    public List<ItemMaster> getAllByCateCode(String code) {
        return findCategoryById(code)
                .map(itemMasterRepository::findAllByCategory)
                .orElse(emptyList());
    }

    @Transactional
    public ItemMaster saveMaster(String cateCode, ItemMaster itemMaster) {
        return findCategoryById(cateCode)
                .map(cate -> {
                    itemMaster.setCategory(cate);
                    return save(itemMaster);
                }).orElseThrow(() -> new IllegalArgumentException("invalid cate code = " + cateCode));

    }

    @Transactional(readOnly = true)
    public ItemMaster findMaster(String id) {
        return findMasterById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid item id = " + id));
    }

    @Transactional
    public ItemMaster deleteMaster(String id) {
        ItemMaster item =  findMasterById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid item id = " + id));
        itemMasterRepository.delete(item);
        return item;
    }


    @Transactional
    public ItemMaster updateItemMaster(String itemId, String cateCode, String itemName) {
        return findMasterById(itemId)
                .map(item -> {
                    Category cate = findCategoryById(cateCode)
                            .orElseThrow(() -> new IllegalArgumentException("invalid cate code = " + cateCode));
                    item.update(cate, itemName);
                    return save(item);
                }).orElseThrow(() -> new IllegalArgumentException("invalid item id = " + itemId));
    }

    @Transactional(readOnly = true)
    public List<ItemOption> getAllByItemId(String itemId) {
        return findMasterById(itemId)
                .map(itemOptionRepository::findAllByMaster)
                .orElse(emptyList());
    }

    @Transactional
    public ItemOption saveOption(String itemId, ItemOption itemOption) {
        return findMasterById(itemId)
                .map(master -> {
                    itemOption.setMaster(master);
                    return save(itemOption);
                }).orElseThrow(() -> new IllegalArgumentException("invalid item id = " + itemId));

    }

    @Transactional(readOnly = true)
    public ItemOption findOption(String id) {
        return findOptionById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid option id = " + id));
    }

    @Transactional
    public ItemOption deleteOption(String id) {
        ItemOption option = findOptionById(id)
                .orElseThrow(() -> new IllegalArgumentException("invalid option id = " + id));
        itemOptionRepository.delete(option);
        return option;
    }

    @Transactional
    public ItemOption updateItemOption(String optionId, String itemId, ItemOption newOption) {
        return findOptionById(optionId)
                .map(option -> {
                    ItemMaster master = findMasterById(itemId)
                            .orElseThrow(() -> new IllegalArgumentException("invalid item id = " + itemId));
                    option.updateDefaultInfo(master, newOption);
                    return save(option);
                }).orElseThrow(() -> new IllegalArgumentException("invalid option id = " + optionId));
    }

    @Transactional
    public ItemOption updateStock(String optionId, int amount) {
        return findOptionById(optionId)
                .map(option -> {
                    option.updateStock(amount);
                    if(option.getStock() < 0)
                        throw new RuntimeException("out of stock = " + optionId);
                    return save(option);
                }).orElseThrow(() -> new IllegalArgumentException("invalid option id = " + optionId));
    }

    private Category save(Category category) {
        return categoryRepository.save(category);
    }

    private ItemMaster save(ItemMaster itemMaster) {
        return itemMasterRepository.save(itemMaster);
    }

    private ItemOption save(ItemOption itemOption) {
        return itemOptionRepository.save(itemOption);
    }

    private Optional<Category> findCategoryById(String code) {
        return categoryRepository.findById(code);
    }

    private Optional<ItemMaster> findMasterById(String id) {
        return itemMasterRepository.findById(id);
    }

    private Optional<ItemOption> findOptionById(String id) {
        return itemOptionRepository.findById(id);
    }

}
