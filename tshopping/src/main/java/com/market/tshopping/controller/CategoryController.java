package com.market.tshopping.controller;

import com.market.tshopping.payload.dto.CategoryDTO;
import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getAllCategory() {
        BaseResponse baseResponse=new BaseResponse();
        try{
            List<CategoryDTO> categoryDTOList=categoryService.getAllCategory();
            baseResponse.setData(categoryDTOList);
        }
        catch (Exception e){
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }
}
