package com.market.tshopping.repository;

import com.market.tshopping.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users,Integer> {
    Users findByUserName(String userName);
}
