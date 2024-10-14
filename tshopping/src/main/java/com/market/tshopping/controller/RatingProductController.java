package com.market.tshopping.controller;

import com.market.tshopping.payload.dto.RatingProductDTO;
import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.RatingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/ratings")
public class RatingProductController {

    @Autowired
    RatingProductService ratingProductService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getRatingsByProductId(@PathVariable Integer productId) {
        BaseResponse response=new BaseResponse();
        try{
            List<RatingProductDTO> ratingProductDTOS=ratingProductService.getRatingsByProductId(productId);
            response.setData(ratingProductDTOS);
        }
        catch (Exception e){
            response.setSuccess(false);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
