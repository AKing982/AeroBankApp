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

@Service
@Getter
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserDAOImpl userDAO;
    private Logger LOGGER = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Autowired
    public UserDetailsServiceImpl(UserDAOImpl userDAO)
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
                .filter(userEntity -> username.equals(userEntity.getUsername()))
                .findFirst().orElse(null);

        return User.builder()
                .username(matchingUser.getUsername())
                .password(matchingUser.getPassword())
                .roles(String.valueOf(matchingUser.getRole()))
                .build();
    }
}
