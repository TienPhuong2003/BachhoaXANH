package com.orebi.service.orderdetail.impl;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.entity.OrderDetail;
import com.orebi.mapper.OrderDetailMapper;
import com.orebi.repository.OrderDetailRepository;
import com.orebi.service.orderdetail.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderDetailMapper orderDetailMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailMapper = orderDetailMapper;
    }

    @Override
    public OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(orderDetailDTO);
        return orderDetailMapper.toDTO(orderDetailRepository.save(orderDetail));
    }

    @Override
    public OrderDetailDTO getOrderDetailById(Long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .map(orderDetailMapper::toDTO)
                .orElse(null);
    }

    @Override
    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrder_OrderId(orderId)
                .stream()
                .map(orderDetailMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDetailDTO updateOrderDetail(Long orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetail existing = orderDetailRepository.findById(orderDetailId).orElse(null);
        if (existing == null) return null;
        existing.setQuantity(orderDetailDTO.getQuantity());
        existing.setUnitPrice(orderDetailDTO.getUnitPrice());
        existing.setTotalPrice(orderDetailDTO.getTotalPrice());
        existing.setSnapshotProductName(orderDetailDTO.getSnapshotProductName());
        existing.setSnapshotProductImage(orderDetailDTO.getSnapshotProductImage());
        existing.setSnapshotPrice(orderDetailDTO.getSnapshotPrice());
        return orderDetailMapper.toDTO(orderDetailRepository.save(existing));
    }

    @Override
    public void deleteOrderDetail(Long orderDetailId) {
        orderDetailRepository.deleteById(orderDetailId);
    }
}