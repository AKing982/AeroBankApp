package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountCodeEntity;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.UserEntity;
import com.example.aerobankapp.exceptions.InvalidUserIDException;
import com.example.aerobankapp.model.LinkedAccountInfo;
import com.example.aerobankapp.model.PlaidAccount;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountService;
import com.example.aerobankapp.services.ExternalAccountsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import({JpaConfig.class, AppConfig.class})
class
PlaidAccountToSystemAccountMapperImplTest {

    @InjectMocks
    private PlaidAccountToSystemAccountImporterImpl plaidAccountToSystemAccountMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountCodeService accountCodeService;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @BeforeEach
    void setUp() {
        plaidAccountToSystemAccountMapper = new PlaidAccountToSystemAccountImporterImpl(accountService, accountCodeService, externalAccountsService);
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

        when(accountCodeService.getAccountCodesListByUserID(1)).thenReturn(accountCodeEntities);

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

        when(accountCodeService.getAccountCodesListByUserID(1)).thenReturn(accountCodeEntities);
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

        when(accountCodeService.getAccountCodesListByUserID(1)).thenReturn(accountCodeEntities);

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