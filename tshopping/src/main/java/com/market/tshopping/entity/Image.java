package com.market.tshopping.entity;

import com.market.tshopping.entity.keys.IdImage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity(name = "image")
public class Image {

    @EmbeddedId
    private IdImage idImage;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    private Product product;

}
