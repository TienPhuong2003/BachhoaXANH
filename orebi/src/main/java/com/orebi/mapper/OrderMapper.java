package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.OrderDTO;
import com.orebi.entity.Order;
import com.orebi.entity.User;

@Component
public class OrderMapper implements EntityMapper<OrderDTO, Order> {

    private final OrderDetailMapper orderDetailMapper;

    public OrderMapper(OrderDetailMapper orderDetailMapper) {
        this.orderDetailMapper = orderDetailMapper;
    }

    @Override
    public OrderDTO toDTO(Order entity) {
        if (entity == null)
            return null;

        OrderDTO dto = new OrderDTO();
        dto.setOrderId(entity.getOrderId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        dto.setOrderDate(entity.getOrderDate());
        dto.setStatus(entity.getStatus());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setShippingAddress(entity.getShippingAddress());
        dto.setPhone(entity.getPhone());
        dto.setTotalPrice(entity.getTotalPrice());
        dto.setPaid(entity.getIsPaid());
        dto.setTrackingNumber(entity.getTrackingNumber());
        dto.setShippingFee(entity.getShippingFee());
        dto.setRecipientName(entity.getRecipientName());
        dto.setRecipientPhone(entity.getRecipientPhone());
        dto.setBankTransferImage(entity.getBankTransferImage());
        dto.setPaymentNote(entity.getPaymentNote());
        dto.setVnpayTransactionNo(entity.getVnpayTransactionNo());
        dto.setNote(entity.getNote());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        dto.setOrderDetails(orderDetailMapper.toDTOList(entity.getOrderDetails()));
        return dto;
    }

    @Override
    public Order toEntity(OrderDTO dto) {
        if (dto == null)
            return null;

        Order entity = new Order();
        entity.setOrderId(dto.getOrderId());

        if (dto.getUserId() != null) {
            User user = new User();
            user.setUserId(dto.getUserId());
            entity.setUser(user);
        }

        entity.setOrderDate(dto.getOrderDate());
        entity.setStatus(dto.getStatus());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setShippingAddress(dto.getShippingAddress());
        entity.setPhone(dto.getPhone());
        entity.setTotalPrice(dto.getTotalPrice());
        entity.setIsPaid(dto.isPaid());
        entity.setTrackingNumber(dto.getTrackingNumber());
        entity.setShippingFee(dto.getShippingFee());
        entity.setRecipientName(dto.getRecipientName());
        entity.setRecipientPhone(dto.getRecipientPhone());
        entity.setBankTransferImage(dto.getBankTransferImage());
        entity.setPaymentNote(dto.getPaymentNote());
        entity.setVnpayTransactionNo(dto.getVnpayTransactionNo());
        entity.setNote(dto.getNote());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        entity.setOrderDetails(orderDetailMapper.toEntityList(dto.getOrderDetails()));

        if (entity.getOrderDetails() != null) {
            entity.getOrderDetails().forEach(detail -> detail.setOrder(entity));
        }

        return entity;
    }
}
