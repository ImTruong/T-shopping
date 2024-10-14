package com.market.tshopping.service.impl;


import com.market.tshopping.payload.dto.ImageDTO;
import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.ProductSearchingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ProductDTO getProductById(int productId);
    List<ProductDTO> getAllProduct();
    List<String> getAllProductBrand();
    Page<ProductDTO> searchProduct(Map<String, String> allParams,String category,String brand, Pageable pageable);
    List<ProductDTO> getSellerProducts();
    Boolean createOrUpdateProduct(ProductDTO productDTO,Integer sellerId);
    List<ImageDTO> getProductImages(int productId);
    Boolean addProductImage(MultipartFile file,Integer productId,String description);
    Boolean deleteProductImage(ImageDTO imageDTO);
    Boolean changeProductRepresentImage(MultipartFile file,int productId);

}
