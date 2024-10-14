package com.market.tshopping.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private Integer id;

    private String productName;

    private String description;

    private String brand;

    private Double price;

    private Integer stock;

    private String categoryName;

    private String sellerName;

    private Double rating;

    private List<String> images;

    private Integer quantity;

    public ProductDTO(Integer id, String productName, String description, String brand, Double price, Integer stock, String categoryName) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
    }

    public ProductDTO(Integer id, String productName, String description, String brand, Double price, Integer stock, String categoryName, String sellerName, Double rating, String image) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.categoryName = categoryName;
        this.sellerName = sellerName;
        this.rating = rating;
        this.images = Collections.singletonList(image);
    }
}
