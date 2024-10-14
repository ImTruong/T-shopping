package com.market.tshopping.service.impl;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    Boolean saveFile(MultipartFile file);
    Resource loadFile(String fileName);
    Boolean deleteFile(String fileName);
}
