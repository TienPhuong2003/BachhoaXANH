package com.orebi.service.image.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orebi.dto.ImageDTO;
import com.orebi.entity.Image;
import com.orebi.mapper.ImageMapper;
import com.orebi.repository.ImageRepository;
import com.orebi.service.image.ImageService;
import com.orebi.thirdparty.Cloudinary.CloudinaryService;
import com.orebi.thirdparty.Cloudinary.CloudinaryUploadResponse;

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
    public List<ImageDTO> uploadImage(String targetId, String targetType, List<MultipartFile> imageFile) {
        String folderName = switch (targetType) {
            case "PRODUCT" -> "orebi/Product";
            case "CATEGORY" -> "orebi/Category";
            case "SUB_CATEGORY" -> "orebi/SubCategory";
            case "PRODUCT_DETAIL" -> "orebi/ProductDetail";
            default -> throw new IllegalArgumentException("Invalid target type: " + targetType);
        };

        List<ImageDTO> result = new java.util.ArrayList<>();
        for (MultipartFile file : imageFile) {
            CloudinaryUploadResponse uploadResponse = cloudinaryService.uploadFile(file, folderName);

            Image image = new Image();
            image.setTargetId(targetId);
            image.setTargetType(targetType);
            image.setPublicId(uploadResponse.getPublicId());
            image.setImageUrl(uploadResponse.getUrl());

            Image savedImage = imageRepository.save(image);
            result.add(imageMapper.toDTO(savedImage));
        }
        return result;
    }

    @Override
    public List<String> getImage(String targetId, String targetType) {
        List<Image> images = imageRepository.findByTargetIdAndTargetType(targetId, targetType);
        if (images.isEmpty()) {
            throw new RuntimeException("No images found for targetId: " + targetId + " and targetType: " + targetType);
        }
        return images.stream().map(Image::getImageUrl).collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> updateImage(String targetId, String targetType, List<String> imageUrls,
            List<MultipartFile> newImageFiles) {
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                List<Image> images = imageRepository.findByTargetIdAndTargetTypeAndImageUrl(targetId, targetType,
                        imageUrl);
                for (Image image : images) {
                    cloudinaryService.deleteFile(image.getPublicId());
                    imageRepository.delete(image);
                }
            }
        }
        if (newImageFiles == null || newImageFiles.isEmpty()) {
            List<Image> remainingImages = imageRepository.findByTargetIdAndTargetType(targetId, targetType);
            return remainingImages.stream().map(imageMapper::toDTO).collect(Collectors.toList());
        }
        return uploadImage(targetId, targetType, newImageFiles);
    }
}
