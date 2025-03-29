package com.orebi.service.subcategory;

import com.orebi.dto.SubCategoryDTO;
import java.util.List;

public interface SubCategoryService {
    List<SubCategoryDTO> getAllSubCategories();
    SubCategoryDTO getSubCategoryById(Long id);
    SubCategoryDTO createSubCategory(SubCategoryDTO subCategoryDTO);
    SubCategoryDTO updateSubCategory(Long id, SubCategoryDTO subCategoryDTO);
    void deleteSubCategory(Long id);
}
