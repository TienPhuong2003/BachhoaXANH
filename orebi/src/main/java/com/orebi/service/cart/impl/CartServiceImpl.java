package com.orebi.service.cart.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.LineItemDTO;
import com.orebi.dto.OrderDTO;
import com.orebi.entity.Cart;
import com.orebi.entity.LineItem;
import com.orebi.entity.User;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.helper.SecurityHelper;
import com.orebi.mapper.CartMapper;
import com.orebi.repository.CartRepository;
import com.orebi.repository.UserRepository;
import com.orebi.service.cart.CartService;
import com.orebi.service.lineitem.LineItemService;
import com.orebi.service.order.OrderService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final LineItemService lineItemService;
    private final SecurityHelper helper;

    public CartServiceImpl(CartRepository cartRepository, OrderService orderService,
            UserRepository userRepository, CartMapper cartMapper,
            LineItemService lineItemService,
            SecurityHelper helper) {
        this.cartRepository = cartRepository;
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.lineItemService = lineItemService;
        this.helper = helper;
    }

    @Override
    public Cart getOrCreateCartEntity(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));

        return cartRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public Optional<CartDTO> getCartByUserId() {
        Long userId = helper.getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);
        CartDTO cartDTO = cartMapper.toDTO(cart);
        List<LineItemDTO> lineItems = lineItemService.getLineItemsByCartId(cart.getCartId());
        cartDTO.setLineItems(lineItems);
        return Optional.of(cartDTO);
    }

    @Override
    @Transactional
    public OrderDTO checkout(List<Long> selectedLineItemIds, OrderDTO orderDTO) {
        Cart cart = getOrCreateCartEntity(helper.getCurrentUserId());
        User user = cart.getUser();

        List<LineItem> selectedItems = cart.getLineItems().stream()
                .filter(item -> selectedLineItemIds.contains(item.getLineItemId()))
                .toList();

        if (selectedItems.isEmpty()) {
            throw new IllegalStateException("Không có sản phẩm nào được chọn để đặt hàng.");
        }

        // Tạo đơn hàng từ giỏ hàng
        OrderDTO createdOrder = orderService.createOrder(user, selectedItems, orderDTO);

        cart.getLineItems().removeAll(selectedItems);
        cartRepository.save(cart);

        return createdOrder;
    }

}
