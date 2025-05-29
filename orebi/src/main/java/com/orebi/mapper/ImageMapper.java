package com.orebi.mapper;

import com.orebi.dto.ImageDTO;
import com.orebi.entity.Image;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper implements EntityMapper<ImageDTO, Image> {

    @Override
    public ImageDTO toDTO(Image entity) {
        if (entity == null) {
            return null;
        }
        ImageDTO dto = new ImageDTO();
        dto.setImageId(entity.getImageId());
        dto.setImageUrl(entity.getImageUrl());
        dto.setPublicId(entity.getPublicId());
        dto.setTargetId(entity.getTargetId());
        dto.setTargetType(entity.getTargetType());
        return dto;
    }

    @Override
    public Image toEntity(ImageDTO dto) {
        if (dto == null) {
            return null;
        }
        Image entity = new Image();
        entity.setImageId(dto.getImageId());
        entity.setImageUrl(dto.getImageUrl());
        entity.setPublicId(dto.getPublicId());
        entity.setTargetId(dto.getTargetId());
        entity.setTargetType(dto.getTargetType());
        return entity;
    }
}
