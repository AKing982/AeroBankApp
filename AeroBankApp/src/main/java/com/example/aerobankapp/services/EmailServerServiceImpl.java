package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.repositories.EmailServerRepository;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class EmailServerServiceImpl implements EmailServerService
{
    private final EmailServerRepository emailServerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EmailServerServiceImpl(EmailServerRepository emailServerRepository, EntityManager entityManager)
    {
        this.emailServerRepository = emailServerRepository;
        this.entityManager = entityManager;
    }

    @Override
    public List<EmailServerEntity> findAll() {
        return getEmailServerRepository().findAll();
    }

    @Override
    public void save(EmailServerEntity obj) {
        getEmailServerRepository().save(obj);
    }

    @Override
    public void delete(EmailServerEntity obj) {
        getEmailServerRepository().delete(obj);
    }

    @Override
    @Transactional
    public void update(EmailServerEntity emailServer) {
       if(emailServer == null || emailServer.getId() == null)
       {
           throw new IllegalArgumentException("Email server or its ID must not be null");
       }

       EmailServerEntity existingEmailServer = getEmailServerRepository().findById(emailServer.getId())
               .orElseThrow(() -> new EntityNotFoundException("Email Server not found with id: " + emailServer.getId()));

       existingEmailServer.setHost(emailServer.getHost());
       existingEmailServer.setPort(emailServer.getPort());
       existingEmailServer.setPassword(emailServer.getPassword());
       existingEmailServer.setUsername(emailServer.getUsername());

       getEmailServerRepository().save(existingEmailServer);
    }

    @Override
    public Optional<EmailServerEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<EmailServerEntity> getEmailServerById(Long id)
    {
        EntityManager manager = getEntityManager();
        TypedQuery<EmailServerEntity> typedQuery = manager.createQuery("FROM EmailServerEntity WHERE id =:id", EmailServerEntity.class);
        typedQuery.setParameter("id", id);
        typedQuery.setMaxResults(2);

       return typedQuery.getResultList();
    }

    @Override
    public boolean emailServerExists(String host, int port) {
        EntityManager manager = getEntityManager();
        TypedQuery<Long> query = manager.createQuery(
                "SELECT COUNT(e) FROM EmailServerEntity e WHERE e.host = :host AND e.port = :port AND e.id = 1",
                Long.class
        );
        query.setParameter("host", host);
        query.setParameter("port", port);

        Long count = query.getSingleResult();
        return count > 0;
    }
}
