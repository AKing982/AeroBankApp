package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.account.AccountType;
import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.*;
import com.example.aerobankapp.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({JpaConfig.class, AppConfig.class})
class PlaidDataImporterImplTest {

    @InjectMocks
    private PlaidDataImporterImpl plaidAccountToSystemAccountMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountCodeService accountCodeService;

    @Mock
    private UserServiceImpl userDataManager;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @BeforeEach
    void setUp() {
        plaidAccountToSystemAccountMapper = new PlaidDataImporterImpl(accountService, accountCodeService, userDataManager, externalAccountsService);
    }

    @Test
    @DisplayName("Test getUserToAccountIdsMap when plaid account list is null, then throw exception")
    public void testGetUserToAccountIdsMapWhenPlaidAccountListIsNull() {
        assertThrows(IllegalArgumentException.class, () -> plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(null, null));
    }

    @Test
    @DisplayName("Test getUserToAccountIdsMap when user or plaid account list is null, then throw exception")
    public void testGetUserToAccountIdsMap_whenUserOrPlaidAccountListIsNull_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(null, null));
    }

    @Test
    @DisplayName("Test getUserToAccountIdsMap when userId is not valid, then throw exception")
    public void testGetUserToAccountIdsMap_whenUserIdIsNotValid_thenThrowException() {

        List<PlaidAccount> plaidAccountList = Collections.emptyList();
        assertThrows(InvalidUserIDException.class, () -> plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntityWithInvalidUserId(), plaidAccountList));
    }

    @Test
    @DisplayName("Test getLinkedAccountInfoList when userId is valid and plaid account list is not empty, then return accountIds map")
    public void testGetLinkedAccountInfoList_whenUserIdIsValidAndPlaidAccountListIsNotEmpty_thenReturnMap() {
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("checking", "e123123123123"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode1 = createAccountEntity("01", 1, createUserEntity(1));
        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(2));
        accountCodeEntities.add(accountCode1);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfo List when userId is valid and plaid account is null then skip to next plaid account, then return linked account list")
    public void testGetLinkedAccountInfoList_whenUserIdIsNull_AndPlaidAccountIsNull_thenSkipToNextPlaidAccount_thenReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("checking", "e123123123123"));
        plaidAccounts.add(null);
        plaidAccounts.add(createPlaidAccount("checking", "e123123123123"));
        plaidAccounts.add(null);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode1 = createAccountEntity("01", 1, createUserEntity(1));
        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(2));
        accountCodeEntities.add(accountCode1);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfo when account Code entity is null, then skip null account code and return LinkedAccountInfo list")
    public void testGetLinkedAccountInfo_whenAccountCodeEntityIsNull_thenSkipNullAccountCodeEntityAndReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("checking", "e123123123123"));
        plaidAccounts.add(createPlaidAccount("checking", "e123123123123"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(2));
        accountCodeEntities.add(null);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfoList when plaid account sub type is empty/null then throw exception")
    public void testGetLinkedAccountInfoList_whenPlaidAccountSubTypeIsEmptyOrNull_thenThrowException()
    {
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccountWithEmptySubType());

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(1));
        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1));
        accountCodeEntities.add(accountCode);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        });
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfoList when subType is valid and is checking type then return linkedAccountList")
    public void testGetLinkedAccountInfoList_whenSubTypeIsValidAndIsCheckingType_thenReturnLinkedAccountList()
    {
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING", "e123123123123"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1));
        accountCodeEntities.add(accountCode);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test getLinkedAccountInfoList when subType is SAVINGS type then return linkedAccountList")
    public void testGetLinkedAccountInfoList_whenSubTypeEqualsSAVINGS_type_thenReturnLinkedAccountList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("SAVINGS", "e42323-23432-554a2"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode = createAccountEntity("02", 2, createUserEntity(1));
        accountCodeEntities.add(accountCode);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test getLinkedAccountInfoList when subType is CHECKING, AccountType is 01, then return LinkedAccountInfo list")
    public void testGetLinkedAccountInfoList_whenSubTypeEqualsCHECKING_AccountTypeIsZeroOne_thenReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING", "e123123123123"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1));
        accountCodeEntities.add(accountCode);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test getLinkedAccountInfoList when subTypes are CHECKING and SAVINGS, return LinkedAccountInfo list")
    public void testGetLinkedAccountInfoList_whenSubTypesAreCheckingAndSavings_thenReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING", "e123123123123"));
        plaidAccounts.add(createPlaidAccount("SAVINGS", "e42323-23432-554a2"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e42323-23432-554a2", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1));
        AccountEntity accountCode2 = createAccountEntity("02", 2, createUserEntity(1));
        accountCodeEntities.add(accountCode);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfoList when system account type doesn't exist in plaid account subtype, then skip account with type and return linked account info list")
    public void testGetLinkedAccountInfoList_whenAccountTypeDNE_thenSkipAccountWithTypeAndReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING", "e123123123123"));
        plaidAccounts.add(createPlaidAccount("SAVINGS", "e42323-23432-554a2"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e42323-23432-554a2", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1));
        AccountEntity accountCode2 = createAccountEntity("03", 2, createUserEntity(1));
        accountCodeEntities.add(accountCode);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);
        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfoList when there's more account codes than plaid accounts, then return out of bounds exception")
    public void testGetLinkedAccountInfoList_whenMoreAccountCodesThanPlaidAccounts_thenReturnIndexOutOfBoundsException(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING", "e123123123123"));
        plaidAccounts.add(createPlaidAccount("SAVINGS", "e42323-23432-554a2"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e42323-23432-554a2", 2));

        List<AccountEntity> accountCodeEntities = new ArrayList<>();
        AccountEntity accountCode = createAccountEntity("01", 1, createUserEntity(1));
        AccountEntity accountCode1 = createAccountEntity("02", 2, createUserEntity(1));
        AccountEntity accountCode2 = createAccountEntity("03", 2, createUserEntity(1));
        accountCodeEntities.add(accountCode);
        accountCodeEntities.add(accountCode1);
        accountCodeEntities.add(accountCode2);

        when(accountService.getListOfAccountsByUserID(1)).thenReturn(accountCodeEntities);

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test Process Plaid Account By SubType when subType is empty or null then throw exception")
    public void testProcessAccountBySubType_whenSubTypeIsEmptyOrNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () ->
                plaidAccountToSystemAccountMapper.processPlaidAccountBySubType(createPlaidAccount("", "e123123123123"), createAccountEntity("01", 1, createUserEntity(1))));
    }

    @Test
    @DisplayName("Test Process Plaid Account by subtype when subtype is CHECKING, then return LinkedAccountInfo list")
    public void testProcessAccountBySubType_whenSubtypeEqualsCHECKING_thenReturnLinkedAccountInfoList(){
        PlaidAccount plaidAccount = createPlaidAccount("CHECKING", "e123123123123");
        AccountEntity account = createAccountEntity("01", 1, createUserEntity(1));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.processPlaidAccountBySubType(plaidAccount, account);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

    @Test
    @DisplayName("Test Process Plaid Account By Subtype when subtype is SAVINGS, then return LinkedAccountInfo list")
    public void testProcessAccountBySubType_whenSubtypeEqualsSAVINGS_thenReturnLinkedAccountInfoList(){
        PlaidAccount plaidAccount = createPlaidAccount("SAVINGS", "e123123123123");
        AccountEntity accountCode = createAccountEntity("02", 2, createUserEntity(1));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<LinkedAccountInfo> actual = plaidAccountToSystemAccountMapper.processPlaidAccountBySubType(plaidAccount, accountCode);
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++) {
            assertEquals(expected.get(i).getExternalAcctID(), actual.get(i).getExternalAcctID());
            assertEquals(expected.get(i).getSystemAcctID(), actual.get(i).getSystemAcctID());
        }
    }

   @Test
   @DisplayName("Test processPlaidAccountBySubType when account type is checking for plaid sub type checking, then return LinkedAccountInfoList")
   public void testProcessPlaidAccountsBySubType_whenAccountTypeIsCHECKING_thenReturnLinkedAccountInfoList(){

   }

   @Test
   @DisplayName("Test import PlaidDataToSystemAccount when plaid account is null, then throw exception")
   public void testImportPlaidDataToSystemAccount_whenPlaidAccountIsNull_thenThrowException(){
        Account account = mock(Account.class);
        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.importPlaidDataToSystemAccount(null, account);
        });
   }

   @Test
   @DisplayName("Test importPlaidDataToSystemAccount when account data is null, then throw exception")
   public void testImportPlaidDataToSystemAccount_whenAccountDataIsNull_thenThrowException(){
        PlaidAccount plaidAccount = mock(PlaidAccount.class);
        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.importPlaidDataToSystemAccount(plaidAccount, null);
        });
   }

   @Test
   @DisplayName("Test importPlaidDataToSystemAccount when both account data and plaid data are null, then throw exception")
   public void testImportPlaidDataToSystemAccount_whenAccountDataAndPlaidDataAreNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> plaidAccountToSystemAccountMapper.importPlaidDataToSystemAccount(null, null));
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

       AccountEntity accountEntity = createAccountEntity("checking", 1, createUserEntity(1));
       when(userDataManager.findById(1)).thenReturn(createUserEntity(1));
       when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenReturn(accountEntity);
       doNothing().when(accountService).save(accountEntity);

        PlaidImportResult expected = new PlaidImportResult(account, true);

        PlaidImportResult actual = plaidAccountToSystemAccountMapper.importPlaidDataToSystemAccount(plaidAccount, account);
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

       AccountEntity accountEntity = createAccountEntity("checking", 1, createUserEntity(1));
       when(userDataManager.findById(1)).thenReturn(createUserEntity(1));
       when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenReturn(accountEntity);
       doNothing().when(accountService).save(accountEntity);

       PlaidImportResult expected = new PlaidImportResult(account, true);

       PlaidImportResult actual = plaidAccountToSystemAccountMapper.importPlaidDataToSystemAccount(plaidAccount, account);
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

       when(userDataManager.findById(1)).thenReturn(createUserEntity(1));
       when(accountService.buildAccountEntityByAccountModel(account, createAccountCodeEntity(), createUserEntity(1))).thenThrow(new IllegalArgumentException(""));

       assertThrows(IllegalArgumentException.class, () -> {
           plaidAccountToSystemAccountMapper.importPlaidDataToSystemAccount(plaidAccount, account);
       });
   }

   @Test
   @DisplayName("test validate AccountSubTypeToTypeCriteria when account list is null/empty, then throw exception")
   public void testValidateAccountSubTypeToTypeCriteria_whenAccountListIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.validateAccountSubTypeToTypeCriteria(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.validateAccountSubTypeToTypeCriteria(Collections.emptyList());
        });
   }

   @Test
   @DisplayName("test validate AccountSubTypeToTypeCriteria when account list is valid, subType and type are empty, then throw exception")
   public void testValidateAccountSubTypeToTypeCriteria_whenAccountListIsValid_AndSubTypeAndTypeAreEmpty_thenThrowException(){

        AccountEntity accountEntity = new AccountEntity();;
        accountEntity.setAcctID(1);
        accountEntity.setAccountName("Alex's Checking");
        accountEntity.setAccountType("");
        accountEntity.setSubtype("");
        accountEntity.setBalance(BigDecimal.valueOf(1200));

        List<AccountEntity> accountEntities = Collections.singletonList(accountEntity);

        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.validateAccountSubTypeToTypeCriteria(accountEntities);
        });
   }

   @Test
   @DisplayName("Test validate AccountSubTypeToTypeCriteria when subtype and type are valid, then return true")
   public void testValidateAccountSubTypeToTypeCriteria_whenSubTypeAndTypeAreValid_thenReturnTrue(){
       AccountEntity accountEntity = new AccountEntity();;
       accountEntity.setAcctID(1);
       accountEntity.setAccountName("Alex's Checking");
       accountEntity.setAccountType("01");
       accountEntity.setType("depository");
       accountEntity.setSubtype("checking");
       accountEntity.setBalance(BigDecimal.valueOf(1200));

       List<AccountEntity> accountEntities = Collections.singletonList(accountEntity);

       Boolean result = plaidAccountToSystemAccountMapper.validateAccountSubTypeToTypeCriteria(accountEntities);
       assertTrue(result);
   }

   @Test
   @DisplayName("Test validate AccountSubTypeToTypeCriteria when account entity is null, then skip null account and return true")
   public void testValidateAccountSubTypeToTypeCriteria_whenAccountEntityIsNull_thenSkipAccountAndReturnTrue(){
       AccountEntity accountEntity = new AccountEntity();;
       accountEntity.setAcctID(1);
       accountEntity.setAccountName("Alex's Checking");
       accountEntity.setAccountType("01");
       accountEntity.setType("depository");
       accountEntity.setSubtype("checking");
       accountEntity.setBalance(BigDecimal.valueOf(1200));

       List<AccountEntity> accountEntityList = new ArrayList<>();
       accountEntityList.add(accountEntity);
       accountEntityList.add(null);
       accountEntityList.add(accountEntity);

       Boolean result = plaidAccountToSystemAccountMapper.validateAccountSubTypeToTypeCriteria(accountEntityList);
       assertTrue(result);
   }

   @Test
   @DisplayName("Test importPlaidTransactionToSystem when plaid transaction is null then throw exception")
   public void testImportPlaidTransactionToSystem_whenPlaidTransactionIsNull_thenThrowException(){
        assertThrows(IllegalArgumentException.class, () -> plaidAccountToSystemAccountMapper.importPlaidTransactionToSystem(null));
   }

   @Test
   @DisplayName("Test ImportPlaidTransactionToSystem when plaid transaction has invalid parameters, then throw exception")
   public void testImportPlaidTransactionToSystem_whenPlaidTransactionHasInvalidParameters_thenThrowException(){
       PlaidTransaction transaction = new PlaidTransaction();
        transaction.setTransactionName("Test Transaction");
        transaction.setAccountId(null);
        transaction.setDate(null);
        transaction.setAmount(null);
        transaction.setPending(false);

        assertThrows(IllegalArgumentException.class, () -> plaidAccountToSystemAccountMapper.importPlaidTransactionToSystem(transaction));
   }

   @Test
   @DisplayName("Test ImportPlaidTransactionToSystem when plaid transaction is valid, then return PlaidImportResult")
   public void testImportPlaidTransactionToSystem_whenPlaidTransactionIsValid_thenReturnPlaidImportResult(){

       PlaidTransactionImport plaidTransactionImport = new PlaidTransactionImport();
       plaidTransactionImport.setTransactionDate(createPlaidTransaction().getDate());
       plaidTransactionImport.setTransactionId(createPlaidTransaction().getTransactionId());
       plaidTransactionImport.setReferenceNumber("12121212");
       plaidTransactionImport.setAmount(BigDecimal.valueOf(1200));
       plaidTransactionImport.setPending(false);
       plaidTransactionImport.setTransactionName("Test Transaction");
       plaidTransactionImport.setPosted(LocalDate.now());



   }

   private PlaidTransaction createPlaidTransaction(){
       PlaidTransaction transaction = new PlaidTransaction();
       transaction.setTransactionName("Test Transaction");
       transaction.setAccountId("e12123123123");
       transaction.setDate(LocalDate.of(2024, 6, 14));
       transaction.setAmount(BigDecimal.valueOf(120));
       transaction.setPending(false);
       return transaction;
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

    private AccountEntity createAccountEntity(String acctType, int acctID, UserEntity user)
    {
        return AccountEntity.builder()
                .accountType(acctType)
                .acctID(acctID)
                .user(user)
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

    private PlaidAccount createPlaidAccount(String subtype, String acctID)
    {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId(acctID);
        plaidAccount.setName("Checking");
        plaidAccount.setSubtype(subtype);
        plaidAccount.setAvailableBalance(BigDecimal.valueOf(1100));
        return plaidAccount;
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

    private UserEntity createUserEntity(int userID)
    {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserID(userID);
        return userEntity;
    }

    @AfterEach
    void tearDown() {
    }
}