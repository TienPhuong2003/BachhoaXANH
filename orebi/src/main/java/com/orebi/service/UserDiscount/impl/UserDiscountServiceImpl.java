package com.orebi.service.UserDiscount.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.orebi.dto.UserDiscountDTO;
import com.orebi.entity.UserDiscount;
import com.orebi.helper.SecurityHelper;
import com.orebi.mapper.UserDiscountMapper;
import com.orebi.repository.DiscountRepository;
import com.orebi.repository.UserDiscountRepository;
import com.orebi.repository.UserRepository;
import com.orebi.service.UserDiscount.UserDiscountService;

@Service
public class UserDiscountServiceImpl implements UserDiscountService {

    private final UserDiscountRepository userDiscountRepository;
    private final UserRepository userRepository;
    private final DiscountRepository discountRepository;
    private final UserDiscountMapper userDiscountMapper;
    private final SecurityHelper helper;

    public UserDiscountServiceImpl(UserDiscountRepository userDiscountRepository,
            UserRepository userRepository,
            DiscountRepository discountRepository,
            UserDiscountMapper userDiscountMapper,
            SecurityHelper helper) {
        this.userDiscountRepository = userDiscountRepository;
        this.userRepository = userRepository;
        this.discountRepository = discountRepository;
        this.userDiscountMapper = userDiscountMapper;
        this.helper = helper;
    }

    @Override
    public List<UserDiscountDTO> getAllByUserId() {
        Long userId = helper.getCurrentUserId();
        return userDiscountRepository.findByUser_UserId(userId)
                .stream()
                .map(userDiscountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDiscountDTO SaveDiscountToUser(Long discountId) {
        Long userId = helper.getCurrentUserId();
        var userOpt = userRepository.findById(userId);
        var discountOpt = discountRepository.findById(discountId);

        if (userOpt.isEmpty() || discountOpt.isEmpty()) {
            throw new IllegalArgumentException("User or Discount not found");
        }

        var existing = userDiscountRepository.findByUser_UserIdAndDiscount_Id(userId, discountId);
        if (existing.isPresent()) {
            return userDiscountMapper.toDTO(existing.get());
        }

        UserDiscount userDiscount = new UserDiscount();
        userDiscount.setUser(userOpt.get());
        userDiscount.setDiscount(discountOpt.get());
        userDiscount.setUsed(false);
        userDiscount.setAssignedAt(LocalDateTime.now());

        userDiscount = userDiscountRepository.save(userDiscount);
        return userDiscountMapper.toDTO(userDiscount);
    }
}
