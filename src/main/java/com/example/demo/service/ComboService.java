package com.example.demo.service;

import com.example.demo.entity.Combo;

import java.util.List;

public interface ComboService {
    List<Combo> getAllCombos();
    List<Combo> getAllByType(String type);
}
