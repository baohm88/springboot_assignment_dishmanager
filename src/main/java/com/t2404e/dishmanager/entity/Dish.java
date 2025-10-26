package com.t2404e.dishmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Dish {

    @Id
    @Column(name = "id", length = 16, columnDefinition = "VARCHAR(16)")
    private String id;

    @Column(name = "name", nullable = false, length = 250, columnDefinition = "VARCHAR(250)")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double price;

    @CreationTimestamp
    @Column(name = "start_date", nullable = false, updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime startDate;

    @CreationTimestamp
    @Column(name = "last_modified_date", columnDefinition = "DATETIME")
    private LocalDateTime lastModifiedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20, columnDefinition = "VARCHAR(20)")
    private DishStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false, columnDefinition = "BIGINT")
    @JsonIgnoreProperties({"dishes", "hibernateLazyInitializer", "handler"})
    private Category category;
}
