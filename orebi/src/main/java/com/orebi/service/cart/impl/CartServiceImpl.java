package com.orebi.service.cart.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.CartDTO;
import com.orebi.dto.request.CheckoutRequest;
import com.orebi.dto.request.UpdateCartRequest;
import com.orebi.entity.Cart;
import com.orebi.entity.LineItem;
import com.orebi.entity.Order;
import com.orebi.entity.OrderDetail;
import com.orebi.entity.OrderStatus;
import com.orebi.entity.Product;
import com.orebi.entity.User;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.CartMapper;
import com.orebi.mapper.LineItemMapper;
import com.orebi.mapper.OrderMapper;
import com.orebi.repository.CartRepository;
import com.orebi.repository.LineItemRepository;
import com.orebi.repository.OrderRepository;
import com.orebi.repository.ProductRepository;
import com.orebi.repository.UserRepository;
import com.orebi.security.CustomUserDetails;
import com.orebi.service.cart.CartService;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final LineItemRepository lineItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final LineItemMapper lineItemMapper;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    public CartServiceImpl(CartRepository cartRepository,
            LineItemRepository lineItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            CartMapper cartMapper,
            LineItemMapper lineItemMapper,
            OrderMapper orderMapper,
            OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.lineItemRepository = lineItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.lineItemMapper = lineItemMapper;
        this.orderMapper = orderMapper;
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
        cartDTO.setLineItems(lineItemMapper.toDTOList(cart.getLineItems()));
        return Optional.of(cartDTO);
    }

    @Override
    @Transactional
    public CartDTO updateCartItems(List<UpdateCartRequest.CartItemUpdate> items) {
        Long userId = getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);

        for (UpdateCartRequest.CartItemUpdate item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            Optional<LineItem> existingLineItemOpt = lineItemRepository.findByCartAndProduct(cart, product);

            if (item.getQuantity() <= 0) {
                existingLineItemOpt.ifPresent(lineItem -> {
                    lineItemRepository.delete(lineItem);
                    cart.getLineItems().remove(lineItem);
                });
            } else {
                LineItem lineItem = existingLineItemOpt.orElseGet(() -> {
                    LineItem newItem = new LineItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    return newItem;
                });

                lineItem.setQuantity(item.getQuantity());
                calculateTotalPrice(lineItem);
                lineItemRepository.save(lineItem);
            }
        }

        CartDTO cartDTO = cartMapper.toDTO(cart);
        cartDTO.setLineItems(lineItemMapper.toDTOList(cart.getLineItems()));
        return cartDTO;
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
