package com.ecom.apis.repository;

import com.ecom.apis.entity.ProductRating;
import com.ecom.apis.entity.Products;
import com.ecom.apis.entity.UserEntity;
import org.hibernate.dialect.function.AvgFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

@Repository
public interface RatingRepository extends JpaRepository<ProductRating,Long> {

    boolean existsByUser(UserEntity userEntity);

    ProductRating findByUser(UserEntity userEntity);

    @Query("select avg(p.rating) from ProductRating p where p.products= :product")
    Double averageRating(@PathVariable("product") Products product);
}
