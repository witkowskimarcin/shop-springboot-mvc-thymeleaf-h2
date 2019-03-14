package com.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Category;
import com.example.entity.Product;

@Repository("categoryRepository")
public interface CategoryRepository extends CrudRepository<Category, Long> {
	
	Category findById(long id);
	Category findByName(String name);

}
