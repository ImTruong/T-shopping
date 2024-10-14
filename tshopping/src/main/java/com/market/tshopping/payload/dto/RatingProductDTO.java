package com.market.tshopping.payload.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RatingProductDTO {
    private int id;
    private int star;
    private String desciption;
    private String timeCreated;
    private String userFullName;
}
