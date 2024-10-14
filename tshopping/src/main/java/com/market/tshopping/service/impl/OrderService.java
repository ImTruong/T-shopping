package com.market.tshopping.service.impl;

import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.OrderCreateRequest;
import com.market.tshopping.payload.response.CheckOutResponse;

import java.util.List;

public interface OrderService {
    String createTemporaryOrder(List<ProductDTO> items);

    CheckOutResponse getTemporaryOrder(String orderId);

    Boolean deleteTemporaryOrder(String orderId);

    Boolean createOrder(OrderCreateRequest orderCreateRequest);
}
