package com.orebi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orebi.dto.LineItemDTO;
import com.orebi.service.lineitem.LineItemService;

@RestController
@RequestMapping("/api/line-items")
public class LineItemController {
    private final LineItemService lineItemService;

    public LineItemController(LineItemService lineItemService) {
        this.lineItemService = lineItemService;
    }

    @PutMapping("/updateLineItem")
    public ResponseEntity<LineItemDTO> updateLineItem(@RequestParam Long cartId, @RequestParam Long productId,
            @RequestParam int quantity) {
        LineItemDTO updatedLineItem = lineItemService.UpdateLineItem(cartId, productId, quantity);
        return ResponseEntity.ok(updatedLineItem);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLineItems(@RequestBody List<Long> lineItemIds) {
        lineItemService.deleteLineItems(lineItemIds);
        return ResponseEntity.ok("Xóa các line item thành công");
    }
}
