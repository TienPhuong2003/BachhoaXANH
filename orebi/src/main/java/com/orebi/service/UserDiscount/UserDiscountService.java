package com.orebi.service.UserDiscount;

import java.util.List;

import com.orebi.dto.UserDiscountDTO;

public interface UserDiscountService {

    List<UserDiscountDTO> getAllByUserId();

    UserDiscountDTO SaveDiscountToUser(Long discountId);
}
