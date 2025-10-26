package com.t2404e.dishmanager.controller;

import com.t2404e.dishmanager.dto.DishDTO;
import com.t2404e.dishmanager.dto.PageResponse;
import com.t2404e.dishmanager.entity.Dish;
import com.t2404e.dishmanager.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Dish", description = "Dish endpoints")
@RestController
@RequestMapping("/api/v1/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Operation(summary = "List dishes with pagination, sorting, filtering")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PageResponse<Dish>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer limit,
            @RequestParam(defaultValue = "startDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false, defaultValue = "ON_SALE") String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        return ResponseEntity.ok(
                dishService.listDishes(page, limit, sortBy, sortDir, status, keyword, categoryId, minPrice, maxPrice)
        );
    }

    @Operation(summary = "Get dish by id")
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
//    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable String id) {
        return ResponseEntity.ok(dishService.getDish(id));
    }

    @Operation(summary = "Create new dish (status=ON_SALE, startDate auto)")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Dish> create(@Valid @RequestBody DishDTO dto) {
        Dish created = dishService.createDish(dto);
        return ResponseEntity.status(201).body(created);
    }

    @Operation(summary = "Update dish by id (cannot change to DELETED here)")
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
//    @PutMapping("/{id}")
    public ResponseEntity<Dish> update(@PathVariable String id, @Valid @RequestBody DishDTO dto) {
        return ResponseEntity.ok(dishService.updateDish(id, dto));
    }

    @Operation(summary = "Soft delete dish by id (status=DELETED)")
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
//    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        dishService.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}
