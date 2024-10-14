package com.market.tshopping.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;

@Component
public class ObjectUtil {
    public boolean isEmptyObject(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value=field.get(obj);
            if (value != null && (!(value instanceof String) || StringUtils.hasText((String) value)))
                return false;
        }
        return true;
    }
}
