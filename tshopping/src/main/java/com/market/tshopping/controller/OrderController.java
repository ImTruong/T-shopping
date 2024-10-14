package com.market.tshopping.controller;

import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.OrderCreateRequest;
import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/temporary")
    public ResponseEntity<?> createTemporaryOrder(@RequestBody List<ProductDTO> productList) {
        BaseResponse baseResponse=new BaseResponse();
        try{
            baseResponse.setData(orderService.createTemporaryOrder(productList));
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/temporary/{temporaryOrderId}")
    public ResponseEntity<?> getTemporaryOrder(@PathVariable String temporaryOrderId) {
        BaseResponse baseResponse=new BaseResponse();
        try{
            baseResponse.setData(orderService.getTemporaryOrder(temporaryOrderId));
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @DeleteMapping("/temporary/{temporaryOrderId}")
    public ResponseEntity<?> deleteTemporaryOrder(@PathVariable String temporaryOrderId) {
        BaseResponse baseResponse=new BaseResponse();
        try{
            baseResponse.setSuccess(orderService.deleteTemporaryOrder(temporaryOrderId));
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> completeOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        BaseResponse baseResponse=new BaseResponse();
        try{
            orderService.createOrder(orderCreateRequest);
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }
}

