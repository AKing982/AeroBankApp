package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.repositories.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserDAOImpl userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAOImpl userDAO)
    {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        List<UserEntity> userEntities = userDAO.findByUserName(username);
        if(userEntities.isEmpty())
        {
            throw new UsernameNotFoundException("UserName Not Found: " + username);
        }

        UserEntity matchingUser = userEntities.stream()
                .filter(userEntity -> username.equals(userEntity.getUsername()))
                .findFirst()
                .orElseThrow();

        return User.builder()
                .username(matchingUser.getUsername())
                .password(matchingUser.getPassword())
                .build();
    }
}
