package com.market.tshopping.repository;

import com.market.tshopping.entity.OrderProduct;
import com.market.tshopping.entity.keys.IdOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, IdOrderProduct> {
}
