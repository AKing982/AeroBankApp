package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.*;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import com.example.aerobankapp.workbench.AccountBuilder;
import com.example.aerobankapp.workbench.utilities.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class PlaidAccountImporterImplTest {

    @InjectMocks
    private PlaidAccountImporterImpl importer;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountBuilder accountBuilder;

    @Mock
    private AccountCodeService accountCodeService;

    @Mock
    private UserService userService;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @Mock
    private PlaidLinkService plaidLinkService;


    @BeforeEach
    void setUp() {
        importer = new PlaidAccountImporterImpl(externalAccountsService,plaidLinkService, accountService, accountBuilder, accountCodeService, userService);
    }

    @Test
    @DisplayName("Test linkAccounts when PlaidAccount is null, then throw exception")
    public void testLinkAccounts_whenPlaidAccountIsNull_thenThrowException(){
        AccountEntity mockAccount = mock(AccountEntity.class);
        assertThrows(PlaidAccountNotFoundException.class, () -> {
            importer.linkAccounts(null, mockAccount);
        });
    }

    @Test
    @DisplayName("Test linkAccounts when AccountEntity is null, then throw exception")
    public void testLinkAccounts_whenAccountEntityIsNull_thenThrowException(){
        PlaidAccount mockPlaidAccount = mock(PlaidAccount.class);
        assertThrows(AccountNotFoundException.class, () -> {
            importer.linkAccounts(mockPlaidAccount, null);
        });
    }

    @Test
    @DisplayName("Test link Accounts when Account and PlaidAccount are null, then throw exception")
    public void testLinkAccounts_whenAccountAndPlaidAccountAreNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            importer.linkAccounts(null, null);
        });
    }

    @Test
    @DisplayName("Test link Accounts when plaid account subType is empty, then throw exception")
    public void testLinkAccounts_whenPlaidAccountSubTypeIsEmpty_thenThrowException(){
        PlaidAccount plaidAccount = createPlaidAccount("Checking Test", "DEPOSITORY", "", "e2323232");
        AccountEntity account = createAccountEntity("01", "", 1, createUserEntity(1), "checking");

        assertThrows(InvalidPlaidSubTypeException.class, () -> {
            importer.linkAccounts(plaidAccount, account);
        });
    }

    @Test
    @DisplayName("Test linkAccounts when plaid account subType is valid, then return linkAccountInfo")
    public void testLinkAccounts_whenPlaidAccountSubTypeIsValid_thenReturnLinkAccountInfo(){
        PlaidAccount plaidAccount = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "CHECKING", "e2323232", "1111");
        AccountEntity account = createAccountEntityWithMask("DEPOSITORY", "01",1, createUserEntity(1), "checking", "1111");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(true);

        LinkedAccountInfo expected = new LinkedAccountInfo(1, "e2323232");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }

    @Test
    @DisplayName("Test linkAccounts when plaid account subtype is lowercase, then return linkAccountInfo")
    public void testLinkAccounts_whenPlaidAccountSubTypeIsLowercase_thenReturnLinkAccountInfo(){
        PlaidAccount plaidAccount = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e2323232", "1111");
        AccountEntity account = createAccountEntityWithMask("DEPOSITORY", "01",1, createUserEntity(1), "checking", "1111");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(true);

        LinkedAccountInfo expected = new LinkedAccountInfo(1, "e2323232");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }

    @Test
    @DisplayName("Test linkAccounts when plaid account subtype different from account subtype then break out and return linkAccountInfo")
    public void testLinkAccounts_whenPlaidAccountSubTypeDifferent_thenReturnLinkAccountInfo(){
        PlaidAccount plaidAccount = createPlaidAccount("Checking Test", "DEPOSITORY", "checking", "e2323232");
        AccountEntity account = createAccountEntity("02", "",2, createUserEntity(1), "savings");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(true);

        LinkedAccountInfo expected = new LinkedAccountInfo(2, "");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }

    @Test
    @DisplayName("Test linkAccounts when plaid account subtype matches account subtype, then return linkAccountInfo")
    public void testLinkAccounts_whenPlaidAccountSubTypeEqualsAccountSubType_thenReturnLinkAccountInfo(){
        PlaidAccount plaidAccount = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e2323232", "1111");
        AccountEntity account = createAccountEntityWithMask("DEPOSITORY", "01",1, createUserEntity(1), "checking", "1111");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(true);

        LinkedAccountInfo expected = new LinkedAccountInfo(1, "e2323232");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }

    @Test
    @DisplayName("Test linkAccounts when plaid account and account type match the plaid account type, then return linkAccountInfo")
    public void testLinkAccountsWhenPlaidAccountTypeAndAccountTypeAreEqual_thenReturnLinkAccountInfo(){
        PlaidAccount plaidAccount = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e2323232", "1111");
        AccountEntity account = createAccountEntityWithMask("DEPOSITORY", "01",1, createUserEntity(1), "checking", "1111");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(true);

        LinkedAccountInfo expected = new LinkedAccountInfo(1, "e2323232");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }

    @Test
    @DisplayName("Test linkAccounts when plaid account mask is empty/null, then return empty linked account info state")
    public void testLinkAccountsWhenPlaidAccountMaskIsEmpty_thenThrowException(){
        PlaidAccount plaidAccount = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e23232323", "");
        AccountEntity account = createAccountEntityWithMask("01", "",1, createUserEntity(1), "checking", "1111");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(true);

        LinkedAccountInfo expected = new LinkedAccountInfo(1, "");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }


    @Test
    @DisplayName("Test prepareLinkedAccounts when user is null, then throw exception")
    public void testPrepareLinkedAccounts_whenUserIsNull_thenThrowException(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        assertThrows(UserNotFoundException.class, () -> {
            importer.prepareLinkedAccounts(null, plaidAccounts);
        });
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when plaid account list is null, then throw exception")
    public void testPrepareLinkedAccounts_whenPlaidAccountsIsNull_thenThrowException(){
        UserEntity user = createUserEntity(1);
        assertThrows(PlaidAccountNotFoundException.class, () -> {
            importer.prepareLinkedAccounts(user, null);
        });
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when both plaid accounts list and user is null, then throw exception")
    public void testPrepareLinkedAccounts_WhenPlaidAccountsAndUserIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            importer.prepareLinkedAccounts(null, null);
        });
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when plaid accounts size is zero, then return empty list")
    public void testPrepareLinkedAccounts_whenPlaidAccountsSizeIsZero_thenThrowException(){
        UserEntity user = createUserEntity(1);
        List<PlaidAccount> expected = Collections.emptyList();

        List<LinkedAccountInfo> actual = importer.prepareLinkedAccounts(user, expected);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when user has two plaid accounts and two accounts, then return linkedAccountInfo list")
    public void testPrepareLinkedAccounts_whenUserHasTwoPlaidAccountsAndTwoAccount_thenReturnLinkedAccountInfoList(){
        UserEntity user = createUserEntity(1);

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount checkingPlaid = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e23232323", "1111");
        PlaidAccount savingsPlaid = createPlaidAccountWithMask("Savings Test", "DEPOSITORY", "savings", "e22222222", "2222");
        plaidAccounts.add(checkingPlaid);
        plaidAccounts.add(savingsPlaid);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo expected1 = new LinkedAccountInfo(1, "e23232323");
        LinkedAccountInfo expected2 = new LinkedAccountInfo(2, "e22222222");
        expected.add(expected1);
        expected.add(expected2);

        AccountEntity checking = createAccountEntityWithMask("DEPOSITORY", "01", 1, createUserEntity(1), "checking", "1111");
        AccountEntity savings = createAccountEntityWithMask("DEPOSITORY", "02", 2, createUserEntity(1), "savings", "2222");
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(checking);
        accounts.add(savings);

        Optional<PlaidLinkEntity> plaidLinkEntityOptional = mock(Optional.class);
        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accounts);
        when(plaidLinkService.findPlaidLinkEntityByUserId(1)).thenReturn(plaidLinkEntityOptional);

        List<LinkedAccountInfo> actual = importer.prepareLinkedAccounts(user, plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when user has two plaid accounts and one account, then link one account and create one new account")
    public void testPrepareLinkAccounts_whenUserHasTwoPlaidAccountsAndOneAccount_thenLinkOneAccount_returnLinkedAccountInfoList(){

    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when user has one plaid account and three accounts, then return one linked account")
    public void testPrepareLinkedAccounts_whenUserHasOnePlaidAccount_AndThreeAccounts_returnLinkedAccountInfoList(){
        UserEntity user = createUserEntity(1);

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount checkingPlaid = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e23232323", "1111");
        plaidAccounts.add(checkingPlaid);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo expected1 = new LinkedAccountInfo(1, "e23232323");
        expected.add(expected1);

        AccountEntity checking = createAccountEntityWithMask("DEPOSITORY", "01", 1, createUserEntity(1), "checking", "1111");
        AccountEntity savings = createAccountEntityWithMask("DEPOSITORY", "02", 2, createUserEntity(1), "savings", "2222");
        AccountEntity rent = createAccountEntityWithMask("DEPOSITORY", "03", 3, createUserEntity(1), "rent", "3333");
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(checking);
        accounts.add(savings);
        accounts.add(rent);

        Optional<PlaidLinkEntity> plaidLinkEntityOptional = mock(Optional.class);
        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accounts);
        when(plaidLinkService.findPlaidLinkEntityByUserId(1)).thenReturn(plaidLinkEntityOptional);

        List<LinkedAccountInfo> actual = importer.prepareLinkedAccounts(user, plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when user has null plaid account, then skip to next plaid account and return linkedAccountInfoList")
    public void testPrepareLinkedAccounts_whenUserHasNullPlaidAccountAndReturnLinkedAccountInfoList(){
        UserEntity user = createUserEntity(1);

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount checkingPlaid = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e23232323", "1111");
        PlaidAccount savingsPlaid = createPlaidAccountWithMask("Savings Test", "DEPOSITORY", "savings", "e22222222", "2222");
        plaidAccounts.add(checkingPlaid);
        plaidAccounts.add(null);
        plaidAccounts.add(savingsPlaid);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo expected1 = new LinkedAccountInfo(1, "e23232323");
        LinkedAccountInfo expected2 = new LinkedAccountInfo(2, "e22222222");
        expected.add(expected1);
        expected.add(expected2);

        AccountEntity checking = createAccountEntityWithMask("DEPOSITORY", "01", 1, createUserEntity(1), "checking", "1111");
        AccountEntity savings = createAccountEntityWithMask("DEPOSITORY", "02", 2, createUserEntity(1), "savings", "2222");
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(checking);
        accounts.add(savings);

        Optional<PlaidLinkEntity> plaidLinkEntityOptional = mock(Optional.class);
        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accounts);
        when(plaidLinkService.findPlaidLinkEntityByUserId(1)).thenReturn(plaidLinkEntityOptional);

        List<LinkedAccountInfo> actual = importer.prepareLinkedAccounts(user, plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test prepareLinkedAccounts when user has unmatching account, then skip account and return default linkedAccountInfo")
    public void testPrepareLinkedAccounts_whenUserHasUnMatchingAccount_thenSkipAccountAndReturnDefaultLinkedAccountInfo(){
        UserEntity user = createUserEntity(1);

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount checkingPlaid = createPlaidAccountWithMask("Plaid Checking", "DEPOSITORY", "checking", "e23232323", "1111");
        PlaidAccount savingsPlaid = createPlaidAccountWithMask("Plaid Savings", "DEPOSITORY", "savings", "e22222222", "2222");
        plaidAccounts.add(checkingPlaid);
        plaidAccounts.add(savingsPlaid);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo checkingLink = new LinkedAccountInfo(1, "e23232323");
        LinkedAccountInfo savingsLink = new LinkedAccountInfo(2, "e22222222");
        expected.add(checkingLink);
        expected.add(savingsLink);

        AccountEntity checking = createAccountEntityWithMask("Alex's Checking", "01", 1, user, "checking", "0000");
        AccountEntity savings = createAccountEntityWithMask("Alex's Savings", "02", 2, user, "savings", "1111");
        AccountEntity investment = createAccountEntityWithMask("Alex's Investment", "04", 3, user, "investment", "2222");
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(checking);
        accounts.add(savings);
        accounts.add(investment);

        Optional<PlaidLinkEntity> plaidLinkEntityOptional = mock(Optional.class);
        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accounts);
        when(plaidLinkService.findPlaidLinkEntityByUserId(1)).thenReturn(plaidLinkEntityOptional);

        List<LinkedAccountInfo> actual = importer.prepareLinkedAccounts(user, plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }


    @Test
    @DisplayName("Test prepareLinkedAccounts when user has null account and two plaid accounts, then skip null account and return linked account")
    public void testPrepareLinkedAccounts_whenUserHasNullAccountAndReturnLinkedAccountInfoList(){
        UserEntity user = createUserEntity(1);

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount checkingPlaid = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e23232323", "1111");
        PlaidAccount savingsPlaid = createPlaidAccountWithMask("Savings Test", "DEPOSITORY", "savings", "e22222222", "2222");
        plaidAccounts.add(checkingPlaid);
        plaidAccounts.add(savingsPlaid);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo expected1 = new LinkedAccountInfo(1, "e23232323");
        expected.add(expected1);

        AccountEntity checking = createAccountEntityWithMask("DEPOSITORY", "01", 1, createUserEntity(1), "checking", "1111");
        List<AccountEntity> accounts = new ArrayList<>();
        accounts.add(checking);
        accounts.add(null);

        Optional<PlaidLinkEntity> plaidLinkEntityOptional = mock(Optional.class);
        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accounts);
        when(plaidLinkService.findPlaidLinkEntityByUserId(1)).thenReturn(plaidLinkEntityOptional);

        List<LinkedAccountInfo> actual = importer.prepareLinkedAccounts(user, plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }


    @Test
    @DisplayName("Test import PlaidDataToSystemAccount when plaid account is null, then throw exception")
    public void testImportPlaidDataToSystemAccount_whenPlaidAccountIsNull_thenThrowException(){
        Account account = mock(Account.class);
        assertThrows(IllegalArgumentException.class, () -> {
            importer.importDataFromPlaidAccountToSystemAccount(null, account);
        });
    }

    @Test
    @DisplayName("Test importPlaidDataToSystemAccount when both account data and plaid data are null, then throw exception")
    public void testImportPlaidDataToSystemAccount_whenAccountDataAndPlaidDataAreNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> importer.importDataFromPlaidAccountToSystemAccount(null, null));
    }
//
    @Test
    @DisplayName("Test importPlaidDataToSystemAccount when account data is null, then throw exception")
    public void testImportPlaidDataToSystemAccount_whenAccountDataIsNull_thenThrowException(){
        PlaidAccount plaidAccount = mock(PlaidAccount.class);
        assertThrows(IllegalArgumentException.class, () -> {
            importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, null);
        });
    }

    @Test
    @DisplayName("Test importPlaidDataToSystemAccount when both account data and plaid data are valid, then return true")
    public void testImportPlaidDataToSystemAccount_whenBothAccountDataAndPlaidDataAreValid_thenReturnTrue(){
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setAccountId("e123123123123");
        plaidAccount.setSubtype("checking");
        plaidAccount.setType("depository");
        plaidAccount.setMask("1111");
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1150));
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setOfficialName("Plaid Checking");
        plaidAccount.setName("Checking Plaid Account");
        plaidAccount.setLimit(BigDecimal.valueOf(1000));

        Account account = new Account();
        account.setAccountID(1);
        account.setAccountName("Alex's Checking");
        account.setAccountType(AccountType.CHECKING);
        account.setMask("1111");
        account.setBalance(BigDecimal.valueOf(250));
        account.setUser("AKing94");
        account.setUserID(1);
        account.setSubType("checking");
        account.setType("depository");

        AccountCodeEntity aCodeEntity = createAccountCodeEntity();
        UserEntity user = createUserEntity(1);

        PlaidImportResult expected = new PlaidImportResult(account, true);

        AccountEntity accountEntity = createAccountEntity("DEPOSITORY", "01", 1, createUserEntity(1), "checking");
        when(userService.findById(1)).thenReturn(createUserEntity(1));
        when(accountCodeService.findByUserIdAndAcctSegment(1, 1)).thenReturn(aCodeEntity);
        when(accountService.buildAccountEntityByAccountModel(account, aCodeEntity, user)).thenReturn(accountEntity);
        doNothing().when(accountService).save(accountEntity);

        PlaidImportResult actual = importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
        assertEquals(expected.getResult(), actual.getResult());
        assertEquals(account.getAccountName(), plaidAccount.getOfficialName());
        assertEquals(account.getBalance(), plaidAccount.getCurrentBalance());
    }

    @Test
    @DisplayName("Test import PlaidDataToSystemAccount when both account data and plaid data are valid, then convert to account and save to account")
    public void testImportPlaidDataToSystemAccount_whenBothAccountAndPlaidDataAreValid_thenConvertToAccountAndSaveToAccount(){
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setAccountId("e123123123123");
        plaidAccount.setSubtype("checking");
        plaidAccount.setType("depository");
        plaidAccount.setMask("1111");
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1150));
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setOfficialName("Plaid Checking");
        plaidAccount.setName("Checking Plaid Account");
        plaidAccount.setLimit(BigDecimal.valueOf(1000));

        Account account = new Account();
        account.setAccountID(1);
        account.setAccountName("Alex's Checking");
        account.setAccountType(AccountType.CHECKING);
        account.setMask("1111");
        account.setBalance(BigDecimal.valueOf(250));
        account.setUser("AKing94");
        account.setUserID(1);
        account.setSubType("checking");
        account.setType("depository");

        AccountCodeEntity accountCode = createAccountCodeEntity();

        AccountEntity accountEntity = createAccountEntity("DEPOSITORY", "01", 1,  createUserEntity(1), "checking");
        when(userService.findById(1)).thenReturn(createUserEntity(1));
        when(accountCodeService.findByUserIdAndAcctSegment(1, 1)).thenReturn(createAccountCodeEntity());
        when(accountService.buildAccountEntityByAccountModel(account, accountCode, createUserEntity(1))).thenReturn(accountEntity);

        doNothing().when(accountService).save(accountEntity);

        PlaidImportResult expected = new PlaidImportResult(account, true);

        PlaidImportResult actual = importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
        assertEquals(expected.getResult(), actual.getResult());

        assertEquals(1, account.getUserID());
        assertEquals(account.getAccountName(), plaidAccount.getOfficialName());
        assertEquals(account.getBalance(), plaidAccount.getCurrentBalance());

