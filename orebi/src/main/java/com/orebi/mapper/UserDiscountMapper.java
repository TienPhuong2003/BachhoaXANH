package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.UserDiscountDTO;
import com.orebi.entity.UserDiscount;

@Component
public class UserDiscountMapper implements EntityMapper<UserDiscountDTO, UserDiscount> {

    @Override
    public UserDiscountDTO toDTO(UserDiscount entity) {
        if (entity == null)
            return null;

        UserDiscountDTO dto = new UserDiscountDTO();
        dto.setId(entity.getId());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getUserId());
        }
        if (entity.getDiscount() != null) {
            dto.setDiscountId(entity.getDiscount().getId());
        }
        dto.setUsed(entity.isUsed());
        dto.setAssignedAt(entity.getAssignedAt());
        dto.setUsedAt(entity.getUsedAt());

        return dto;
    }

    @Override
    public UserDiscount toEntity(UserDiscountDTO dto) {
        if (dto == null)
            return null;

        UserDiscount entity = new UserDiscount();
        entity.setId(dto.getId());

        if (dto.getUserId() != null) {
            var user = new com.orebi.entity.User();
            user.setUserId(dto.getUserId());
            entity.setUser(user);
        }

        if (dto.getDiscountId() != null) {
            var discount = new com.orebi.entity.Discount();
            discount.setId(dto.getDiscountId());
            entity.setDiscount(discount);
        }

        entity.setUsed(dto.isUsed());
        entity.setAssignedAt(dto.getAssignedAt());
        entity.setUsedAt(dto.getUsedAt());

        return entity;
    }
}
