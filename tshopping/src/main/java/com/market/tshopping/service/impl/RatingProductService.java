package com.market.tshopping.service.impl;



import com.market.tshopping.payload.dto.RatingProductDTO;

import java.util.List;

public interface RatingProductService {
    List<RatingProductDTO> getRatingsByProductId(int productId);
}
