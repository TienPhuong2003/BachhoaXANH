package com.orebi.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.orebi.dto.ImageDTO;

public interface ImageService {
    List<ImageDTO> uploadImage(String targetId, String targetType, List<MultipartFile> imageFile);

    List<String> getImage(String targetId, String targetType);

    List<ImageDTO> updateImage(String targetId, String targetType, List<String> imageUrls,
            List<MultipartFile> newImageFiles);
}
