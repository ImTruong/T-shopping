package com.market.tshopping.repository;

import com.market.tshopping.entity.Image;
import com.market.tshopping.entity.Product;
import com.market.tshopping.entity.keys.IdImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ImageRepository extends JpaRepository<Image, IdImage> {
    Image findImageByProductAndDescription(Product product, String description);
    List<Image> findImagesByProductId(int productId);

    Optional<Image> findImageByProductIdAndDescription(int productId, String description);
}
