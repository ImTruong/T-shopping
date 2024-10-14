package com.market.tshopping.service.impl;

import com.market.tshopping.payload.response.CartResponse;

public interface CartService {
    CartResponse getUserCart();

    Boolean addProductToUserCart(int productId,Integer quantity);

    Boolean deleteProductFromUserCart(int productId);
}