//       verify(accountService).buildAccountEntityByAccountModel(any(Account.class), any(AccountCodeEntity.class), any(UserEntity.class));
    }


    @Test
    @DisplayName("Test importPlaidDataToSystemAccount when account entity is null, then throw exception")
    public void testImportPlaidDataToSystemAccount_whenAccountEntityIsNull_thenThrowException(){

        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setAccountId("e123123123123");
        plaidAccount.setSubtype("checking");
        plaidAccount.setType("depository");
        plaidAccount.setMask("1111");
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1150));
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setOfficialName("Plaid Checking");
        plaidAccount.setName("Checking Plaid Account");
        plaidAccount.setLimit(BigDecimal.valueOf(1000));

        Account account = new Account();
        account.setAccountID(1);
        account.setAccountName("Alex's Checking");
        account.setAccountType(AccountType.CHECKING);
        account.setMask("1111");
        account.setBalance(BigDecimal.valueOf(250));
        account.setUser("AKing94");
        account.setUserID(1);
        account.setSubType("checking");
        account.setType("depository");

        when(userService.findById(1)).thenReturn(createUserEntity(1));
        when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenThrow(new IllegalArgumentException(""));

        assertThrows(IllegalArgumentException.class, () -> {
            importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
        });
    }

    @Test
    @DisplayName("Test createImportedAccountsFromNonLinkedAccountsList when map is null, then throw exception")
    public void testCreateImportedAccountsFromNonLinkedAccountsList_whenMapIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            importer.createImportedAccountsFromNonLinkAccountsList(null);
        });
    }

    @Test
    @DisplayName("Test checkPlaidAccountsAreLinked when external account list has one linked account, then return true")
    public void testCheckPlaidAccountsAreLinked_whenExternalAccountsListHasOneLinkedAccount_thenReturnTrue(){
        UserEntity user = createUserEntity(1);
        List<ExternalAccountsEntity> externalAccountsEntities = new ArrayList<>();
        ExternalAccountsEntity externalAccountsEntity = createExternalAccountWithUserId(1, user,"e232323232");
        externalAccountsEntities.add(externalAccountsEntity);

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount plaidTest1 = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        plaidAccounts.add(plaidTest1);

        List<AccountEntity> accountEntities = new ArrayList<>();
        AccountEntity account = createAccountEntity("DEPOSITORY", "01", 1, createUserEntity(1), "checking");
        accountEntities.add(account);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountEntities);

        Boolean result = importer.checkPlaidAccountsAreLinked(externalAccountsEntities, plaidAccounts);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test checkPlaidAccountsAreLinked when no plaid account link found, then return false")
    public void testCheckPlaidAccountsAreLinked_whenNoPlaidAccountLinkFound_thenReturnFalse(){
        List<ExternalAccountsEntity> externalAccountsEntities = new ArrayList<>();

        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        PlaidAccount plaidTest1 = createPlaidAccount("Test Checking", "DEPOSITORY", "checking", "e232323232");
        plaidAccounts.add(plaidTest1);

        List<AccountEntity> accountEntities = new ArrayList<>();
        AccountEntity account = createAccountEntity("DEPOSITORY", "01", 1, createUserEntity(1), "checking");
        accountEntities.add(account);

        Boolean result = importer.checkPlaidAccountsAreLinked(externalAccountsEntities, plaidAccounts);
        assertFalse(result);
    }

    @Test
    @DisplayName("Test execute create and save external account entity when linked account info list is null, then throw exception")
    public void testExecuteCreateAndSaveExternalAccountEntity_whenLinkedAccountInfoListIsNull_thenThrowException(){
        assertThrows(LinkedAccountInfoListNullException.class, () -> {
            importer.executeCreateAndSaveExternalAccountEntity(null);
        });
    }

    @Test
    @DisplayName("Test execute create and save external account when linked account info list is valid, then return true")
    public void testExecuteCreateAndSaveExternalAccountEntity_whenLinkedAccountInfoListIsValid_thenReturnTrue(){

        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo expected1 = new LinkedAccountInfo(1, "e23232323");
        expected.add(expected1);

        Boolean result = importer.executeCreateAndSaveExternalAccountEntity(expected);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test execute create and save external account when linked account info object is null, then skip to next and return true")
    public void testExecuteCreateAndSaveExternalAccountEntity_whenLinkedAccountInfoIsNull_thenSkipToNextAndReturnTrue(){
        List<LinkedAccountInfo> expected = new ArrayList<>();
        LinkedAccountInfo expected1 = new LinkedAccountInfo(1, "e23232323");
        LinkedAccountInfo expected2 = new LinkedAccountInfo(2, "ghtmsdfasdfs");
        expected.add(expected1);
        expected.add(null);
        expected.add(expected2);

        Boolean result = importer.executeCreateAndSaveExternalAccountEntity(expected);
        assertTrue(result);
    }

    @Test
    @DisplayName("Test GetSystemAccountIdsForUser when userId valid, then return list of acctIds")
    public void testGetSystemAccountIdsForUser_whenUserIdIsValid_thenReturnListOfAcctIds(){
        List<Integer> expected = new ArrayList<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);

        AccountEntity checking = createAccountEntityWithMask("Alex's Checking", "01", 1, createUserEntity(1), "checking", "0000");
        AccountEntity savings = createAccountEntityWithMask("Alex Savings", "02", 2, createUserEntity(1), "savings", "1111");
        AccountEntity investment = createAccountEntityWithMask("Alex's Investment", "04", 3, createUserEntity(1), "other", "2222");
        List<AccountEntity> accountEntities = new ArrayList<>();
        accountEntities.add(checking);
        accountEntities.add(savings);
        accountEntities.add(investment);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountEntities);

        List<Integer> actual = importer.getSystemAccountIdsForUser(1);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test createAccountEntityFromModel when account, user, or account code are null, then throw exception")
    public void testCreateAccountEntityFromModel_whenAccountOrUserOrAccountCodeIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            importer.createAccountEntityFromModel(null, null, null);
        });
    }

    @Test
    @DisplayName("Test createAccountEntityFromModel when account, user, or account code is valid, then return AccountEntity")
    public void testCreateAccountEntityFromModel_whenAccountAndUserAndAccountCodeAreValid_thenReturnAccountEntity(){
        Account account = new Account();
        account.setAccountID(1);
        account.setAccountName("Alex's Checking");
        account.setAccountType(AccountType.CHECKING);
        account.setMask("1111");
        account.setBalance(BigDecimal.valueOf(250));
        account.setUser("AKing94");
        account.setUserID(1);
        account.setSubType("checking");
        account.setType("depository");

        UserEntity user = createUserEntity(1);

        AccountCodeEntity aCode = createAccountCodeEntity();
        AccountEntity accountEntity = createAccountEntityWithMask("Alex's Checking", "01", 1, createUserEntity(1), "checking", "0000");

        when(accountService.buildAccountEntityByAccountModel(account, aCode, user)).thenReturn(accountEntity);

        Optional<AccountEntity> expectedAccountEntity = Optional.of(accountEntity);
        Optional<AccountEntity> actual = importer.createAccountEntityFromModel(account, user, aCode);
        assertEquals(expectedAccountEntity, actual);
        assertEquals(expectedAccountEntity.get(), accountEntity);
    }




