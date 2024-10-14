package com.market.tshopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Entity(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "time_created")
    @CreationTimestamp
    private Date timeCreated;

    @Column(name = "status")
    private String status;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> orderProducts;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;


}
