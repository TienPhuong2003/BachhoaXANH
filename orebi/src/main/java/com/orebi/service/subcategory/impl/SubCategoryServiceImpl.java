package com.orebi.service.subcategory.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.SubCategoryDTO;
import com.orebi.entity.SubCategory;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.SubCategoryMapper;
import com.orebi.repository.SubCategoryRepository;
import com.orebi.service.subcategory.SubCategoryService;

@Service
@Transactional
public class SubCategoryServiceImpl implements SubCategoryService {
    private final SubCategoryRepository subCategoryRepository;
    private final SubCategoryMapper subCategoryMapper;

    public SubCategoryServiceImpl(SubCategoryRepository subCategoryRepository, SubCategoryMapper subCategoryMapper) {
        this.subCategoryRepository = subCategoryRepository;
        this.subCategoryMapper = subCategoryMapper;
    }

    @Override
    public List<SubCategoryDTO> getAllSubCategories() {
        List<SubCategory> subCategories = subCategoryRepository.findAll();
        return subCategoryMapper.toDTOList(subCategories);
    }

    @Override
    public SubCategoryDTO getSubCategoryById(Long id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory with ID " + id + " not found"));
        return subCategoryMapper.toDTO(subCategory);
    }

    @Override
    public SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO) {
        SubCategory subCategory = subCategoryMapper.toEntity(subCategoryDTO);
        SubCategory savedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toDTO(savedSubCategory);
    }

    @Override
    public SubCategoryDTO updateSubCategory(Long id, SubCategoryDTO subCategoryDTO) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory with ID " + id + " not found"));

        subCategory.setName(subCategoryDTO.getName());
        SubCategory updatedSubCategory = subCategoryRepository.save(subCategory);
        return subCategoryMapper.toDTO(updatedSubCategory);
    }

    @Override
    public void deleteSubCategory(Long id) {
        if (!subCategoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("SubCategory with ID " + id + " not found");
        }
        subCategoryRepository.deleteById(id);
    }
}
