package com.ecommerce.productservice.util;

public class RedisKeys {

    private RedisKeys() {}

    public static String productById(Long id) {
        return "product:id:" + id;
    }

    public static String allProducts(int page, int size) {
        return "products:page:" + page + ":size:" + size;
    }
}
