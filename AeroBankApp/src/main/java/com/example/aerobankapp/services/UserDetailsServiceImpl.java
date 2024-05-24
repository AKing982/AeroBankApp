package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Getter
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserServiceImpl userDAO;
    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(UserServiceImpl userDAO)
    {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        List<UserEntity> userEntities = getUserDAO().findByUserName(username);
        if(userEntities.isEmpty())
        {
            LOGGER.debug("UserEntity List is Empty");
            throw new UsernameNotFoundException("UserName Not Found: " + username);
        }

        UserEntity matchingUser = userEntities.stream()
                .filter(userEntity -> username.equals(userEntity.getUserCredentials().getUsername()))
                .findFirst().orElseThrow(() -> new NoSuchElementException("User Not Found"));

        return User.builder()
                .username(matchingUser.getUserCredentials().getUsername())
                .password(matchingUser.getUserCredentials().getPassword())
                .roles(String.valueOf(matchingUser.getUserSecurity().getRole()))
                .build();
    }
}
