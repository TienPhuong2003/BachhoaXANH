package com.orebi.dto.request;

import java.util.List;

import com.orebi.dto.request.otherDTO.ProductTransferItem;

public class StoreTransferRequest {
    private Long sourceInventoryId;
    private Long targetInventoryId;
    private List<ProductTransferItem> items;

    public Long getSourceInventoryId() {
        return sourceInventoryId;
    }

    public void setSourceInventoryId(Long sourceInventoryId) {
        this.sourceInventoryId = sourceInventoryId;
    }

    public Long getTargetInventoryId() {
        return targetInventoryId;
    }

    public void setTargetInventoryId(Long targetInventoryId) {
        this.targetInventoryId = targetInventoryId;
    }

    public List<ProductTransferItem> getItems() {
        return items;
    }

    public void setItems(List<ProductTransferItem> items) {
        this.items = items;
    }

}
