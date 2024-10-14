package com.market.tshopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity(name="promo")
public class Promo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(name="discount_percentage", nullable = false)
    private double discountPercentage;

    @Column(name="start_date", nullable = false)
    private Date startDate;

    @Column(name="end_date", nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
}
