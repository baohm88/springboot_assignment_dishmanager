package com.t2404e.dishmanager.service;

import com.t2404e.dishmanager.dto.DishDTO;
import com.t2404e.dishmanager.dto.PageResponse;
import com.t2404e.dishmanager.entity.Category;
import com.t2404e.dishmanager.entity.Dish;
import com.t2404e.dishmanager.entity.DishStatus;
import com.t2404e.dishmanager.exception.BadRequestException;
import com.t2404e.dishmanager.exception.ConflictException;
import com.t2404e.dishmanager.exception.ResourceNotFoundException;
import com.t2404e.dishmanager.repository.CategoryRepository;
import com.t2404e.dishmanager.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private String nextDishId() {
        long count = dishRepository.count() + 1;
        return String.format("MN%03d", count);
    }

    public PageResponse<Dish> listDishes(
            Integer page, Integer limit, String sortBy, String sortDir,
            String status, String keyword, Long categoryId,
            Double minPrice, Double maxPrice
    ) {
        int p = (page == null || page < 1) ? 1 : page;
        int l = (limit == null || limit < 1) ? 5 : limit;
        String sortField = (sortBy == null || sortBy.isBlank()) ? "startDate" : sortBy;
        Sort.Direction dir = ("asc".equalsIgnoreCase(sortDir)) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Sort sort = Sort.by(dir, sortField);
        Pageable pageable = PageRequest.of(p - 1, l, sort);

        Specification<Dish> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Exclude DELETED always for list
            predicates.add(cb.notEqual(root.get("status"), DishStatus.DELETED));

            // Status filter (default ON_SALE)
            if (status == null || status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), DishStatus.ON_SALE));
            } else {
                try {
                    DishStatus st = DishStatus.valueOf(status.toUpperCase(Locale.ROOT));
                    predicates.add(cb.equal(root.get("status"), st));
                } catch (IllegalArgumentException ex) {
                    throw new BadRequestException("Invalid status: " + status);
                }
            }

            if (keyword != null && !keyword.isBlank()) {
                Predicate byName = cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
                Predicate byDesc = cb.like(cb.lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
                predicates.add(cb.or(byName, byDesc));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Dish> pageData = dishRepository.findAll(spec, pageable);
        return new PageResponse<>(pageData.getContent(), p, l, pageData.getTotalPages(), (int) pageData.getTotalElements());
    }

    public Dish getDishById(String id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + id));
    }

    public Dish createDish(DishDTO dto) {
        // Validate Category
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found: " + dto.getCategoryId()));

        Dish dish = new Dish();
        dish.setId(nextDishId());
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setImageUrl(dto.getImageUrl());
        dish.setPrice(dto.getPrice());
        dish.setCategory(category);
        dish.setStatus(DishStatus.ON_SALE); // default
        return dishRepository.save(dish);
    }

    public Dish updateDishById(String id, DishDTO dto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + id));

        if (dish.getStatus() == DishStatus.DELETED) {
            throw new ConflictException("Cannot update a deleted dish");
        }

        // Validate Category
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found: " + dto.getCategoryId()));

        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setImageUrl(dto.getImageUrl());
        dish.setPrice(dto.getPrice());
        dish.setCategory(category);

        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            DishStatus newStatus;
            try {
                newStatus = DishStatus.valueOf(dto.getStatus().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Invalid status: " + dto.getStatus());
            }
            if (newStatus == DishStatus.DELETED) {
                throw new BadRequestException("Use DELETE endpoint to soft-delete a dish");
            }
            dish.setStatus(newStatus);
        }

        return dishRepository.save(dish);
    }

    public void softDelete(String id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found: " + id));

        if (dish.getStatus() == DishStatus.DELETED) {
            throw new ConflictException("Dish already deleted");
        }
        dish.setStatus(DishStatus.DELETED);
        dishRepository.save(dish);
    }
}
