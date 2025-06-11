package com.orebi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.entity.Order;
import com.orebi.entity.User;
import com.orebi.dto.UserDTO;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_UserId(Long userId);

    List<Order> findByUserOrderByOrderDateDesc(UserDTO user);
}
