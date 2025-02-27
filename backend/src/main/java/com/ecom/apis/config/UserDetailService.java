package com.ecom.apis.config;

import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userInfo = Optional.ofNullable(userRepository.findByUserEmail(username));
        return userInfo.map(UserDetailImpl::new)
                .orElseThrow(() -> new UsernameNotFoundException("No Such User " + username));
    }
}
