package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.plaid.PlaidDataImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaidDataImporterRunner implements Runnable
{
    private PlaidDataImporter importer;

    @Autowired
    public PlaidDataImporterRunner(PlaidDataImporter importer){
        this.importer = importer;
    }

    @Override
    public void run() {

    }
}
