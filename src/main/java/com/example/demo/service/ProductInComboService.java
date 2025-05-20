package com.example.demo.service;

import com.example.demo.entity.ProductInCombo;

import java.util.List;

public interface ProductInComboService {
    List<ProductInCombo> getProductInCombo();
    List<ProductInCombo> getProductInComboById(Long id);
}
