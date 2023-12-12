package com.ra.controller;
import com.ra.entity.Image;
import com.ra.entity.Product;
import com.ra.service.ImageService;
import com.ra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final ProductService productService;

    @Autowired
    public ImageController(ImageService imageService, ProductService productService) {
        this.imageService = imageService;
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImageById(@PathVariable("id") Long id) {
        Optional<Image> image = imageService.getImageById(id);
        return image.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Image> createImage(@RequestBody Image image) {
        Image savedImage = imageService.saveOrUpdateImage(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Image> updateImage(@RequestBody Image image, @PathVariable("id") Long id) {
        Optional<Image> existingImage = imageService.getImageById(id);
        if (existingImage.isPresent()) {
            image.setId(id);
            Image updatedImage = imageService.saveOrUpdateImage(image);
            return ResponseEntity.ok(updatedImage);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable("id") Long id) {
        Optional<Image> image = imageService.getImageById(id);
        if (image.isPresent()) {
            imageService.deleteImage(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("productId") Long productId) {
        try {
            Product product = productService.findById(productId);
            if (product != null) {
                String imageUrl = imageService.saveImage(file, product);
                if (imageUrl != null) {
                    return ResponseEntity.status(HttpStatus.CREATED).body(imageUrl);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
