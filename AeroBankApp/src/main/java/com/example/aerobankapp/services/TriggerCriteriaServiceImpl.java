package com.example.aerobankapp.services;

import com.example.aerobankapp.entity.TriggerCriteriaEntity;
import com.example.aerobankapp.repositories.TriggerCriteriaRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class TriggerCriteriaServiceImpl implements TriggerCriteriaService
{

    private final TriggerCriteriaRepository triggerCriteriaRepository;

    @Autowired
    public TriggerCriteriaServiceImpl(@NotNull TriggerCriteriaRepository triggerCriteriaRepository)
    {
        this.triggerCriteriaRepository = triggerCriteriaRepository;
    }

    @Override
    public List<TriggerCriteriaEntity> findAll() {
        return getTriggerCriteriaRepository().findAll();
    }

    @Override
    public void save(TriggerCriteriaEntity obj) {
        getTriggerCriteriaRepository().save(obj);
    }

    @Override
    public void delete(TriggerCriteriaEntity obj) {
        getTriggerCriteriaRepository().delete(obj);
    }

    @Override
    public Optional<TriggerCriteriaEntity> findAllById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<TriggerCriteriaEntity> findByUserName(String user) {
        // NOT USED
        return null;
    }
}
