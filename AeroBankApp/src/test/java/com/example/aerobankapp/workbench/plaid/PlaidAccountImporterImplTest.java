package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.*;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
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
    private AccountCodeService accountCodeService;

    @Mock
    private UserService userService;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @Mock
    private PlaidLinkService plaidLinkService;


    @BeforeEach
    void setUp() {
        importer = new PlaidAccountImporterImpl(externalAccountsService,plaidLinkService, accountService, accountCodeService, userService);
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

        LinkedAccountInfo expected = new LinkedAccountInfo(0, "");
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

        LinkedAccountInfo expected = new LinkedAccountInfo(0, "");
        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
    }

    @Test
    @DisplayName("Test linkAccounts when hasPlaidAccess token is false, then throw exception")
    public void testLinkAccountsWhenHasPlaidAccessIsFalse_thenThrowException(){
        PlaidAccount plaidAccount = createPlaidAccountWithMask("Checking Test", "DEPOSITORY", "checking", "e23232323", "");
        AccountEntity account = createAccountEntityWithMask("DEPOSITORY", "01",1, createUserEntity(1), "checking", "1111");

        when(plaidLinkService.hasPlaidLink(1)).thenReturn(false);

        assertThrows(PlaidAccessTokenNotFoundException.class, () -> {
            importer.linkAccounts(plaidAccount, account);
        });
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




//    @Test
//    @DisplayName("Test import PlaidDataToSystemAccount when plaid account is null, then throw exception")
//    public void testImportPlaidDataToSystemAccount_whenPlaidAccountIsNull_thenThrowException(){
//        Account account = mock(Account.class);
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.importDataFromPlaidAccountToSystemAccount(null, account);
//        });
//    }
//
//    @Test
//    @DisplayName("Test Process Plaid Account By Subtype when subtype is SAVINGS, then return LinkedAccountInfo list")
//    public void testProcessAccountBySubType_whenSubtypeEqualsSAVINGS_thenReturnLinkedAccountInfoList(){
//        PlaidAccount plaidAccount = createPlaidAccount("DEPOSITORY", "SAVINGS", "e123123123123");
//        AccountEntity accountCode = createAccountEntity("02", 2, createUserEntity(1), "savings");
//
//        LinkedAccountInfo expected = new LinkedAccountInfo(2, "e123123123123");
//
//        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, accountCode);
//        assertNotNull(actual);
//        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
//        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
//    }
//
//    @Test
//    @DisplayName("Test importPlaidDataToSystemAccount when both account data and plaid data are null, then throw exception")
//    public void testImportPlaidDataToSystemAccount_whenAccountDataAndPlaidDataAreNull_thenThrowException(){
//        assertThrows(IllegalArgumentException.class, () -> importer.importDataFromPlaidAccountToSystemAccount(null, null));
//    }
//
//    @Test
//    @DisplayName("Test importPlaidDataToSystemAccount when account data is null, then throw exception")
//    public void testImportPlaidDataToSystemAccount_whenAccountDataIsNull_thenThrowException(){
//        PlaidAccount plaidAccount = mock(PlaidAccount.class);
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, null);
//        });
//    }
//
//    @Test
//    @DisplayName("Test Process Plaid Account By SubType when subType is empty or null then throw exception")
//    public void testProcessAccountBySubType_whenSubTypeIsEmptyOrNull_thenThrowException(){
//        assertThrows(IllegalArgumentException.class, () ->
//                importer.linkAccounts(createPlaidAccount("", "","e123123123123"), createAccountEntity("01", 1, createUserEntity(1), "")));
//    }
//
//
//    @Test
//    @DisplayName("Test importPlaidDataToSystemAccount when both account data and plaid data are valid, then return true")
//    public void testImportPlaidDataToSystemAccount_whenBothAccountDataAndPlaidDataAreValid_thenReturnTrue(){
//        PlaidAccount plaidAccount = new PlaidAccount();
//        plaidAccount.setAccountId("e123123123123");
//        plaidAccount.setSubtype("checking");
//        plaidAccount.setType("depository");
//        plaidAccount.setMask("1111");
//        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1150));
//        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
//        plaidAccount.setOfficialName("Plaid Checking");
//        plaidAccount.setName("Checking Plaid Account");
//        plaidAccount.setLimit(BigDecimal.valueOf(1000));
//
//        Account account = new Account();
//        account.setAccountID(1);
//        account.setAccountName("Alex's Checking");
//        account.setAccountType(AccountType.CHECKING);
//        account.setMask("1111");
//        account.setBalance(BigDecimal.valueOf(250));
//        account.setUser("AKing94");
//        account.setUserID(1);
//        account.setSubType("checking");
//        account.setType("depository");
//
//        AccountEntity accountEntity = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        when(userService.findById(1)).thenReturn(createUserEntity(1));
//        when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenReturn(accountEntity);
//        doNothing().when(accountService).save(accountEntity);
//
//        PlaidImportResult expected = new PlaidImportResult(account, true);
//
//        PlaidImportResult actual = importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
//        assertEquals(expected.getResult(), actual.getResult());
//        assertEquals(account.getAccountName(), plaidAccount.getOfficialName());
//        assertEquals(account.getBalance(), plaidAccount.getCurrentBalance());
//    }
//
//    @Test
//    @DisplayName("Test import PlaidDataToSystemAccount when both account data and plaid data are valid, then convert to account and save to account")
//    public void testImportPlaidDataToSystemAccount_whenBothAccountAndPlaidDataAreValid_thenConvertToAccountAndSaveToAccount(){
//        PlaidAccount plaidAccount = new PlaidAccount();
//        plaidAccount.setAccountId("e123123123123");
//        plaidAccount.setSubtype("checking");
//        plaidAccount.setType("depository");
//        plaidAccount.setMask("1111");
//        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1150));
//        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
//        plaidAccount.setOfficialName("Plaid Checking");
//        plaidAccount.setName("Checking Plaid Account");
//        plaidAccount.setLimit(BigDecimal.valueOf(1000));
//
//        Account account = new Account();
//        account.setAccountID(1);
//        account.setAccountName("Alex's Checking");
//        account.setAccountType(AccountType.CHECKING);
//        account.setMask("1111");
//        account.setBalance(BigDecimal.valueOf(250));
//        account.setUser("AKing94");
//        account.setUserID(1);
//        account.setSubType("checking");
//        account.setType("depository");
//
//        AccountEntity accountEntity = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        when(userService.findById(1)).thenReturn(createUserEntity(1));
//        when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenReturn(accountEntity);
//        doNothing().when(accountService).save(accountEntity);
//
//        PlaidImportResult expected = new PlaidImportResult(account, true);
//
//        PlaidImportResult actual = importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
//        assertEquals(expected.getResult(), actual.getResult());
//
//        assertEquals(1, account.getUserID());
//        assertEquals(account.getAccountName(), plaidAccount.getOfficialName());
//        assertEquals(account.getBalance(), plaidAccount.getCurrentBalance());
//
////       verify(accountService).buildAccountEntityByAccountModel(any(Account.class), any(AccountCodeEntity.class), any(UserEntity.class));
//    }
//
//    @Test
//    @DisplayName("Test LinkAccount when subtype is CHECKING, then return LinkedAccountInfo list")
//    public void testLinkAccount_whenSubtypeEqualsCHECKING_thenReturnLinkedAccountInfoList(){
//        PlaidAccount plaidAccount = createPlaidAccount("DEPOSITORY", "checking","e123123123123");
//        AccountEntity account = createAccountEntity("01", 1, createUserEntity(1), "checking");
//
//        LinkedAccountInfo expected = new LinkedAccountInfo(1, "e123123123123");
//
//        LinkedAccountInfo actual = importer.linkAccounts(plaidAccount, account);
//        assertNotNull(actual);
//        assertNotEquals(expected, actual);
//        assertEquals(expected.getExternalAcctID(), actual.getExternalAcctID());
//        assertEquals(expected.getSystemAcctID(), actual.getSystemAcctID());
//
//    }
//
//    @Test
//    @DisplayName("Test importPlaidDataToSystemAccount when account entity is null, then throw exception")
//    public void testImportPlaidDataToSystemAccount_whenAccountEntityIsNull_thenThrowException(){
//
//        PlaidAccount plaidAccount = new PlaidAccount();
//        plaidAccount.setAccountId("e123123123123");
//        plaidAccount.setSubtype("checking");
//        plaidAccount.setType("depository");
//        plaidAccount.setMask("1111");
//        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1150));
//        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
//        plaidAccount.setOfficialName("Plaid Checking");
//        plaidAccount.setName("Checking Plaid Account");
//        plaidAccount.setLimit(BigDecimal.valueOf(1000));
//
//        Account account = new Account();
//        account.setAccountID(1);
//        account.setAccountName("Alex's Checking");
//        account.setAccountType(AccountType.CHECKING);
//        account.setMask("1111");
//        account.setBalance(BigDecimal.valueOf(250));
//        account.setUser("AKing94");
//        account.setUserID(1);
//        account.setSubType("checking");
//        account.setType("depository");
//
//        when(userService.findById(1)).thenReturn(createUserEntity(1));
//        when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenThrow(new IllegalArgumentException(""));
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.importDataFromPlaidAccountToSystemAccount(plaidAccount, account);
//        });
//    }
//
//    @Test
//    @DisplayName("Test GetLinkedAccountInfoList when there's more account codes than plaid accounts, then return out of bounds exception")
//    public void testGetLinkedAccountInfoList_whenMoreAccountCodesThanPlaidAccounts_thenReturnIndexOutOfBoundsException(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY",  "checking", "e123123123123"));
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "savings", "e42323-23432-554a2"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//        expected.add(createLinkedAccountInfo("e42323-23432-554a2", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        AccountEntity accountCode1 = createAccountEntity("02", 2, createUserEntity(1), "savings");
//        AccountEntity accountCode2 = createAccountEntity("03", 2, createUserEntity(1), "rent");
//        accountCodeEntities.add(accountCode);
//        accountCodeEntities.add(accountCode1);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
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
//    @Test
//    @DisplayName("Test getUserToAccountIdsMap when plaid account list is null, then throw exception")
//    public void testGetUserToAccountIdsMapWhenPlaidAccountListIsNull() {
//        assertThrows(IllegalArgumentException.class, () -> importer.prepareLinkedAccountInfoList(null, null));
//    }
//
//    @Test
//    @DisplayName("Test getUserToAccountIdsMap when user or plaid account list is null, then throw exception")
//    public void testGetUserToAccountIdsMap_whenUserOrPlaidAccountListIsNull_thenThrowException() {
//        assertThrows(IllegalArgumentException.class, () -> importer.prepareLinkedAccountInfoList(null, null));
//    }
//
//    @Test
//    @DisplayName("Test getUserToAccountIdsMap when userId is not valid, then throw exception")
//    public void testGetUserToAccountIdsMap_whenUserIdIsNotValid_thenThrowException() {
//
//        List<PlaidAccount> plaidAccountList = Collections.emptyList();
//        assertThrows(InvalidUserIDException.class, () -> importer.prepareLinkedAccountInfoList(createUserEntityWithInvalidUserId(), plaidAccountList));
//    }
//
//
//    @Test
//    @DisplayName("Test getLinkedAccountInfoList when userId is valid and plaid account list is not empty, then return accountIds map")
//    public void testGetLinkedAccountInfoList_whenUserIdIsValidAndPlaidAccountListIsNotEmpty_thenReturnMap() {
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "CHECKING", "e123123123123"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//        expected.add(createLinkedAccountInfo("e123123123123", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode1 = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(2), "savings");
//        accountCodeEntities.add(accountCode1);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//    @Test
//    @DisplayName("Test GetLinkedAccountInfo List when userId is valid and plaid account is null then skip to next plaid account, then return linked account list")
//    public void testGetLinkedAccountInfoList_whenUserIdIsNull_AndPlaidAccountIsNull_thenSkipToNextPlaidAccount_thenReturnLinkedAccountInfoList(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "CHECKING", "e123123123123"));
//        plaidAccounts.add(null);
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "SAVINGS", "e123123123123"));
//        plaidAccounts.add(null);
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//        expected.add(createLinkedAccountInfo("e123123123123", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode1 = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(2), "savings");
//        accountCodeEntities.add(accountCode1);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//
//    @Test
//    @DisplayName("Test GetLinkedAccountInfo when account Code entity is null, then skip null account and return LinkedAccountInfo list")
//    public void testGetLinkedAccountInfo_whenAccountEntityIsNull_thenSkipNullAccountEntityAndReturnLinkedAccountInfoList(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "checking" ,"e123123123123"));
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "savings", "e123123123123"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(2), "savings");
//        accountCodeEntities.add(null);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//    @Test
//    @DisplayName("Test GetLinkedAccountInfoList when plaid account sub type is empty/null then throw exception")
//    public void testGetLinkedAccountInfoList_whenPlaidAccountSubTypeIsEmptyOrNull_thenThrowException()
//    {
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccountWithEmptySubType());
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(1), "savings");
//        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        accountCodeEntities.add(accountCode);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        });
//    }
//
//
//    @Test
//    @DisplayName("Test GetLinkedAccountInfoList when subType is valid and is checking type then return linkedAccountList")
//    public void testGetLinkedAccountInfoList_whenSubTypeIsValidAndIsCheckingType_thenReturnLinkedAccountList()
//    {
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "CHECKING", "e123123123123"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1), "savings");
//        accountCodeEntities.add(accountCode);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//
//
//    @Test
//    @DisplayName("Test getLinkedAccountInfoList when subType is SAVINGS type then return linkedAccountList")
//    public void testGetLinkedAccountInfoList_whenSubTypeEqualsSAVINGS_type_thenReturnLinkedAccountList(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "SAVINGS" ,"e42323-23432-554a2"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode = createAccountEntity("02", 2, createUserEntity(1), "savings");
//        accountCodeEntities.add(accountCode);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//    @Test
//    @DisplayName("Test getLinkedAccountInfoList when subType is CHECKING, AccountType is 01, then return LinkedAccountInfo list")
//    public void testGetLinkedAccountInfoList_whenSubTypeEqualsCHECKING_AccountTypeIsZeroOne_thenReturnLinkedAccountInfoList(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "CHECKING", "e123123123123"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        accountCodeEntities.add(accountCode);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//    @Test
//    @DisplayName("Test getLinkedAccountInfoList when subTypes are CHECKING and SAVINGS, return LinkedAccountInfo list")
//    public void testGetLinkedAccountInfoList_whenSubTypesAreCheckingAndSavings_thenReturnLinkedAccountInfoList(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "CHECKING", "e123123123123"));
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "SAVINGS", "e42323-23432-554a2"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//        expected.add(createLinkedAccountInfo("e42323-23432-554a2", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(1), "savings");
//        accountCodeEntities.add(accountCode);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
//
//    @Test
//    @DisplayName("Test GetLinkedAccountInfoList when system account type doesn't exist in plaid account subtype, then skip account with type and return linked account info list")
//    public void testGetLinkedAccountInfoList_whenAccountTypeDNE_thenSkipAccountWithTypeAndReturnLinkedAccountInfoList(){
//        List<PlaidAccount> plaidAccounts = new ArrayList<>();
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY", "CHECKING", "e123123123123"));
//        plaidAccounts.add(createPlaidAccount("DEPOSITORY",  "SAVINGS", "e42323-23432-554a2"));
//
//        List<LinkedAccountInfo> expected = new ArrayList<>();
//        expected.add(createLinkedAccountInfo("e123123123123", 1));
//        expected.add(createLinkedAccountInfo("e42323-23432-554a2", 2));
//
//        List<AccountEntity> accountCodeEntities = new ArrayList<>();
//        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1), "checking");
//        AccountEntity accountCode2 = createAccountEntity("03", 2, createUserEntity(1), "rent");
//        accountCodeEntities.add(accountCode);
//        accountCodeEntities.add(accountCode2);
//
//        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
//        List<LinkedAccountInfo> actual = importer.prepareLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
//        assertNotNull(actual);
//        assertEquals(expected.size(), actual.size());
//        for (int i = 0; i < actual.size(); i++) {
//            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
//            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
//        }
//    }
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