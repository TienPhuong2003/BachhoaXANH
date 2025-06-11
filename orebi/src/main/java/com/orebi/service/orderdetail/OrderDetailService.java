package com.orebi.service.orderdetail;

import java.util.List;

import com.orebi.dto.OrderDetailDTO;
import com.orebi.entity.LineItem;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;

public interface OrderDetailService {
    List<OrderDetail> createFromLineItems(Order order, List<LineItem> items);

    List<OrderDetailDTO> getByOrderId(Long orderId);

    OrderDetailDTO getById(Long orderDetailId);

    void deleteById(Long orderDetailId);

}
