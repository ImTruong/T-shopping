package com.market.tshopping.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateRequest {
    String temporaryOrderId;

    private Integer addressId;

    private String streetAddress;

    private String city;

    private String country;

    private String firstName;

    private String lastName;

    private String phoneNumber;

}
