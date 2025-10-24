package com.t2404e.dishmanager.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DishRequestDTO {
    @NotBlank
    @Size(min = 8, max = 250, message = "Name must be between 8 - 250 characters long")
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be > 0")
    private Double price;
    @NotNull
    private Long categoryId;
    private String status;

    // Getters/setters
    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
