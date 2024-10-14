package com.market.tshopping.service.impl;

import com.market.tshopping.payload.dto.AddressDTO;
import com.market.tshopping.payload.dto.UserDTO;

public interface UserService {
    boolean createOrUpdateUser(UserDTO userDTO);
    UserDTO getCurrentUser();

    boolean preUpdateUser(UserDTO userDTO, AddressDTO addressDTO);
}
