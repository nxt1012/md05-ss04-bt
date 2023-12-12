package com.ra.service;

import com.ra.entity.Image;
import com.ra.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ImageService {
    List<Image> getAllImages();

    Optional<Image> getImageById(Long id);

    Image saveOrUpdateImage(Image Image);

    void deleteImage(Long id);

    String saveImage(MultipartFile file, Product product) throws IOException;
}
