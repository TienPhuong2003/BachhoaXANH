package com.orebi.service.image.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orebi.Cloudinary.CloudinaryService;
import com.orebi.Cloudinary.CloudinaryUploadResponse;
import com.orebi.dto.ImageDTO;
import com.orebi.entity.Image;
import com.orebi.mapper.ImageMapper;
import com.orebi.repository.ImageRepository;
import com.orebi.service.image.ImageService;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final CloudinaryService cloudinaryService;

    public ImageServiceImpl(ImageRepository imageRepository, ImageMapper imageMapper,
            CloudinaryService cloudinaryService) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public ImageDTO uploadImage(String targetId, String targetType, MultipartFile imageFile) {
        String folderName = switch (targetType) {
            case "PRODUCT" -> "orebi/Product";
            case "CATEGORY" -> "orebi/Category";
            case "SUB_CATEGORY" -> "orebi/SubCategory";
            default -> throw new IllegalArgumentException("Invalid target type: " + targetType);
        };
        CloudinaryUploadResponse uploadResponse = cloudinaryService.uploadFile(imageFile, folderName);

        Image image = new Image();
        image.setTargetId(targetId);
        image.setTargetType(targetType);
        image.setPublicId(uploadResponse.getPublicId());
        image.setImageUrl(uploadResponse.getUrl());

        Image savedImage = imageRepository.save(image);
        return imageMapper.toDTO(savedImage);
    }

    @Override
    public List<String> getImage(String targetId, String targetType) {
        List<Image> images = imageRepository.findByTargetIdAndTargetType(targetId, targetType);
        if (images.isEmpty()) {
            throw new RuntimeException("No images found for targetId: " + targetId + " and targetType: " + targetType);
        }
        return images.stream().map(Image::getImageUrl).collect(Collectors.toList());
    }

}
