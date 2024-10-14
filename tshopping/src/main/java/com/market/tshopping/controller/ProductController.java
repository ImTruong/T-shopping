package com.market.tshopping.controller;

import com.market.tshopping.payload.dto.ImageDTO;
import com.market.tshopping.payload.dto.ProductDTO;
import com.market.tshopping.payload.request.ProductCreateRequest;
import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.repository.ProductRepository;
import com.market.tshopping.service.impl.FileService;
import com.market.tshopping.service.impl.ProductService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/seller")
    public ResponseEntity<?> getSellerProduct(){
        BaseResponse baseResponse=new BaseResponse();
        try{
            List<ProductDTO> productDTOList=productService.getSellerProducts();
            baseResponse.setData(productDTOList);
        }catch(Exception e){
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable int productId){
        BaseResponse baseResponse=new BaseResponse();
        ProductDTO productDTO=productService.getProductById(productId);
        baseResponse.setData(productDTO);
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @GetMapping("/brand")
    public ResponseEntity<?> getAllBrand(){
        BaseResponse baseResponse=new BaseResponse();
        try{
            List<String> brandList=productService.getAllProductBrand();
            baseResponse.setData(brandList);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam Map<String, String> allParams,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            Pageable pageable) {
        BaseResponse baseResponse=new BaseResponse();
        try{
            Page<ProductDTO> productDTOPage=productService.searchProduct(allParams,category,brand,pageable);
            baseResponse.setData(productDTOPage);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @GetMapping("/images/{productId}")
    public ResponseEntity<?> getProductImage(@PathVariable int productId){
        BaseResponse baseResponse=new BaseResponse();
        try {
            List<ImageDTO> imageDTOS=productService.getProductImages(productId);
            baseResponse.setData(imageDTOS);
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateProductDetail(@RequestBody ProductDTO updateRequest){
        BaseResponse baseResponse=new BaseResponse();
        try{
            productService.createOrUpdateProduct(updateRequest,null);
        }catch (Exception e){
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @PostMapping("/images")
    public ResponseEntity<?> addProductImage(@RequestParam MultipartFile file,
                                             @RequestParam String description,
                                             @RequestParam Integer productId){
        BaseResponse baseResponse=new BaseResponse();
        try{
            productService.addProductImage(file,productId,description);
        }catch (Exception e){
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @DeleteMapping("/images")
    public ResponseEntity<?> deleteProductImage(@RequestBody ImageDTO imageDTO){
        BaseResponse baseResponse=new BaseResponse();
        baseResponse.setSuccess(productService.deleteProductImage(imageDTO));
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @PutMapping("/images")
    public ResponseEntity<?> changeProductRepresentImage(@RequestParam MultipartFile file,
                                                         @RequestParam Integer productId){
        BaseResponse baseResponse=new BaseResponse();
        baseResponse.setSuccess(productService.changeProductRepresentImage(file,productId));
        return new ResponseEntity<>(baseResponse,HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateRequest productCreateRequest){
        BaseResponse baseResponse=new BaseResponse();
        try{
            productService.createOrUpdateProduct(productCreateRequest.getProductDTO(),productCreateRequest.getSellerId());
        }catch (Exception e){
            System.out.println(e);
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.CREATED);
    }
}
