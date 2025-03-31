package com.example.demo.repository;

import com.example.demo.entity.ProductInCombo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductInComboRepository extends JpaRepository<ProductInCombo, Long> {
    List<ProductInCombo> findByCombo_Id(Long comboId);
}
