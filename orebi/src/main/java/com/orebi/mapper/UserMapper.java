package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.UserDTO;
import com.orebi.entity.User;

@Component
public class UserMapper implements EntityMapper<UserDTO, User> {

    @Override
    public UserDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setActive(entity.isActive());
        dto.setUserId(entity.getUserId());
        dto.setAddress(entity.getAddress());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setDistrict(entity.getDistrict());
        dto.setProvince(entity.getProvince());
        dto.setWard(entity.getWard());
        return dto;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        entity.setActive(dto.isActive());
        entity.setUserId(dto.getUserId());
        entity.setAddress(dto.getAddress());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setDistrict(dto.getDistrict());
        entity.setProvince(dto.getProvince());
        entity.setWard(dto.getWard());
        return entity;
    }
}
