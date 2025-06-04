package com.orebi.service.lineitem;

import java.util.List;

import com.orebi.dto.LineItemDTO;

public interface LineItemService {
    List<LineItemDTO> getLineItemsByCartId(Long cartId);

    LineItemDTO UpdateLineItem(Long cartId, Long productId, int quantity);

    void deleteLineItem(Long lineItemId);

    LineItemDTO getLineItemById(Long lineItemId);

    void deleteLineItems(List<Long> lineItemIds);
}
