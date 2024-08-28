package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRespository productRespository;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRespository.save(new Product(requestDto));
        return new ProductResponseDto(product);
    }
}
