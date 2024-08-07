package com.example.aerobankapp.converter;

public interface CombineEntityModelConverter<E, S, M>
{
    M convert(E entity1, S entity2);
}
