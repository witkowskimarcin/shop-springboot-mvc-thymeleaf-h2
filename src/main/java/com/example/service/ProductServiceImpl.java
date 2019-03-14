package com.example.service;

import com.example.entity.Image;
import com.example.entity.Product;
import com.example.repository.ImageRepository;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired private ProductRepository productRepository;
    @Autowired private ImageRepository imageRepository;
    @Autowired private JdbcTemplate jdbcTemplate;

    @Override
    public boolean delete(Product product) {

        try {
            jdbcTemplate.execute("DELETE FROM PRODUCTS_IMAGES WHERE PRODUCT_ID=" + product.getId());
            try {
                jdbcTemplate.execute("DELETE FROM PROMOTED_PRODUCTS WHERE PRODUCT_ID=" + product.getId());
            } catch (Exception e){

            }
            for (Image i : product.getImages()) {
                imageRepository.delete(i);
            }
            productRepository.delete(product);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteById(long id) {
        try {
            return delete(productRepository.findById(id));
        }
        catch(Exception e){
            return false;
        }
    }
}
