package com.example.aerobankapp.converter;

public interface EntityToModelConverter<E, M> {
    M convert(E entity);
}
