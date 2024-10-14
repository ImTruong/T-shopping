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
public class IdOrderProduct implements Serializable {

    @Column(name="order_id")
    private int orderId;

    @Column(name="product_id")
    private int productId;
}
