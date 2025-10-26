package com.t2404e.dishmanager.service;

import com.t2404e.dishmanager.entity.Category;
import com.t2404e.dishmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        // Sort by id desc
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }
}
