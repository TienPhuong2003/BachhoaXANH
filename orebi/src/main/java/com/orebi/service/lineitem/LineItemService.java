package com.orebi.service.lineitem;

import java.util.List;

import com.orebi.dto.LineItemDTO;

public interface LineItemService {
    List<LineItemDTO> getLineItemsByCartId(Long cartId);

    LineItemDTO UpdateLineItem(Long cartId, Long productId, int quantity);

    void deleteLineItems(List<Long> lineItemIds);

    LineItemDTO getLineItemById(Long lineItemId);

}
