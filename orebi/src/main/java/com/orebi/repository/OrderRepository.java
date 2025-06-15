package com.orebi.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orebi.dto.UserDTO;
import com.orebi.entity.Order;
import com.orebi.entity.OrderStatus;
import com.orebi.entity.PaymentMethod;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
        List<Order> findByUser_UserId(Long userId);

        List<Order> findByUserOrderByOrderDateDesc(UserDTO user);

        List<Order> findByIsPaidFalseAndReminderSentFalseAndPaymentMethodAndCreatedAtBefore(
                        PaymentMethod paymentMethod,
                        LocalDateTime cutoff);

        List<Order> findByIsPaidTrueAndPaymentMethodAndCreatedAtBefore(
                        PaymentMethod paymentMethod,
                        LocalDateTime cutoff);

        List<Order> findByStatus(OrderStatus status);

        Order findByOrderId(Long orderId);
}
