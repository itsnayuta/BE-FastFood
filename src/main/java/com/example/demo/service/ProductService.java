package com.example.demo.service;

import com.example.demo.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(Long id);
    void deleteProductById(Long id);
    List<Product> findByCategoryId(Long categoryId);
    Product updateProduct(Long id, Product product);
    Product addProduct(Product product);
}
