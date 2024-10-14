package com.market.tshopping.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse {
    private String desc;
    private Object data;
    private boolean isSuccess=true;
}
