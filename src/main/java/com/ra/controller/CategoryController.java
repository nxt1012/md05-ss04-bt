package com.ra.controller;

import com.ra.entity.Category;
import com.ra.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> list = categoryService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable("id") Long id) {
        Category category = categoryService.findById(id);
        return category != null ? ResponseEntity.ok(category) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category saveResult = categoryService.save(category);
        return saveResult != null ? ResponseEntity.status(HttpStatus.CREATED).body(saveResult)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> edit(@RequestBody Category category, @PathVariable("id") Long id) {
        if (categoryService.findById(id) != null) {
            return ResponseEntity.ok(categoryService.save(category));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        Category category = categoryService.findById(id);
        if (category != null) {
            categoryService.delete(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
