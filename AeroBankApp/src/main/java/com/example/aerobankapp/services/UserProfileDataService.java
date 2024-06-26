package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.UserProfileDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileDataService implements CustomQueryService<UserProfileDTO>
{
    private EntityManager entityManager;
    private Logger LOGGER = LoggerFactory.getLogger(UserProfileDataService.class);

    @Autowired
    public UserProfileDataService(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public Optional<UserProfileDTO> runUserProfileQuery(int userID)
    {
        String jpql = "SELECT CONCAT(u.userDetails.firstName, ' ', u.userDetails.lastName), u.userDetails.email, ul.lastLogin " +
                "FROM UserEntity u " +
                "JOIN u.userLogs ul " +
                "WHERE u.userID = ?1";

        return this.executeQuery(jpql, userID);
    }

    @Override
    public Optional<UserProfileDTO> executeQuery(String jpql, Object... params)
    {
        Optional<UserProfileDTO> userProfileDTO = Optional.empty();
        try
        {
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

            for(int i = 0; i < params.length; i++)
            {
                query.setParameter(i, params[i]);
            }

            Object[] result = query.getSingleResult();
            if(result != null)
            {
                String name = (String) result[0];
                String email = (String) result[1];
                LocalDateTime lastLogin = (LocalDateTime) result[2];

                // Format the login
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a 'on' MM/dd/yyyy");
                String formattedLastLogin = lastLogin.format(dateTimeFormatter);
                LOGGER.info("Name: {}", name);
                LOGGER.info("Email: {}", email);
                LOGGER.info("Last Login: {}", lastLogin);
                userProfileDTO = Optional.of(new UserProfileDTO(name, email, formattedLastLogin));
                return userProfileDTO;
            }
        }catch(Exception e)
        {
            LOGGER.error("There was a problem executing the query", e);
            return Optional.empty();
        }
        LOGGER.info(userProfileDTO.get().toString());
       return userProfileDTO;
    }

    @Override
    public List<UserProfileDTO> executeQueryForList(String jpql, Object... params) {
        try {
            TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);

            // Set parameters
            for (int i = 0; i < params.length; i++)
            {
                query.setParameter(i + 1, params[i]);
            }

            List<Object[]> results = query.getResultList();

            return results.stream().map(result ->
            {
                String name = (String) result[0];
                String email = (String) result[1];
                LocalDateTime lastLogin = (LocalDateTime) result[2];

                // Format the lastLogin
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a 'on' MM/dd/yyyy");
                String formattedLastLogin = lastLogin.format(formatter);
                LOGGER.info("Name: {}", name);
                LOGGER.info("Email: {}", email);
                LOGGER.info("Last Login: {}", lastLogin);
                return new UserProfileDTO(name, email, formattedLastLogin);
            }).toList();
        } catch (Exception e)
        {
            LOGGER.error("There was a problem executing the query", e);
        }
        return List.of();
    }

    @Override
    public void setEntityManager(EntityManager em) {
        this.entityManager = em;
    }
}
