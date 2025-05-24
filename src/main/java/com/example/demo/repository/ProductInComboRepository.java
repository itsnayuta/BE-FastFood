package com.example.demo.repository;

import com.example.demo.entity.ProductInCombo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductInComboRepository extends JpaRepository<ProductInCombo, Long> {
    List<ProductInCombo> findByCombo_Id(Long comboId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM product_in_combo WHERE combo_id = :comboId", nativeQuery = true)
    void deleteByComboId(Long comboId);
}
