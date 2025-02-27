package com.ecom.apis.repository;

import com.ecom.apis.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    public UserEntity findByUserEmail(String userEmail);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.userPassword = :userPassword where u.userEmail = :userEmail")
    void updatePass(@Param(value = "userPassword") String userPassword, @Param(value = "userEmail") String userEmail);

    @Transactional
    void deleteByUserId(Long id);

    UserEntity findByUserId(Long id);

}
