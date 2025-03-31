package com.example.demo.service.impl;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductInCombo;
import com.example.demo.repository.ProductInComboRepository;
import com.example.demo.service.ProductInComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProductInComboServiceImpl implements ProductInComboService {
    @Autowired
    ProductInComboRepository productInComboRepository;
    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Override
    public List<ProductInCombo> getProductInCombo() {
        List<ProductInCombo> products = new ArrayList<ProductInCombo>();
        products = productInComboRepository.findAll();
        return products;
    }

    @Override
    public List<ProductInCombo> getProductInComboById(Long id) {
        List<ProductInCombo> productInCombos = new ArrayList<>();
        productInCombos = productInComboRepository.findByCombo_Id(id);
        return productInCombos;
    }


}
