package com.orebi.mapper;

import com.orebi.dto.LineItemDTO;
import com.orebi.entity.LineItem;
import org.springframework.stereotype.Component;

@Component
public class LineItemMapper implements EntityMapper<LineItemDTO, LineItem> {

    private final ProductMapper productMapper;

    public LineItemMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public LineItemDTO toDTO(LineItem entity) {
        if (entity == null) {
            return null;
        }
        return new LineItemDTO(
                entity.getLineItemId(),
                productMapper.toDTO(entity.getProduct()),  
                entity.getQuantity(),
                entity.getTotalPrice()
        );
    }

    @Override
    public LineItem toEntity(LineItemDTO dto) {
        if (dto == null) {
            return null;
        }
        LineItem lineItem = new LineItem();
        lineItem.setLineItemId(dto.getLineItemId());
        lineItem.setQuantity(dto.getQuantity());
        lineItem.setProduct(productMapper.toEntity(dto.getProduct()));
        lineItem.setTotalPrice(dto.getTotalPrice());
        return lineItem;
    }
}
