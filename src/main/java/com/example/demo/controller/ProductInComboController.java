package com.example.demo.controller;

import com.example.demo.entity.ProductInCombo;
import com.example.demo.service.ProductInComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/productincombos")
public class ProductInComboController {
    @Autowired
    private ProductInComboService productInComboService;

    @GetMapping("listAll")
    public ResponseEntity<List<ProductInCombo>> getAllProductInCombos() {
        List<ProductInCombo> productInCombos = new ArrayList<>();
        productInCombos = productInComboService.getProductInCombo();
        return new ResponseEntity<>(productInCombos, HttpStatus.OK);
    }

    @GetMapping("/searchByComboId/{id}")
    public ResponseEntity<List<ProductInCombo>> getProductInComboById(@PathVariable Long id) {
        List<ProductInCombo> productInCombos = new ArrayList<>();
                productInCombos = productInComboService.getProductInComboById(id);
        return new ResponseEntity<>(productInCombos, HttpStatus.OK);
    }
}
