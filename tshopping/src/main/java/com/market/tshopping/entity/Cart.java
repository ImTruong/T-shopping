package com.market.tshopping.entity;

import com.market.tshopping.entity.keys.IdCart;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name="cart")
public class Cart {
    @EmbeddedId
    private IdCart idCart;

    @Column(name="quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "product_id",insertable = false,updatable = false)
    private Product product;

    public Cart(IdCart idCart, int quantity) {
        this.idCart = idCart;
        this.quantity = quantity;
    }
}
