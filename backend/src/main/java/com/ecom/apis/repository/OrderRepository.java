package com.ecom.apis.repository;

import com.ecom.apis.entity.Orders;
import com.ecom.apis.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

    List<Orders> findAllByUser(UserEntity user);
}
