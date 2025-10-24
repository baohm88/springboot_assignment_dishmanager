package com.t2404e.dishmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dishes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Dish {
    @Id
    @Column(name = "id",length = 16)
    private String id;

    @Column(name = "name", columnDefinition = "VARCHAR(250)",nullable = false,length = 250)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "imageUrl", columnDefinition = "TEXT", nullable = false)
    private String imageUrl;

    @Column(name = "price", columnDefinition = "DECIMAL", nullable = false)
    private Double price;

    @CreationTimestamp
    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    @CreationTimestamp
    @Column(name = "lastModifiedDate")
    private LocalDateTime lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DishStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
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
    public LocalDateTime getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    public DishStatus getStatus() {
        return status;
    }
    public void setStatus(DishStatus status) {
        this.status = status;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
