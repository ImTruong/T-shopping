package com.market.tshopping.payload.request;

import com.market.tshopping.payload.dto.AddressDTO;
import com.market.tshopping.payload.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateUserRequest {
    private Integer id;

    private String userName;

    private String phoneNumber;

    private String password;

    private String firstName;

    private String lastName;

    private Integer addressId;

    private String email;

    private Integer roleId;

    private String streetAddress;

    private String city;

    private String country;

    public UserDTO getUserDTO() {
        return new UserDTO(
                this.id,
                this.userName,
                this.phoneNumber,
                this.password,
                this.firstName,
                this.lastName,
                this.addressId,
                this.email,
                this.roleId
        );
    }

    public AddressDTO getAddressDTO() {
        return new AddressDTO(
                this.streetAddress,
                this.city,
                this.country
        );
    }
}
