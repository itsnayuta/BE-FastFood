package com.example.demo.dto;

import java.util.List;

public class ComboDTO {
    private String name;
    private String description;
    private Float price;
    private Float discount;
    private String imageUrl;
    private String servingSize;
    private String type;

    private List<ProductInComboDTO> products;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProductInComboDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductInComboDTO> products) {
        this.products = products;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ComboDTO() {
    }
}
