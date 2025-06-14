package com.orebi.service.orderdetail.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.entity.LineItem;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.Product;
import com.orebi.mapper.OrderDetailMapper;
import com.orebi.repository.OrderDetailRepository;
import com.orebi.service.orderdetail.OrderDetailService;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository, OrderDetailMapper orderDetailMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderDetailMapper = orderDetailMapper;
    }

    @Override
    @Transactional
    public List<OrderDetail> createFromLineItems(Order order, List<LineItem> items) {
        List<OrderDetail> details = new ArrayList<>();

        for (LineItem item : items) {
            Product p = item.getProduct();

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setQuantity(item.getQuantity());
            detail.setUnitPrice(p.getDiscountedPrice() != 0 ? p.getDiscountedPrice() : p.getOriginalPrice());
            detail.setTotalPrice(detail.getUnitPrice() * detail.getQuantity());

            detail.setSnapshotProductId(p.getProductId());
            detail.setSnapshotProductName(p.getName());
            detail.setShippingFee(order.getShippingFee());
            detail.setSnapshotPrice(detail.getTotalPrice() + detail.getShippingFee());

            details.add(detail);
        }

        return orderDetailRepository.saveAll(details);
    }

    @Override
    public List<OrderDetailDTO> getByOrderId(Long orderId) {
        List<OrderDetail> details = orderDetailRepository.findByOrder_OrderId(orderId);
        return orderDetailMapper.toDTOList(details);
    }

    @Override
    public OrderDetailDTO getById(Long orderDetailId) {
        OrderDetail detail = orderDetailRepository.findById(orderDetailId)
                .orElseThrow(() -> new RuntimeException("Order detail not found: " + orderDetailId));
        return orderDetailMapper.toDTO(detail);
    }

    @Override
    @Transactional
    public void deleteById(Long orderDetailId) {
        if (!orderDetailRepository.existsById(orderDetailId)) {
            throw new RuntimeException("Order detail not found to delete: " + orderDetailId);
        }
        orderDetailRepository.deleteById(orderDetailId);
    }

}