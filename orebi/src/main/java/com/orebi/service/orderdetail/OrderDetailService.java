package com.orebi.service.orderdetail;

import java.util.List;
import com.orebi.dto.OrderDetailDTO;

public interface OrderDetailService {
    OrderDetailDTO createOrderDetail(OrderDetailDTO orderDetailDTO);
    OrderDetailDTO getOrderDetailById(Long orderDetailId);
    List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId);
    OrderDetailDTO updateOrderDetail(Long orderDetailId, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(Long orderDetailId);
}
