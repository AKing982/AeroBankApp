package com.example.aerobankapp.workbench.generator;

import com.example.aerobankapp.workbench.model.AccountNumber;

public interface Generator<T>
{
    T generate();

}
