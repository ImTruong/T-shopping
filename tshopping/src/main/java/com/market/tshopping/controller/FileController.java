package com.market.tshopping.controller;


import com.market.tshopping.payload.response.BaseResponse;
import com.market.tshopping.service.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin
@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FileService fileService;

    @PostMapping
    public ResponseEntity<?> saveFile(@RequestBody MultipartFile file){
        BaseResponse baseResponse=new BaseResponse();
        boolean isSucces= fileService.saveFile(file);
        baseResponse.setSuccess(isSucces);
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<?> getFile(@PathVariable String fileName){
        Resource resource=fileService.loadFile(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }
}
