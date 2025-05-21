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
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("product with "+id+" not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public Product updateProduct(Product product) {
        Optional<Product> optionalProduct = productRepository.findById(product.getId());
        Product productToUpdate = optionalProduct.orElseThrow(
                ()-> new RuntimeException("Product with id" + product.getId() + " does not exist")
        );

        updateProduct(productToUpdate, product);
        return productRepository.save(productToUpdate);


    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        List<Product> products = new ArrayList<>();
        products = productRepository.findByCategory_Id(categoryId);
        return products;
    }

    private void updateProduct(Product productToUpdate, Product product) {
        if (product.getId() != null) {
            productToUpdate.setId(product.getId());
        }
        if (product.getName() != null) {
            productToUpdate.setName(product.getName());
        }
        if (product.getDescription() != null) {
            productToUpdate.setDescription(product.getDescription());
        }
        if (product.getPrice() != null) {
            productToUpdate.setPrice(product.getPrice());
        }
        if (product.getDiscount() != null) {
            productToUpdate.setDiscount(product.getDiscount());
        }
        if (product.getImageUrl() != null) {
            productToUpdate.setImageUrl(product.getImageUrl());
        }
        if (product.getCategory() != null) {
            productToUpdate.setCategory(product.getCategory());
        }

        if (product.getSize()!=null) {
            productToUpdate.setSize(product.getSize());
        }
    }
}
