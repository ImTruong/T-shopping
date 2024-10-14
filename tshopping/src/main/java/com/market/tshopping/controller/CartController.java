package com.market.tshopping.controller;

import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@CrossOrigin
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/users")
    public ResponseEntity<?> getUserCart(){
        BaseResponse baseResponse=new BaseResponse();
        try{
            baseResponse.setData(cartService.getUserCart());
        }catch(Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addProductToUserCart(@RequestParam int productId,
                                                  @RequestParam(required = false) Integer quantity){
        BaseResponse baseResponse=new BaseResponse();
        try{
            cartService.addProductToUserCart(productId, quantity);
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{productId}")
    public ResponseEntity<?> deleteProductFromUserCart(@PathVariable int productId){
        BaseResponse baseResponse=new BaseResponse();
        try{
            cartService.deleteProductFromUserCart(productId);
        }catch (Exception e){
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }
}
