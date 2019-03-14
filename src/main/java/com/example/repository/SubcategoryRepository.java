package com.example.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Category;
import com.example.entity.Product;
import com.example.entity.Subcategory;

import antlr.collections.List;

@Repository("subcategoryRepository")
public interface SubcategoryRepository extends CrudRepository<Subcategory, Long> {
	
	Subcategory findById(long id);
	Subcategory findByName(String name);
	ArrayList<Subcategory> findAllByCategory(Category category);
}
