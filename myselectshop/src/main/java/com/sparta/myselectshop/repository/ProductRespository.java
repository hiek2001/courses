package com.sparta.myselectshop.repository;

import com.sparta.myselectshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRespository extends JpaRepository<Product, Long> {
}
