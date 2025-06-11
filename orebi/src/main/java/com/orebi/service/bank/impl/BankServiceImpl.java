package com.orebi.service.bank.impl;

import java.time.LocalDateTime;

import com.orebi.entity.OrderStatus;
import com.orebi.entity.PaymentMethod;
import com.orebi.service.bank.BankService;
import com.orebi.entity.Order;
import com.orebi.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BankServiceImpl implements BankService {

    private final OrderRepository orderRepository;
    public BankServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    @Transactional
    public void handleBankTransfer(Long orderId, String imageUrl) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getPaymentMethod() != PaymentMethod.BANKING) {
            throw new RuntimeException("Phương thức thanh toán không hợp lệ");
        }
        if (order.getStatus() != OrderStatus.PENDING || order.getStatus() != OrderStatus.CANCELLED) {
            throw new RuntimeException("Đơn hàng không ở trạng thái chờ xác nhận");
        }
        order.setBankTransferImage(imageUrl);
        order.setPaymentNote("Đã gửi hình ảnh chuyển khoản ngân hàng");
        order.setStatus(OrderStatus.PENDING_PAYMENT); 
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public void confirmBankTransfer(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new RuntimeException("Đơn hàng không ở trạng thái chờ xác nhận");
        }

        order.setStatus(OrderStatus.PAYMENT_SUCCESS); 
        order.setIsPaid(true);
        order.setPaymentNote("thanh toán ngân hàng thành công");
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

}
