package com.orebi.service.lineitem.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orebi.dto.LineItemDTO;
import com.orebi.entity.Cart;
import com.orebi.entity.LineItem;
import com.orebi.entity.Product;
import com.orebi.mapper.LineItemMapper;
import com.orebi.repository.CartRepository;
import com.orebi.repository.LineItemRepository;
import com.orebi.repository.ProductRepository;
import com.orebi.service.lineitem.LineItemService;

@Service
public class LineItemServiceImpl implements LineItemService {

    private final LineItemRepository lineItemRepository;
    private final LineItemMapper lineItemMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public LineItemServiceImpl(LineItemRepository lineItemRepository, LineItemMapper lineItemMapper,
            CartRepository cartRepository, ProductRepository productRepository) {
        this.lineItemRepository = lineItemRepository;
        this.lineItemMapper = lineItemMapper;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<LineItemDTO> getLineItemsByCartId(Long cartId) {
        List<LineItem> items = lineItemRepository.findByCart_CartId(cartId);
        return items.stream().map(lineItemMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public LineItemDTO UpdateLineItem(Long cartId, Long productId, int quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<LineItem> existing = lineItemRepository.findByCartAndProduct(cart, product);

        if (quantity == 0) {
            existing.ifPresent(lineItemRepository::delete);
            return null;
        }

        LineItem lineItem;
        if (existing.isPresent()) {
            lineItem = existing.get();
            lineItem.setQuantity(quantity);
        } else {
            lineItem = new LineItem();
            lineItem.setCart(cart);
            lineItem.setProduct(product);
            lineItem.setQuantity(quantity);
        }
        calculateTotalPrice(lineItem);

        LineItem saved = lineItemRepository.save(lineItem);
        return lineItemMapper.toDTO(saved);
    }

    @Override
    public LineItemDTO getLineItemById(Long lineItemId) {
        return lineItemRepository.findById(lineItemId)
                .map(lineItemMapper::toDTO)
                .orElse(null);
    }

    @Override
    public void deleteLineItems(List<Long> lineItemIds) {
        List<LineItem> items = lineItemRepository.findAllById(lineItemIds);
        lineItemRepository.deleteAll(items);
    }

    // helper method
    private void calculateTotalPrice(LineItem lineItem) {
        Product product = lineItem.getProduct();
        double price = product.getDiscountedPrice() != 0 ? product.getDiscountedPrice()
                : product.getOriginalPrice();
        lineItem.setTotalPrice(price * lineItem.getQuantity());
    }

}
