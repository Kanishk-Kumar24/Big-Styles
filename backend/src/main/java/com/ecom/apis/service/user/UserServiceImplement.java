package com.ecom.apis.service.user;

import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImplement implements UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public UserEntity getUserByEmail(String userEmail) throws NotFoundException {
        UserEntity user= userRepository.findByUserEmail(userEmail);
        if(user!=null) return user;
        else throw new NotFoundException("No user found with email: "+userEmail);
    }

    @Override
    public List<UserEntity> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public String addUser(UserEntity userEntity) {
            userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
            userEntity.setConfirmPassword(passwordEncoder.encode(userEntity.getConfirmPassword()));
            userEntity.setRole("USER");
            userEntity.setVerified(0);
            return saveUser(userEntity);
    }


    @Override
    public String remove(Long id) throws NotFoundException {
        if(userRepository.existsById(id)) {
            userRepository.deleteByUserId(id);
            return "User mapped to id "+id+" has been removed";
            }
        else throw new NotFoundException("user not found : " + id);
    }

    @Override
    public String adminAdd(UserEntity userEntity) {
        if(userEntity!= null && userEntity.getUserPassword()!=null && userEntity.getUserEmail()!=null
                && userEntity.getPhoneNumber()!=0 && userEntity.getAddress()!=null) {
            userEntity.setUserPassword(passwordEncoder.encode(userEntity.getUserPassword()));
            userEntity.setConfirmPassword(passwordEncoder.encode(userEntity.getConfirmPassword()));
            userEntity.setVerified(1);
            return saveUser(userEntity);
        }
        else return "check details";
    }

    @Override
    public boolean verified(String email) throws NotFoundException {
        return getUserByEmail(email).getVerified() ==1 ;
    }

    public String saveUser(UserEntity userEntity){
        if(Objects.nonNull(userRepository.save(userEntity))) return "success";
        else return  "error";
    }


    public String updatePass(String password, String email) {
        userRepository.updatePass(password,email);
        return "success";
    }
}
