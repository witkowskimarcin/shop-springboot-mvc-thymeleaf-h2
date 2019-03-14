package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Product;
import com.example.entity.PromotedProduct;
import com.example.entity.Subcategory;
import com.example.entity.User;

@Repository("promotedProductRepository")
public interface PromotedProductRepository extends CrudRepository<PromotedProduct, Long> {

	PromotedProduct findById(long id);
}
