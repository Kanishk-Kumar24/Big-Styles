package com.ecom.apis.repository;

import com.ecom.apis.entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Products,Long> {
    @Transactional
    void deleteBySellerId(Long sellerId);
     List<Products> findAllBySellerId(Long id);
    Products findProductsByProductId(Long id);

    boolean existsByNameAndSellerId(String name,Long sellerId);
    Products findByNameAndSellerId(String name, Long sellerId);

    @Transactional
    @Modifying
    @Query("update Products p set p.quantity =:quantity where  p.name= :name and p.sellerId=:id")
    void updateQuantity(@Param(value = "quantity") int quantity, @Param(value = "name") String name,@Param(value = "id") Long id);

}
