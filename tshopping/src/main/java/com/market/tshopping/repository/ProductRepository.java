package com.market.tshopping.repository;

import com.market.tshopping.entity.Product;
import com.market.tshopping.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>, ProductRepositoryCustom {
    List<Product> getProductsByUserId(int sellerId);
    List<Product> findProductsByUserUserName(String userName);
}
