package com.market.tshopping.repository.custom;

import com.market.tshopping.payload.request.ProductSearchingRequest;
import com.market.tshopping.payload.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<ProductDTO> searchProductsByCriteria(ProductSearchingRequest productSearchingRequest, Pageable pageable) throws IllegalAccessException;
}
