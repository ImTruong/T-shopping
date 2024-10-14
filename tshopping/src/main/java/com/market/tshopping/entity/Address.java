package com.market.tshopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter

@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @OneToOne(mappedBy = "address")
    private Users user;

    @OneToMany(mappedBy = "address")
    private Set<Order> orders;
}
