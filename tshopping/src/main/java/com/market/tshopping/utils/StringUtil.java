package com.market.tshopping.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public boolean isNullOrEmpty(String str) {
        return str == null || str.equals("");
    }
}
