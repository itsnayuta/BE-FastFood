package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/get-all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO productDTO) {
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }
        Product product = new Product();
        product.setCategory(category);
        product.setDescription(productDTO.getDescription());
        product.setImageUrl(productDTO.getImageUrl());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setSize(productDTO.getSize());

        Product savedProduct = productService.addProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully with ID: " + id);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully with ID: " + id);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        // Lấy category theo categoryId
        Category category = categoryService.getCategoryById(productDTO.getCategoryId());
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        // Lấy sản phẩm hiện tại theo id (nếu cần)
        Product existingProduct = productService.getProductById(id);
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // Cập nhật thông tin sản phẩm
        existingProduct.setCategory(category);
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setSize(productDTO.getSize());

        Product updatedProduct = productService.updateProduct(id, existingProduct);

        return ResponseEntity.ok(updatedProduct);
    }

}
