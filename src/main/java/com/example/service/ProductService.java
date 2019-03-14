package com.example.service;

import com.example.entity.Product;

public interface ProductService {

    boolean delete(Product product);
    boolean deleteById(long id);
}
