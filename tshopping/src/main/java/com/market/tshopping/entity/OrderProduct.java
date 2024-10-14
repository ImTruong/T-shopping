package com.market.tshopping.entity;

import com.market.tshopping.entity.keys.IdOrderProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name="order_product")
public class OrderProduct {

    @EmbeddedId
    private IdOrderProduct idOrderProduct;

    @Column(name="quantity")
    private long quantity;

    @ManyToOne
    @JoinColumn(name="order_id",insertable = false,updatable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_id",insertable = false,updatable = false)
    private Product product;

}
