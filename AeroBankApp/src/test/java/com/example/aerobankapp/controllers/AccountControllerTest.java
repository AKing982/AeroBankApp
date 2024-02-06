package com.example.aerobankapp.controllers;

import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value=AccountController.class)
@RunWith(SpringRunner.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountDAO;

    private AccountController accountController;


    @BeforeEach
    void setUp() {
    }

    @Test
    @WithMockUser
    public void whenGetUserAccounts_thenReturnListofAccounts() throws Exception
    {
        AccountEntity accountEntity = AccountEntity.builder()
                .accountCode("A1")
                .accountType("Checking")
                .isRentAccount(false)
                .userID(1)
                .acctID(1)
                .balance(new BigDecimal("4500"))
                .hasDividend(false)
                .hasMortgage(false)
                .interest(new BigDecimal("1.67"))
                .aSecID(0)
                .build();

        List<AccountEntity> accountEntityList = Collections.singletonList(accountEntity);
     //   List<AccountResponse> accountResponseList = getAccountResponseList(accountEntityList, new BigDecimal("1200"), new BigDecimal("1150"));
        String username = "AKing94";

        given(accountDAO.findByUserName(username)).willReturn(accountEntityList);

        mockMvc.perform(get("/api/accounts/data/{user}", username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].accountCode", is(accountEntity.getAccountCode())));

    }

    private List<AccountResponse> getAccountResponseList(List<AccountEntity> entityList, BigDecimal pending, BigDecimal available)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();
        for(AccountEntity entity : entityList)
        {
            BigDecimal balance = entity.getBalance();
            String acctCode = entity.getAccountCode();
            String acctColor = entity.getAcct_color();
            AccountResponse accountResponse = new AccountResponse(acctCode, balance, pending, available, acctColor);
            accountResponseList.add(accountResponse);
        }

        return accountResponseList;
    }

    @Test
    @WithMockUser
    public void whenGetAccountCodes_thenReturnListOfAccountCodes() throws Exception
    {
        String user = "AKing94";

        List<String> accountCodes = new ArrayList<>();
        accountCodes.add("A1");
        accountCodes.add("A2");
        accountCodes.add("A3");

        given(accountDAO.getListOfAccountCodes(user)).willReturn(accountCodes);

        mockMvc.perform(get("/api/accounts/data/codes/{user}", user)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].accountCode", is("A1")));


    }

    @Test
    @WithMockUser
    public void whenGetAccountTypeMapByAccountID_thenReturnMap() throws Exception {
        String userName = "AKing94";
        Map<Integer, String> accountTypeMap = new HashMap<>();
        accountTypeMap.put(1, "Checking");
        accountTypeMap.put(2, "Savings");

        // Mock the behavior of accountDAO to return the accountTypeMap when getAccountTypeMapByAccountId is called
        given(accountDAO.getAccountTypeMapByAccountId(userName)).willReturn(accountTypeMap);

        // Perform the request and verify the response
        mockMvc.perform(get("/api/accounts/{userName}/account-types", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.1", is("Checking")))
                .andExpect(jsonPath("$.2", is("Savings")));
    }


    @AfterEach
    void tearDown() {
    }
}