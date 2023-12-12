package com.ra.service;

import com.ra.entity.Image;
import com.ra.entity.Product;
import com.ra.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {
    @Value("${upload.directory}")
    private String uploadDir;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image saveOrUpdateImage(Image Image) {
        return imageRepository.save(Image);
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public String saveImage(MultipartFile file, Product product) throws IOException {
        String imageUrl = saveImageToStorage(file);

        Image image = new Image();
        image.setImageUrl(imageUrl);
        image.setProduct(product);

        imageRepository.save(image);

        return imageUrl;
    }

    private String saveImageToStorage(MultipartFile file) throws IOException {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        String filePath = uploadDir + File.separator + fileName;

        Path path = Paths.get(filePath);
        Files.copy(file.getInputStream(), path);

        return uploadDir + fileName;
    }

    private String generateUniqueFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String uniqueID = UUID.randomUUID().toString();

        return uniqueID + extension;
    }
}
