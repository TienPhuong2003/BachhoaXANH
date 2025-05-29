package com.orebi.service.image;

import org.springframework.web.multipart.MultipartFile;

import com.orebi.dto.ImageDTO;

public interface ImageService  {
    public ImageDTO uploadImage(String targetId, String targetType, MultipartFile imageFile);
    public String getImage(String targetId, String targetType);
}
