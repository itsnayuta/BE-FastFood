package com.example.demo.service.impl;

import com.example.demo.entity.Combo;
import com.example.demo.repository.ComboRepository;
import com.example.demo.service.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ComboServiceImpl implements ComboService {
    @Autowired
    private ComboRepository comboRepository;

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
}
