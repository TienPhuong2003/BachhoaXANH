package com.orebi.controller;

import com.orebi.dto.DiscountCodeDTO;
import com.orebi.service.discount.DiscountCodeService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount-codes")
public class DiscountCodeController {

    private final DiscountCodeService discountCodeService;

    public DiscountCodeController(DiscountCodeService discountCodeService){
        this.discountCodeService = discountCodeService;
    }

    @PostMapping
    public DiscountCodeDTO create(@RequestBody DiscountCodeDTO dto) {
        return discountCodeService.createDiscountCode(dto);
    }

    @PutMapping("/{id}")
    public DiscountCodeDTO update(@PathVariable Long id, @RequestBody DiscountCodeDTO dto) {
        return discountCodeService.updateDiscountCode(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        discountCodeService.deleteDiscountCode(id);
    }

    @GetMapping("/{id}")
    public DiscountCodeDTO getById(@PathVariable Long id) {
        return discountCodeService.getDiscountCodeById(id);
    }

    @GetMapping
    public List<DiscountCodeDTO> getAll() {
        return discountCodeService.getAllDiscountCodes();
    }
}
