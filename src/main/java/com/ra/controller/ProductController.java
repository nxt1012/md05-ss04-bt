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
@RequestMapping("/products")
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
        return saveOrUpdateProduct(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> edit(@RequestBody Product updatedProduct, @PathVariable("id") Long id) {
        updatedProduct.setId(id);
        return saveOrUpdateProduct(updatedProduct);
    }

    private ResponseEntity<Product> saveOrUpdateProduct(Product product) {
        Category category = product.getCategory();
        if (category != null && category.getId() == null) {
            Category savedCategory = categoryService.save(category);
            product.setCategory(savedCategory);
        } else if (category != null) {
            Category existingCategory = categoryService.findById(category.getId());
            if (existingCategory != null) {
                existingCategory.setCategoryName(category.getCategoryName());
                existingCategory.setCategoryStatus(category.getCategoryStatus());
                categoryService.save(existingCategory);
                product.setCategory(existingCategory);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
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
