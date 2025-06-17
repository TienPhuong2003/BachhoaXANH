package com.orebi.service.user.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.UserDTO;
import com.orebi.entity.User;
import com.orebi.mapper.UserMapper;
import com.orebi.repository.CartRepository;
import com.orebi.repository.UserRepository;
import com.orebi.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CartRepository cartRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cartRepository = cartRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDTOList(users);
    }

    @Override
    public Optional<UserDTO> getUserById(Long userId) {
        return userRepository.findById(userId).map(userMapper::toDTO);
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Override
    @Transactional
    public Optional<UserDTO> updateUser(Long userId, UserDTO userDTO) {
        return userRepository.findById(userId).map(existingUser -> {
            Optional.ofNullable(userDTO.getAddress()).ifPresent(existingUser::setAddress);
            Optional.ofNullable(userDTO.getName()).ifPresent(existingUser::setName);
            Optional.ofNullable(userDTO.getEmail()).ifPresent(existingUser::setEmail);
            Optional.ofNullable(userDTO.getPhone()).ifPresent(existingUser::setPhone);
            Optional.ofNullable(userDTO.isActive()).ifPresent(existingUser::setActive);
            Optional.ofNullable(userDTO.getDistrict()).ifPresent(existingUser::setDistrict);
            Optional.ofNullable(userDTO.getProvince()).ifPresent(existingUser::setProvince);
            Optional.ofNullable(userDTO.getWard()).ifPresent(existingUser::setWard);
            User updatedUser = userRepository.save(existingUser);
            return userMapper.toDTO(updatedUser);
        });
    }

    @Override
    public Optional<UserDTO> getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username).map(user -> {
                UserDTO userDTO = userMapper.toDTO(user);
                userDTO.setRole(user.getRole().getRoleName());

                return userDTO;
            });
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        cartRepository.findByUser_UserId(userId)
                .ifPresent(cart -> cartRepository.delete(cart));
        userRepository.deleteById(userId);
    }
}
