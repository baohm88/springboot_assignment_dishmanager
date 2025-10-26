package com.t2404e.dishmanager.service;

import com.t2404e.dishmanager.entity.Category;
import com.t2404e.dishmanager.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> findAll() {
        // bổ sung logic xử lý data trước khi trả về
        return categoryRepository.findAll();
    }
}
