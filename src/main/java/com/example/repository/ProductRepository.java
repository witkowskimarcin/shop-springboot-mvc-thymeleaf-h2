package com.example.repository;

import java.util.ArrayList;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Product;
import com.example.entity.Subcategory;
import com.example.entity.User;

@Repository("productRepository")
public interface ProductRepository extends CrudRepository<Product, Long> {

	Product findById(long id);
	ArrayList<Product> findAllBySubcategory(Subcategory subcategory);
	Product findByPrice(double price);
}
