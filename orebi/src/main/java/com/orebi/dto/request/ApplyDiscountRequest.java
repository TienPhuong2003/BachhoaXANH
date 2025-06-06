package com.orebi.dto.request;

import java.util.List;

public class ApplyDiscountRequest {
    private Long discountId;
    private List<Long> productIds;

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }

}
