package com.market.tshopping.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;

    private String userName;

    private String phoneNumber;

    private String password;

    private String firstName;

    private String lastName;

    private Integer addressId;

    private String email;

    private Integer roleId;


}
