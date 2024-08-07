package com.example.aerobankapp.services.builder;

public interface EntityBuilder<E, M>
{
    E createEntity(M model);
}
