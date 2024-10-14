package com.market.tshopping.controller;

import com.market.tshopping.payload.dto.UserDTO;
import com.market.tshopping.payload.request.UpdateUserRequest;
import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.UserService;
import com.market.tshopping.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @GetMapping
    public ResponseEntity<?> getUser(){
        BaseResponse baseResponse=new BaseResponse();
        UserDTO userDTO=userService.getCurrentUser();
        if(userDTO!=null) baseResponse.setData(userDTO);
        else{
            baseResponse.setSuccess(false);
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createNewUser(@RequestBody UserDTO registerRequest){
        BaseResponse baseResponse=new BaseResponse();
        baseResponse.setSuccess(userService.createOrUpdateUser(registerRequest));
        return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateRequest){
        BaseResponse baseResponse=new BaseResponse();
        baseResponse.setSuccess(userService.preUpdateUser(updateRequest.getUserDTO(),updateRequest.getAddressDTO()));
        return new ResponseEntity<>(baseResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String userName,@RequestParam String password){
        BaseResponse baseResponse=new BaseResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, password));

            String token = jwtUtil.generateToken(userName);
            baseResponse.setData("Bearer " + token);
        } catch (AuthenticationException e) {
            baseResponse.setSuccess(false);
            baseResponse.setData("Invalid username or password");
        }
        return new ResponseEntity<>(baseResponse,HttpStatus.ACCEPTED);
    }
}
