package com.example.aerobankapp.workbench.plaid;


import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGenerator;
import com.example.aerobankapp.workbench.generator.ReferenceNumberGeneratorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.example.aerobankapp.workbench.plaid.PlaidUtil.convertPlaidSubTypeEnumListToStrings;

@Component
public class PlaidDataImporterImpl implements PlaidDataImporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaidDataImporterImpl.class);
    private final PlaidAccountImporter plaidAccountImporter;
    private final PlaidTransactionImporter plaidTransactionImporter;

    @Autowired
    public PlaidDataImporterImpl(PlaidAccountImporter plaidAccountImporter,
                                 PlaidTransactionImporter plaidTransactionImporter)
    {
      this.plaidAccountImporter = plaidAccountImporter;
      this.plaidTransactionImporter = plaidTransactionImporter;
    }

    @Override
    public List<LinkedAccountInfo> getLinkedAccountInfoList(final UserEntity user, final List<PlaidAccount> plaidAccounts) {
        return null;
    }

}
