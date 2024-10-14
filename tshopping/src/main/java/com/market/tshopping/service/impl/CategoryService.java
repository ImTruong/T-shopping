package com.market.tshopping.service.impl;



import com.market.tshopping.entity.Category;
import com.market.tshopping.payload.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategory();
}
