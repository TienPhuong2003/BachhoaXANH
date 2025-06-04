package com.orebi.service.cart.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.LineItemDTO;
import com.orebi.dto.request.CheckoutRequest;
import com.orebi.entity.Cart;
import com.orebi.entity.LineItem;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.OrderStatus;
import com.orebi.entity.Product;
import com.orebi.entity.User;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.CartMapper;
import com.orebi.repository.CartRepository;
import com.orebi.repository.LineItemRepository;
import com.orebi.repository.OrderRepository;
import com.orebi.repository.UserRepository;
import com.orebi.security.CustomUserDetails;
import com.orebi.service.cart.CartService;
import com.orebi.service.lineitem.LineItemService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final LineItemRepository lineItemRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final OrderRepository orderRepository;
    private final LineItemService lineItemService;

    public CartServiceImpl(CartRepository cartRepository, LineItemRepository lineItemRepository,
            UserRepository userRepository, CartMapper cartMapper, OrderRepository orderRepository,
            LineItemService lineItemService) {
        this.lineItemService = lineItemService;
        this.cartRepository = cartRepository;
        this.lineItemRepository = lineItemRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.orderRepository = orderRepository;
    }

    private Long getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUserId();
    }

    private Cart getOrCreateCartEntity(Long userId) {
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
        Long userId = getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);
        CartDTO cartDTO = cartMapper.toDTO(cart);
        List<LineItemDTO> lineItems = lineItemService.getLineItemsByCartId(cart.getCartId());
        cartDTO.setLineItems(lineItems);
        return Optional.of(cartDTO);
    }

    @Override
    @Transactional
    public void checkout(CheckoutRequest checkoutRequest) {
        Long userId = getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);
        User user = cart.getUser();

        List<LineItem> selectedItems = cart.getLineItems().stream()
                .filter(item -> checkoutRequest.getLineItemIds().contains(item.getLineItemId()))
                .toList();

        if (selectedItems.isEmpty()) {
            throw new IllegalStateException("Không có sản phẩm nào được chọn để đặt hàng.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(checkoutRequest.getPaymentMethod());
        order.setShippingAddress(checkoutRequest.getShippingAddress());
        order.setPhone(checkoutRequest.getPhone());
        order.setShippingFee(checkoutRequest.getShippingFee());
        order.setRecipientName(checkoutRequest.getRecipientName());
        order.setRecipientPhone(checkoutRequest.getRecipientPhone());
        order.setNote(checkoutRequest.getNote());
        order.setIsPaid(false);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        double totalOrderPrice = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (LineItem item : selectedItems) {
            Product product = item.getProduct();
            double unitPrice = product.getDiscountedPrice() > 0 ? product.getDiscountedPrice()
                    : product.getOriginalPrice();
            int quantity = item.getQuantity();
            double totalPrice = unitPrice * quantity;

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setTotalPrice(totalPrice);
            detail.setSnapshotProductId(product.getProductId());
            detail.setSnapshotProductName(product.getName());
            detail.setSnapshotPrice(unitPrice);

            orderDetails.add(detail);
            totalOrderPrice += totalPrice;
        }

        order.setTotalPrice(totalOrderPrice);
        order.setOrderDetails(orderDetails);

        orderRepository.save(order);

        lineItemRepository.deleteAll(selectedItems);
        cart.getLineItems().removeAll(selectedItems);
    }

    // helper method
    private void calculateTotalPrice(LineItem lineItem) {
        Product product = lineItem.getProduct();
        double price = product.getDiscountedPrice() != 0 ? product.getDiscountedPrice()
                : product.getOriginalPrice();
        lineItem.setTotalPrice(price * lineItem.getQuantity());
    }
}
