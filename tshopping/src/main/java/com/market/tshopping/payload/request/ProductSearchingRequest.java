package com.market.tshopping.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ProductSearchingRequest {
    private String productName;

    private List<Integer> category;

    private List<String> brand;

    private Integer minPrice;

    private Integer maxPrice;

    private Integer star;

    public ProductSearchingRequest(String productName, List<Integer> category, List<String> brand, Integer minPrice, Integer maxPrice, Integer star) {
        this.productName = productName;
        this.category = category;
        this.brand = brand;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.star = star;
    }
}
