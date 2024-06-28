package com.example.aerobankapp.converter;

public interface ModelConverter<D, M>
{
    D convert(M model);
}
