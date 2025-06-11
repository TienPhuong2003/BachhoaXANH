package com.orebi.scheduler;

import com.orebi.entity.Order;
import com.orebi.entity.OrderStatus;
import com.orebi.entity.PaymentMethod;
import com.orebi.repository.OrderRepository;
import com.orebi.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class OrderReminderScheduler {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedRate = 10 * 60 * 1000) 
    public void processUnpaidBankTransfers() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime reminderCutoff = now.minusMinutes(15);
        List<Order> ordersToRemind = orderRepository
                .findByIsPaidFalseAndReminderSentFalseAndPaymentMethodAndCreatedAtBefore(
                        PaymentMethod.BANKING,
                        reminderCutoff
                );

        for (Order order : ordersToRemind) {
            try {
                emailService.sendBankTransferReminderEmail(
                        order.getUser().getEmail(),
                        order.getOrderId()
                );

                order.setReminderSent(true); 
                orderRepository.save(order);
            } catch (Exception e) {
                System.err.println("Lỗi gửi email nhắc nhở cho đơn hàng #" + order.getOrderId());
            }
        }

        LocalDateTime cancelCutoff = now.minusHours(24);
        List<Order> ordersToCancel = orderRepository
                .findByIsPaidTrueAndPaymentMethodAndCreatedAtBefore(
                        PaymentMethod.BANKING,
                        cancelCutoff
                );

        for (Order order : ordersToCancel) {
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            try {
                emailService.sendOrderCancelledEmail(
                        order.getUser().getEmail(),
                        order.getOrderId()
                );
            } catch (Exception e) {
                System.err.println("Lỗi gửi email hủy đơn hàng #" + order.getOrderId());
            }
        }
    }
}
