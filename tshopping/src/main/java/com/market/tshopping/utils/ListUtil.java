package com.market.tshopping.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListUtil {

    public List<Integer> JsonIntToList(String s){
        return Arrays.stream(s.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
    public List<String> JsonStringToList(String s) {
        return Arrays.stream(s.split(","))
                .collect(Collectors.toList());
    }
    public boolean isNullOrEmpty(List<?> list) {
        return list == null || list.size()==0;
    }
}
