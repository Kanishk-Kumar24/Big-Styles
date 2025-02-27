package com.ecom.apis.service.user;

import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;

import java.util.List;


public interface UserServiceInterface {
    String addUser(UserEntity userEntity);


    String remove(Long id) throws NotFoundException;

    String adminAdd(UserEntity userEntity);

    boolean verified(String email) throws NotFoundException;

    UserEntity getUserByEmail(String user) throws NotFoundException;

    List<UserEntity> findAllUser();
}
