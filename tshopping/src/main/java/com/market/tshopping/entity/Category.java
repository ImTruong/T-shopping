package com.market.tshopping.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name="description")
    private String description;

    @Column(name="parent_id")
    private Integer parentId;

    @OneToMany(mappedBy = "category")
    private Set<Product> products;


}
