package com.orebi.service.category.impl;

import com.orebi.dto.CategoryDTO;
import com.orebi.entity.Category;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.CategoryMapper;
import com.orebi.mapper.SubCategoryMapper;
import com.orebi.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.orebi.service.category.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final SubCategoryMapper subCategoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
            CategoryMapper categoryMapper,
            SubCategoryMapper subCategoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.subCategoryMapper = subCategoryMapper;
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
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with ID " + id + " not found"));

        category.setName(categoryDTO.getName());
        category.setImage(categoryDTO.getImage());

        if (categoryDTO.getSubCategories() != null) {
            category.setSubCategories(subCategoryMapper.toEntityList(categoryDTO.getSubCategories()));
            category.getSubCategories().forEach(subCategory -> subCategory.setCategory(category));
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category with ID " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
}