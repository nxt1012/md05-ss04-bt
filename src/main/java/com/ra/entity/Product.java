package com.ra.entity;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Double productPrice;
    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;

    public Product() {
    }

    public Product(Long id, String productName, Double productPrice, Category category) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
