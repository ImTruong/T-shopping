package com.market.tshopping.utils;

import com.market.tshopping.entity.Users;
import com.market.tshopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    @Autowired
    UserRepository userRepository;
    public Users getUserFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Users user=userRepository.findByUserName(username);
            return user;
        }
        return null;
    }
}
