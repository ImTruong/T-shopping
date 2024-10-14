package com.market.tshopping.controller;

import com.market.tshopping.payload.dto.CategoryDTO;
import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@CrossOrigin
public class AddressController {

    @Autowired
    AddressService addressService;
    @GetMapping("/{addressId}")
    public ResponseEntity<?> getAddress(@PathVariable int addressId) {
        BaseResponse baseResponse=new BaseResponse();
        try{
            baseResponse.setData(addressService.getAddressById(addressId));
        }
        catch (Exception e){
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
