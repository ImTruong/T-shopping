package com.market.tshopping.repository;

import com.market.tshopping.entity.Cart;
import com.market.tshopping.entity.keys.IdCart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, IdCart> {
    List<Cart> findCartsByUserUserName(String userName);
}
