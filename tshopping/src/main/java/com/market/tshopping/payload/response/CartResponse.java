package com.market.tshopping.payload.response;


import com.market.tshopping.payload.dto.ProductDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor

public class CartResponse {
    private int totalItems;

    private double totalPrice;

    private List<ProductDTO> productList;

    public CartResponse(int totalItems, double totalPrice) {
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
    }
}
