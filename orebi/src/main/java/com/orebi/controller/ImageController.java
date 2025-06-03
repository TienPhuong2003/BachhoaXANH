package com.orebi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orebi.dto.ImageDTO;
import com.orebi.service.image.ImageService;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ImageDTO> uploadImage(@RequestParam String targetId,
            @RequestParam String targetType,
            @RequestParam("file") MultipartFile imageFile) {

        ImageDTO uploadImage = imageService.uploadImage(targetId, targetType, imageFile);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping("/getImage")
    public ResponseEntity<List<String>> getImage(@RequestParam String targetId, @RequestParam String targetType) {
        List<String> imageUrl = imageService.getImage(targetId, targetType);
        return ResponseEntity.ok(imageUrl);
    }
}
