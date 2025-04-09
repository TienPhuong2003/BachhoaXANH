package com.orebi.service.discount.impl;

import com.orebi.dto.DiscountCodeDTO;
import com.orebi.entity.DiscountCode;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.DiscountCodeMapper;
import com.orebi.repository.DiscountCodeRepository;
import com.orebi.service.discount.DiscountCodeService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountCodeServiceImpl implements DiscountCodeService {

    private final DiscountCodeRepository discountCodeRepository;
    private final DiscountCodeMapper discountCodeMapper;

    public DiscountCodeServiceImpl(DiscountCodeRepository discountCodeRepository,
            DiscountCodeMapper discountCodeMapper) {
        this.discountCodeMapper = discountCodeMapper;
        this.discountCodeRepository = discountCodeRepository;
    }

    @Override
    public DiscountCodeDTO createDiscountCode(DiscountCodeDTO dto) {
        DiscountCode discountCode = discountCodeMapper.toEntity(dto);
        discountCode.setUsedCount(0); // default
        return discountCodeMapper.toDTO(discountCodeRepository.save(discountCode));
    }

    @Override
    public DiscountCodeDTO updateDiscountCode(Long id, DiscountCodeDTO dto) {
        DiscountCode existing = discountCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountCode not found with id " + id));

        existing.setCode(dto.getCode());
        existing.setDiscountValue(dto.getDiscountValue());
        existing.setPercentage(dto.isPercentage());
        existing.setQuantity(dto.getQuantity());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());

        return discountCodeMapper.toDTO(discountCodeRepository.save(existing));
    }

    @Override
    public void deleteDiscountCode(Long id) {
        if (!discountCodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("DiscountCode not found with id " + id);
        }
        discountCodeRepository.deleteById(id);
    }

    @Override
    public DiscountCodeDTO getDiscountCodeById(Long id) {
        return discountCodeRepository.findById(id)
                .map(discountCodeMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("DiscountCode not found with id " + id));
    }

    @Override
    public List<DiscountCodeDTO> getAllDiscountCodes() {
        return discountCodeMapper.toDTOList(discountCodeRepository.findAll());
    }
}
