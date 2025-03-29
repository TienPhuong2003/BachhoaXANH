package com.orebi.controller;

import com.orebi.dto.SubCategoryDTO;
import com.orebi.service.subcategory.SubCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    public SubCategoryController(SubCategoryService subCategoryService) {
        this.subCategoryService = subCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<SubCategoryDTO>> getAllSubCategories() {
        return ResponseEntity.ok(subCategoryService.getAllSubCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategoryDTO> getSubCategoryById(@PathVariable Long id) {
        SubCategoryDTO subCategoryDTO = subCategoryService.getSubCategoryById(id);
        return subCategoryDTO != null ? ResponseEntity.ok(subCategoryDTO) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SubCategoryDTO> createSubCategory(@RequestBody SubCategoryDTO subCategoryDTO) {
        return ResponseEntity.ok(subCategoryService.createSubCategory(subCategoryDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubCategoryDTO> updateSubCategory(@PathVariable Long id, @RequestBody SubCategoryDTO subCategoryDTO) {
        SubCategoryDTO updatedSubCategory = subCategoryService.updateSubCategory(id, subCategoryDTO);
        return updatedSubCategory != null ? ResponseEntity.ok(updatedSubCategory) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void deleteSubCategory(@PathVariable Long id) {
        subCategoryService.deleteSubCategory(id);
    }
}
