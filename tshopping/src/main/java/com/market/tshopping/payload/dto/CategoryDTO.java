package com.market.tshopping.payload.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    private int id;
    private String categoryName;
    private String description;
    private Integer parentId;
}
