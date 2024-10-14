package com.market.tshopping.payload.request;

import com.market.tshopping.payload.dto.ProductDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateRequest {
    private Integer id;

    private String productName;

    private String description;

    private String brand;

    private Double price;

    private Integer stock;

    private String categoryName;

    private Integer sellerId;

    public ProductDTO getProductDTO(){
        return new ProductDTO(id,productName,description,brand,price,stock,categoryName);
    }
}
