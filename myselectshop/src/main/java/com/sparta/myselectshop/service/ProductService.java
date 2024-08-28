package com.sparta.myselectshop.service;

import com.sparta.myselectshop.dto.ProductMyPriceRequestDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.repository.ProductRespository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRespository productRespository;

    public static final int MIN_MY_PRICE = 100;

    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productRespository.save(new Product(requestDto));
        return new ProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductMyPriceRequestDto requestDto) {
        int myprice = requestDto.getMyprice();
        if(myprice < MIN_MY_PRICE) {
            throw new IllegalArgumentException("유효하지 않은 관심 가격입니다. 최소 "+MIN_MY_PRICE+"원 이상으로 설정해주세요.");
        }

        Product product = productRespository.findById(id).orElseThrow(() ->
                new NullPointerException("해당 상품이 존재하지 않습니다.")
        );

        product.updateMyprice(requestDto);

        return new ProductResponseDto(product);
    }

    public List<ProductResponseDto> getProducts() {
        List<Product> productList = productRespository.findAll();
        List<ProductResponseDto> responseDtoList = new ArrayList<>();

        for (Product product : productList) {
            responseDtoList.add(new ProductResponseDto(product));
        }

        return responseDtoList;
    }
}
