package com.orebi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.UserDiscountDTO;
import com.orebi.service.UserDiscount.UserDiscountService;

@RestController
@RequestMapping("/api/user-discounts")
public class UserDiscountController {

    private final UserDiscountService userDiscountService;

    public UserDiscountController(UserDiscountService userDiscountService) {
        this.userDiscountService = userDiscountService;
    }

    @GetMapping
    public ResponseEntity<List<UserDiscountDTO>> getUserDiscounts() {
        List<UserDiscountDTO> discounts = userDiscountService.getAllByUserId();
        return ResponseEntity.ok(discounts);
    }

    @PostMapping("/save")
    public ResponseEntity<UserDiscountDTO> assignDiscount(
            @RequestParam Long discountId) {
        UserDiscountDTO userDiscount = userDiscountService.SaveDiscountToUser(discountId);
        return ResponseEntity.ok(userDiscount);
    }
}
