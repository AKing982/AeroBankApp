package com.example.aerobankapp.workbench.plaid;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountCodeEntity;
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
    private PlaidAccountToSystemAccountMapperImpl plaidAccountToSystemAccountMapper;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountCodeService accountCodeService;

    @Mock
    private ExternalAccountsService externalAccountsService;

    @BeforeEach
    void setUp() {
        plaidAccountToSystemAccountMapper = new PlaidAccountToSystemAccountMapperImpl(accountService, accountCodeService, externalAccountsService);
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
        plaidAccounts.add(createPlaidAccount("checking"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode1 = createAccountCodeEntity("01", 1, createUserEntity(1));
        AccountCodeEntity accountCode2 = createAccountCodeEntity("02", 2, createUserEntity(2));
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
        plaidAccounts.add(createPlaidAccount("checking"));
        plaidAccounts.add(null);
        plaidAccounts.add(createPlaidAccount("checking"));
        plaidAccounts.add(null);

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode1 = createAccountCodeEntity("01", 1, createUserEntity(1));
        AccountCodeEntity accountCode2 = createAccountCodeEntity("02", 2, createUserEntity(2));
        accountCodeEntities.add(accountCode1);
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
    @DisplayName("Test GetLinkedAccountInfo when account Code entity is null, then skip null account code and return LinkedAccountInfo list")
    public void testGetLinkedAccountInfo_whenAccountCodeEntityIsNull_thenSkipNullAccountCodeEntityAndReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("checking"));
        plaidAccounts.add(createPlaidAccount("checking"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode2 = createAccountCodeEntity("02", 2, createUserEntity(2));
        accountCodeEntities.add(null);
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
    @DisplayName("Test GetLinkedAccountInfoList when plaid account sub type is empty/null then throw exception")
    public void testGetLinkedAccountInfoList_whenPlaidAccountSubTypeIsEmptyOrNull_thenThrowException()
    {
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccountWithEmptySubType());

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode2 = createAccountCodeEntity("02", 2, createUserEntity(1));
        AccountCodeEntity accountCode = createAccountCodeEntity("01", 1, createUserEntity(1));
        accountCodeEntities.add(accountCode);
        accountCodeEntities.add(accountCode2);

        when(accountCodeService.getAccountCodesListByUserID(1)).thenReturn(accountCodeEntities);

        assertThrows(IllegalArgumentException.class, () -> {
            plaidAccountToSystemAccountMapper.getLinkedAccountInfoList(createUserEntity(1), plaidAccounts);
        });
    }

    @Test
    @DisplayName("Test GetLinkedAccountInfoList when subType is valid and is checking type then return linkedAccountList")
    public void testGetLinkedAccountInfoList_whenSubTypeIsValidAndIsCheckingType_thenReturnLinkedAccountList()
    {
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode = createAccountCodeEntity("01", 1, createUserEntity(1));
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
    @DisplayName("Test getLinkedAccountInfoList when subType is SAVINGS type then return linkedAccountList")
    public void testGetLinkedAccountInfoList_whenSubTypeEqualsSAVINGS_type_thenReturnLinkedAccountList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("SAVINGS"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 2));

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode = createAccountCodeEntity("02", 2, createUserEntity(1));
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
    @DisplayName("Test getLinkedAccountInfoList when subType is CHECKING, AccountType is 01, then return LinkedAccountInfo list")
    public void testGetLinkedAccountInfoList_whenSubTypeEqualsCHECKING_AccountTypeIsZeroOne_thenReturnLinkedAccountInfoList(){
        List<PlaidAccount> plaidAccounts = new ArrayList<>();
        plaidAccounts.add(createPlaidAccount("CHECKING"));

        List<LinkedAccountInfo> expected = new ArrayList<>();
        expected.add(createLinkedAccountInfo("e123123123123", 1));

        List<AccountCodeEntity> accountCodeEntities = new ArrayList<>();
        AccountCodeEntity accountCode = createAccountCodeEntity("01", 1, createUserEntity(1));
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


    private AccountCodeEntity createAccountCodeEntity(String acctType, int acctID, UserEntity user)
    {
        return AccountCodeEntity.builder()
                .accountType(acctType)
                .account_segment(acctID)
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

    private PlaidAccount createPlaidAccount(String subtype)
    {
        PlaidAccount plaidAccount = new PlaidAccount();
        plaidAccount.setCurrentBalance(BigDecimal.valueOf(1200));
        plaidAccount.setAccountId("e123123123123");
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