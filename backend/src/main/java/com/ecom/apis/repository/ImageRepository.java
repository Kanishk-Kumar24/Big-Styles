package com.ecom.apis.repository;

import com.ecom.apis.entity.ImageData;
import com.ecom.apis.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageData,Long> {
    @Query("select i from ImageData i where i.products.productId = :id ")
    List<ImageData> findImages(@PathVariable("id") Long id);
}
