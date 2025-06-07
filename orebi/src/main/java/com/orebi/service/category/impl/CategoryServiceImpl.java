package com.orebi.service.category.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.CategoryDTO;
import com.orebi.dto.SubCategoryDTO;
import com.orebi.entity.Category;
import com.orebi.entity.Product;
import com.orebi.entity.SubCategory;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.CategoryMapper;
import com.orebi.repository.CategoryRepository;
import com.orebi.repository.ProductRepository;
import com.orebi.repository.SubCategoryRepository;
import com.orebi.service.category.CategoryService;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            SubCategoryRepository subCategoryRepository,
            ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toDTOList(categories);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));
        return categoryMapper.toDTO(category);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        if (category.getSubCategories() == null) {
            if (category.getSubCategories() != null) {
                for (SubCategory sub : category.getSubCategories()) {
                    sub.setCategory(null);
                    subCategoryRepository.save(sub);
                }
            }
            category.setSubCategories(null);
        } else {
            category.getSubCategories().forEach(subCategory -> subCategory.setCategory(category));
        }
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));

        category.setName(categoryDTO.getName());

        List<Long> newSubCategoryIds = (categoryDTO.getSubCategories() == null) ? List.of()
                : categoryDTO.getSubCategories().stream()
                        .map(SubCategoryDTO::getSubCategoryId)
                        .collect(Collectors.toList());

        // Lấy danh sách subcategory hiện tại
        List<SubCategory> currentSubs = category.getSubCategories() != null ? category.getSubCategories() : List.of();

        // Ngắt liên kết các subcategory không còn trong danh sách mới
        for (SubCategory sub : currentSubs) {
            if (!newSubCategoryIds.contains(sub.getSubCategoryId())) {
                sub.setCategory(null);
                subCategoryRepository.save(sub);
            }
        }

        // Liên kết các subcategory mới
        List<SubCategory> newSubs = newSubCategoryIds.isEmpty() ? new java.util.ArrayList<>()
                : subCategoryRepository.findAllById(newSubCategoryIds);

        newSubs.forEach(sub -> sub.setCategory(category));
        category.setSubCategories(newSubs);

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));
        List<Product> products = productRepository.findByCategory(category);
        for (Product product : products) {
            product.setCategory(null);
        }

        productRepository.saveAll(products);
        categoryRepository.deleteById(id);
    }
}