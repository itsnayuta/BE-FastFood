package com.example.demo.service.impl;

import com.example.demo.dto.ComboDTO;
import com.example.demo.dto.ProductInComboDTO;
import com.example.demo.entity.Combo;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductInCombo;
import com.example.demo.repository.ComboRepository;
import com.example.demo.repository.ProductInComboRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class ComboServiceImpl implements ComboService {
    @Autowired
    private ComboRepository comboRepository;


    @Autowired
    private ProductInComboRepository productInComboRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Combo> getAllCombos() {
        List<Combo> combos = new ArrayList<>();
        combos = comboRepository.findAll();
        return combos;
    }

    @Override
    public List<Combo> getAllByType(String type) {
        List<Combo> combos = new ArrayList<>();
        combos = comboRepository.findComboByType(type);
        return combos;
    }


    public Combo createComboWithProducts(ComboDTO request) {
        // 1. Tạo combo
        Combo combo = new Combo();
        combo.setName(request.getName());
        combo.setDescription(request.getDescription());
        combo.setPrice(request.getPrice());
        combo.setDiscount(request.getDiscount());
        combo.setImageUrl(request.getImageUrl());
        combo.setServingSize(request.getServingSize());
        combo.setType(request.getType());
        combo.setCreatedAt(LocalDateTime.now());
        combo.setUpdatedAt(LocalDateTime.now());

        Combo savedCombo = comboRepository.save(combo);

        // 2. Lưu danh sách ProductInCombo
        for (ProductInComboDTO item : request.getProducts()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + item.getProductId()));

            ProductInCombo pic = new ProductInCombo();
            pic.setCombo(savedCombo);
            pic.setProduct(product);
            pic.setQuantity(item.getQuantity());

            productInComboRepository.save(pic);
        }

        return savedCombo;
    }

    @Transactional
    public void deleteCombo(Long comboId) {
        // 1. Xóa tất cả ProductInCombo thuộc combo này
        productInComboRepository.deleteByComboId(comboId);

        // 2. Xóa combo
        comboRepository.deleteById(comboId);
    }
}
