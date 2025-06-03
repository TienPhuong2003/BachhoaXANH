package com.orebi.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.orebi.dto.ImageDTO;

public interface ImageService {
    public ImageDTO uploadImage(String targetId, String targetType, MultipartFile imageFile);

    List<String> getImage(String targetId, String targetType);
}
