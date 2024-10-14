package com.market.tshopping.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class IdImage implements Serializable {
    @Column(name = "product_id")
    private int productId;

    @Column(name = "image_url")
    private String imageUrl;


}
