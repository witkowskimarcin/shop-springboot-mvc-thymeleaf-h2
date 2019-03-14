package com.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Category;
import com.example.entity.Order;
import com.example.entity.Product;

@Repository("orderRepository")
public interface OrderRepository extends CrudRepository<Order, Long> {
	
	Order findById(long id);

}