//
//
//    @Test
//    @DisplayName("test validate AccountSubTypeToTypeCriteria when account list is null/empty, then throw exception")
//    public void testValidateAccountSubTypeToTypeCriteria_whenAccountListIsNull_thenThrowException(){
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.validateAccountSubTypeToTypeCriteria(null);
//        });
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.validateAccountSubTypeToTypeCriteria(Collections.emptyList());
//        });
//    }
//
//    @Test
//    @DisplayName("test validate AccountSubTypeToTypeCriteria when account list is valid, subType and type are empty, then throw exception")
//    public void testValidateAccountSubTypeToTypeCriteria_whenAccountListIsValid_AndSubTypeAndTypeAreEmpty_thenThrowException(){
//
//        AccountEntity accountEntity = new AccountEntity();;
//        accountEntity.setAcctID(1);
//        accountEntity.setAccountName("Alex's Checking");
//        accountEntity.setAccountType("");
//        accountEntity.setSubtype("");
//        accountEntity.setBalance(BigDecimal.valueOf(1200));
//
//        List<AccountEntity> accountEntities = Collections.singletonList(accountEntity);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.validateAccountSubTypeToTypeCriteria(accountEntities);
//        });
//    }
//

//
//    @Test
//    @DisplayName("Test validate AccountSubTypeToTypeCriteria when subtype and type are valid, then return true")
//    public void testValidateAccountSubTypeToTypeCriteria_whenSubTypeAndTypeAreValid_thenReturnTrue(){
//        AccountEntity accountEntity = new AccountEntity();;
//        accountEntity.setAcctID(1);
//        accountEntity.setAccountName("Alex's Checking");
//        accountEntity.setAccountType("01");
//        accountEntity.setType("depository");
//        accountEntity.setSubtype("checking");
//        accountEntity.setBalance(BigDecimal.valueOf(1200));
//
//        List<AccountEntity> accountEntities = Collections.singletonList(accountEntity);
//
//        Boolean result = importer.validateAccountSubTypeToTypeCriteria(accountEntities);
//        assertTrue(result);
//    }
//
//    @Test
//    @DisplayName("Test validate AccountSubTypeToTypeCriteria when account entity is null, then skip null account and return true")
//    public void testValidateAccountSubTypeToTypeCriteria_whenAccountEntityIsNull_thenSkipAccountAndReturnTrue(){
//        AccountEntity accountEntity = new AccountEntity();;
//        accountEntity.setAcctID(1);
//        accountEntity.setAccountName("Alex's Checking");
//        accountEntity.setAccountType("01");
//        accountEntity.setType("depository");
//        accountEntity.setSubtype("checking");
//        accountEntity.setBalance(BigDecimal.valueOf(1200));
//
//        List<AccountEntity> accountEntityList = new ArrayList<>();
//        accountEntityList.add(accountEntity);
//        accountEntityList.add(null);
//        accountEntityList.add(accountEntity);
//
//        Boolean result = importer.validateAccountSubTypeToTypeCriteria(accountEntityList);
//        assertTrue(result);
//    }


    private ExternalAccountsEntity createExternalAccountWithUserId(int acctID, UserEntity user, String plaidAcctID)
    {
        ExternalAccountsEntity externalAccountsEntity = new ExternalAccountsEntity();
        externalAccountsEntity.setAccount(AccountEntity.builder().acctID(acctID).user(user).build());
        externalAccountsEntity.setExternalAcctID(plaidAcctID);
        return externalAccountsEntity;
    }

    private ExternalAccountsEntity createExternalAccount(int acctID, String plaidAcctID){
        ExternalAccountsEntity externalAccountsEntity = new ExternalAccountsEntity();
        externalAccountsEntity.setAccount(AccountEntity.builder().acctID(acctID).build());
        externalAccountsEntity.setExternalAcctID(plaidAcctID);
        return externalAccountsEntity;
    }

    private AccountEntity createAccountEntity(String type, String acctType, int acctID, UserEntity user, String subType)
    {
        return AccountEntity.builder()
                .accountType(acctType)
                .subtype(subType)
                .type(type)
                .acctID(acctID)
                .user(user)
                .build();
    }

    private AccountEntity createAccountEntityWithMask(String type, String acctType, int acctID, UserEntity user, String subType, String mask){
        return AccountEntity.builder()
                .accountType(acctType)
                .subtype(subType)
                .type(type)
                .acctID(acctID)
                .mask(mask)
                .user(user)
                .build();
    }

    private PlaidAccount createPlaidAccountWithMask(String name, String type, String subtype, String acctID, String mask){
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId(acctID);
        plaidAccount.setName(name);
        plaidAccount.setSubtype(subtype);
        plaidAccount.setType(type);
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1100));
        plaidAccount.setMask(mask);
        return plaidAccount;
    }

    private PlaidAccount createPlaidAccount(String name, String type, String subtype, String acctID)
    {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId(acctID);
        plaidAccount.setName(name);
        plaidAccount.setSubtype(subtype);
        plaidAccount.setType(type);
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1100));
        return plaidAccount;
    }

    private UserEntity createUserEntity(int userID)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(userID);
        return userEntity;
    }

    private AccountCodeEntity createAccountCodeEntity(){
        return AccountCodeEntity.builder()
                .accountType("01")
                .account_segment(1)
                .first_initial_segment("A")
                .last_initial_segment("K")
                .year_segment(24)
                .user(UserEntity.builder().userID(1).build())
                .build();
    }

    private LinkedAccountInfo createLinkedAccountInfo(String external, int acctID)
    {
        LinkedAccountInfo linkedAccountInfo = new LinkedAccountInfo();
        linkedAccountInfo.setExternalAcctID(external);
        linkedAccountInfo.setSystemAcctID(acctID);
        return linkedAccountInfo;
    }

    private UserEntity createUserEntityWithInvalidUserId()
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(-1);
        return userEntity;
    }

    private PlaidAccount createPlaidAccountWithEmptySubType()
    {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId("e123123123123");
        plaidAccount.setName("Checking");
        plaidAccount.setSubtype("");
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1100));
        return plaidAccount;
    }

    @AfterEach
    void tearDown() {
    }
}