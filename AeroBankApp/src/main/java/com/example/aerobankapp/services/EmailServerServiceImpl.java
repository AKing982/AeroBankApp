package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.EmailServerEntity;
import com.example.aerobankapp.repositories.EmailServerRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void update(EmailServerEntity emailServer) {

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
}
