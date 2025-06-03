package com.orebi.service.user;

import java.util.List;
import java.util.Optional;

import com.orebi.dto.UserDTO;

public interface UserService {
    List<UserDTO> getAllUsers();

    Optional<UserDTO> getUserById(Long userId);

    UserDTO createUser(UserDTO userDTO);

    Optional<UserDTO> updateUser(Long userId, UserDTO userDTO);

    void deleteUser(Long userId);

    Optional<UserDTO> getCurrentUser();
}
