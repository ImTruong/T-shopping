package com.market.tshopping.service;

import com.market.tshopping.entity.Category;
import com.market.tshopping.payload.dto.CategoryDTO;
import com.market.tshopping.repository.CategoryRepository;
import com.market.tshopping.service.impl.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<CategoryDTO> getAllCategory() {
        List<Category> categoryList=categoryRepository.findAll();
        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());
        return categoryDTOList;
    }
}
