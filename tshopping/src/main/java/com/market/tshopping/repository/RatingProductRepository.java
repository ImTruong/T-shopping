package com.market.tshopping.repository;

import com.market.tshopping.entity.RatingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingProductRepository extends JpaRepository<RatingProduct,Integer> {
    List<RatingProduct> findByProductId(int productId);
}
