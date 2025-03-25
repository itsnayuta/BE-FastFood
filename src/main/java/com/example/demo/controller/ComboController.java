package com.example.demo.controller;

import com.example.demo.entity.Combo;
import com.example.demo.entity.Product;
import com.example.demo.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/combos")
public class ComboController {

    @Autowired
    ComboService comboService;

    @GetMapping("/listAll")
    public ResponseEntity<List<Combo>> getAllProducts() {
        List<Combo> combos = comboService.getAllCombos();
        return new ResponseEntity<>(combos, HttpStatus.OK);
    }
}
