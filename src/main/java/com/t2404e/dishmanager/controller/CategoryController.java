package com.t2404e.dishmanager.controller;

import com.t2404e.dishmanager.entity.Category;
import com.t2404e.dishmanager.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Category", description = "Category endpoints")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @Operation(summary = "Get all categories with their dishes", description = "Trả về danh sách tất cả danh mục và các món ăn thuộc từng danh mục")
    @ApiResponse(responseCode = "200", description = "Danh sách danh mục và món ăn")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategoriesWithDishes() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }
}
