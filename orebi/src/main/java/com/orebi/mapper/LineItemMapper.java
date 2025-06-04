package com.orebi.mapper;

import org.springframework.stereotype.Component;

import com.orebi.dto.LineItemDTO;
import com.orebi.entity.LineItem;
import com.orebi.entity.Product;

@Component
public class LineItemMapper implements EntityMapper<LineItemDTO, LineItem> {

    @Override
    public LineItemDTO toDTO(LineItem entity) {
        if (entity == null) {
            return null;
        }
        return new LineItemDTO(
                entity.getLineItemId(),
                entity.getProduct() != null ? entity.getProduct().getProductId() : null,
                entity.getQuantity(),
                entity.getTotalPrice());
    }

    @Override
    public LineItem toEntity(LineItemDTO dto) {
        if (dto == null) {
            return null;
        }
        LineItem lineItem = new LineItem();
        lineItem.setLineItemId(dto.getLineItemId());
        lineItem.setQuantity(dto.getQuantity());
        lineItem.setTotalPrice(dto.getTotalPrice());
        if (dto.getProductId() != null) {
            Product product = new Product();
            product.setProductId(dto.getProductId());
            lineItem.setProduct(product);
        }
        return lineItem;
    }
}
