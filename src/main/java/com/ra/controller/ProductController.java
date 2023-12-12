package com.ra.controller;
import com.ra.entity.Category;
import com.ra.entity.Product;
import com.ra.service.CategoryService;
import com.ra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> list = productService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Category category = product.getCategory();
        if (category != null) {
            Category savedCategory = categoryService.save(category);
            product.setCategory(savedCategory);
        }

        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> edit(@RequestBody Product updatedProduct, @PathVariable("id") Long id) {
        Product existingProduct = productService.findById(id);
        if (existingProduct != null) {
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductPrice(updatedProduct.getProductPrice());

            Category updatedCategory = updatedProduct.getCategory();
            if (updatedCategory != null) {
                Category existingCategory = existingProduct.getCategory();
                if (existingCategory != null) {
                    existingCategory.setCategoryName(updatedCategory.getCategoryName());
                    existingCategory.setCategoryStatus(updatedCategory.getCategoryStatus());
                } else {
                    existingProduct.setCategory(updatedCategory);
                }
            }

            Product savedProduct = productService.save(existingProduct);
            return ResponseEntity.ok(savedProduct);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        if (product != null) {
            productService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
