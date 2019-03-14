package com.example.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Product;
import com.example.entity.PromotedProduct;
import com.example.repository.PromotedProductRepository;

@Service("promotedProductService")
public class PromotedProductSerivceImpl implements PromotedProductService {
	
	@Autowired
	private PromotedProductRepository promotedProductRepository;

	@Override
	public ArrayList<Product> findAllPromotedProducts (){
		ArrayList<Product> products = new ArrayList<>();
		Iterable<PromotedProduct> promotedProducts = promotedProductRepository.findAll();
		for (PromotedProduct promotedProduct : promotedProducts) {
			//System.out.println(promotedProduct.getProduct().getName());
			products.add(promotedProduct.getProduct());
		}
		return products;
	}
}
