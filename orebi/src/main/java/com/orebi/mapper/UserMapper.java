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
        dto.setAddress(entity.getAddress());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    @Override
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        entity.setAddress(dto.getAddress());
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        return entity;
    }
}
