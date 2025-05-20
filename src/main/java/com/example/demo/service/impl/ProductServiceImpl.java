package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        products = productRepository.findAll();
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        Product product = new Product();
        product = productRepository.findById(id).get();
        return product;
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void updateProduct(Long id, Product product) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product1 = optionalProduct.get();
    }

    @Override
    public Product addProduct(Product product) {
        return null;
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        List<Product> products = new ArrayList<>();
        products = productRepository.findByCategory_Id(categoryId);
        return products;
    }
}
