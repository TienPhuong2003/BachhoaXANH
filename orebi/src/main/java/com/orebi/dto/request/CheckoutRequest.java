package com.orebi.dto.request;

import java.util.List;
import com.orebi.dto.OrderDTO;
public class CheckoutRequest {
    private List<Long> selectedLineItemIds;
    private OrderDTO order;
    
    public OrderDTO getOrder() {
        return order;
    }
    public void setOrder(OrderDTO order) {
        this.order = order;
    }
    public List<Long> getSelectedLineItemIds() {
        return selectedLineItemIds;
    }
    public void setSelectedLineItemIds(List<Long> selectedLineItemIds) {
        this.selectedLineItemIds = selectedLineItemIds;
    }
    
}
