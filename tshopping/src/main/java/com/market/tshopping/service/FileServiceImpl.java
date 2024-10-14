package com.market.tshopping.service;

import com.market.tshopping.service.impl.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;

@Service
public class FileServiceImpl implements FileService {
    @Value("${file.rootPath}")
    private String rootPath;
    private Path root;

    private void init(){
        try {
            root= Paths.get(rootPath);
            if(Files.notExists(root)){
                Files.createDirectories(root);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Boolean saveFile(MultipartFile file) {
        try{
            init();
            Files.copy(file.getInputStream(),root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Resource loadFile(String fileName) {
        try{
            init();
            Path file=root.resolve(fileName);
            Resource resource=new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean deleteFile(String fileName) {
        try {
            init();
            Path file = root.resolve(fileName);
            if (Files.exists(file)) {
                Files.delete(file);
                return true;
            } else {
                System.out.println("File not found: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the file: " + e.getMessage());
        }
        return false;
    }
}
