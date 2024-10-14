package com.market.tshopping.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@Entity(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="role_name", nullable = false)
    private String roleName;

    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<Users> users;

}
