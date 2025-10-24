package com.t2404e.dishmanager.repository;

import com.t2404e.dishmanager.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DishRepository extends JpaRepository<Dish, Long>,
        JpaSpecificationExecutor<Dish> {
}
