package com.market.tshopping.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private String imageUrl;
    private int productId;
    private String description;
    private MultipartFile file;

    public ImageDTO(String imageUrl, int productId, String description) {
        this.imageUrl = imageUrl;
        this.productId = productId;
        this.description = description;
    }
    public ImageDTO(String imageUrl, int productId) {
        this.imageUrl = imageUrl;
        this.productId = productId;
    }
}
