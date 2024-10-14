package com.market.tshopping.payload.response;

import com.market.tshopping.payload.dto.ProductDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckOutResponse {
    private int totalItems;

    private double totalPrice;

    private List<ProductDTO> productList;


}
