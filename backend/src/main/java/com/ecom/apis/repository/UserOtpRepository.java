package com.ecom.apis.repository;

import com.ecom.apis.entity.UserOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp,Long> {
    public UserOtp findUserOtpByEmail(String email);
}
