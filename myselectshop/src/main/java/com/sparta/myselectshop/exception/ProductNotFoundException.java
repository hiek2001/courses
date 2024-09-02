package com.sparta.myselectshop.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message); // RuntimeException쪽으로 던짐
    }
}
