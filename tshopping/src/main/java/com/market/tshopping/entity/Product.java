package com.market.tshopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name="brand")
    private String brand;

    @Column(name="price", nullable = false)
    private double price;

    @Column(name="stock", nullable = false)
    private int stock;

    @OneToMany(mappedBy = "product")
    private Set<OrderProduct> orderProducts;

    @OneToMany(mappedBy = "product")
    private Set<Promo> promos;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private Users user;

    @OneToMany(mappedBy = "product")
    private Set<RatingProduct> ratingProducts;

    @OneToMany(mappedBy = "product")
    private Set<Image> images;

    @OneToMany(mappedBy = "product")
    private Set<Cart> carts;
}
