package com.market.tshopping.service;

import com.market.tshopping.entity.RatingProduct;
import com.market.tshopping.payload.dto.RatingProductDTO;
import com.market.tshopping.repository.RatingProductRepository;
import com.market.tshopping.service.impl.RatingProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RatingProductServiceImpl implements RatingProductService {
    @Autowired
    RatingProductRepository ratingProductRepository;

    @Autowired
    ModelMapper modelMapper;
    @Override
    public List<RatingProductDTO> getRatingsByProductId(int productId) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        List<RatingProduct> ratingProducts=ratingProductRepository.findByProductId(productId);
        List<RatingProductDTO> ratingProductDTOS=ratingProducts.stream().map(ratingProduct -> {
                RatingProductDTO dto =
                        modelMapper.map(ratingProduct, RatingProductDTO.class);
                dto.setUserFullName(ratingProduct.getUser().getFirstName()+" "+ratingProduct.getUser().getLastName());
                String formattedDate = formatter.format(ratingProduct.getTimeCreated());
                dto.setTimeCreated(formattedDate);
            return dto;
            }).collect(Collectors.toList());
        return ratingProductDTOS;
    }


}
