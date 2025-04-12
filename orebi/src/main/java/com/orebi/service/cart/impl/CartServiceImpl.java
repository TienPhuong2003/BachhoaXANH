package com.orebi.service.cart.impl;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orebi.dto.CartDTO;
import com.orebi.entity.Cart;
import com.orebi.entity.LineItem;
import com.orebi.entity.Product;
import com.orebi.entity.User;
import com.orebi.exception.ResourceNotFoundException;
import com.orebi.mapper.CartMapper;
import com.orebi.mapper.LineItemMapper;
import com.orebi.repository.CartRepository;
import com.orebi.repository.LineItemRepository;
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

    public CartServiceImpl(CartRepository cartRepository,
                           LineItemRepository lineItemRepository,
                           ProductRepository productRepository,
                           UserRepository userRepository,
                           CartMapper cartMapper,
                           LineItemMapper lineItemMapper) {
        this.cartRepository = cartRepository;
        this.lineItemRepository = lineItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.lineItemMapper = lineItemMapper;
    }
    private Long getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
    public CartDTO addItemToCart(Long productId, int quantity) {
        Long userId = getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product không tồn tại"));

        LineItem lineItem = lineItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> {
                    LineItem newItem = new LineItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setQuantity(0); 
                    return newItem;
                });

        lineItem.setQuantity(lineItem.getQuantity() + quantity);
        lineItemRepository.save(lineItem);

        CartDTO cartDTO = cartMapper.toDTO(cart);
        cartDTO.setLineItems(lineItemMapper.toDTOList(cart.getLineItems()));
        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO removeItemFromCart(Long productId) {
        Long userId = getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + productId));

        lineItemRepository.findByCartAndProduct(cart, product).ifPresent(lineItem -> {
            lineItemRepository.delete(lineItem);
            cart.getLineItems().remove(lineItem);
        });

        cartRepository.save(cart);

        CartDTO cartDTO = cartMapper.toDTO(cart);
        cartDTO.setLineItems(lineItemMapper.toDTOList(cart.getLineItems()));
        return cartDTO;
    }

    @Override
    @Transactional
    public void clearCart() {
        Long userId = getCurrentUserId();
        Cart cart = getOrCreateCartEntity(userId);

        lineItemRepository.deleteByCart(cart);
        cart.getLineItems().clear();
        cartRepository.save(cart);
    }
}
