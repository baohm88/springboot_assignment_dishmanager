package com.t2404e.dishmanager.repository;

import com.t2404e.dishmanager.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
