package com.orebi.service.cart.impl;

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

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final LineItemRepository lineItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final LineItemMapper lineItemMapper;

    public CartServiceImpl(CartRepository cartRepository, LineItemRepository lineItemRepository,
            ProductRepository productRepository, UserRepository userRepository,
            CartMapper cartMapper, LineItemMapper lineItemMapper) {
        this.cartRepository = cartRepository;
        this.lineItemRepository = lineItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.lineItemMapper = lineItemMapper;
    }

    @Override
    @Transactional
    public Optional<CartDTO> getCartByUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        return cartRepository.findByUser_UserId(userId)
                .map(cartMapper::toDTO)
                .or(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    Cart savedCart = cartRepository.save(newCart);
                    return Optional.of(cartMapper.toDTO(savedCart));
                });
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(Long productId, int quantity) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User không tồn tại"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product không tồn tại"));

        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        LineItem lineItem = lineItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> new LineItem());

        lineItem.setCart(cart);
        lineItem.setProduct(product);
        lineItem.setQuantity(lineItem.getQuantity() + quantity);
        lineItemRepository.save(lineItem);

        CartDTO cartDTO = cartMapper.toDTO(cart);
        cartDTO.setLineItems(lineItemMapper.toDTOList(cart.getLineItems()));

        return cartDTO;
    }

    @Override
    @Transactional
    public CartDTO removeItemFromCart(Long productId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getUserId();
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Giỏ hàng không tồn tại"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + productId));

        lineItemRepository.findByCartAndProduct(cart, product).ifPresent(lineItem -> {
            lineItemRepository.delete(lineItem);
            cart.getLineItems().remove(lineItem);
        });
        cartRepository.save(cart);

        return cartMapper.toDTO(cart);
    }

    @Override
    @Transactional
    public void clearCart() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getUserId();
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Giỏ hàng không tồn tại"));

        lineItemRepository.deleteByCart(cart);
        cart.getLineItems().clear();
        cartRepository.save(cart);
    }
}
