package com.orebi.service.discount;

import com.orebi.dto.DiscountCodeDTO;

import java.util.List;

public interface DiscountCodeService {
    DiscountCodeDTO createDiscountCode(DiscountCodeDTO dto);
    DiscountCodeDTO updateDiscountCode(Long id, DiscountCodeDTO dto);
    void deleteDiscountCode(Long id);
    DiscountCodeDTO getDiscountCodeById(Long id);
    List<DiscountCodeDTO> getAllDiscountCodes();
}
