package com.example.aerobankapp.controllers;

import com.example.aerobankapp.configuration.AppConfig;
import com.example.aerobankapp.configuration.JpaConfig;
import com.example.aerobankapp.entity.AccountEntity;
import com.example.aerobankapp.entity.AccountPropertiesEntity;
import com.example.aerobankapp.services.AccountCodeService;
import com.example.aerobankapp.services.AccountNotificationService;
import com.example.aerobankapp.services.AccountPropertiesService;
import com.example.aerobankapp.services.AccountServiceImpl;
import com.example.aerobankapp.workbench.utilities.response.AccountResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value=AccountController.class, excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.session.SessionAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class,
        org.springframework.session.jdbc.config.annotation.web.http.JdbcHttpSessionConfiguration.class
})
@RunWith(SpringRunner.class)
@Import({AppConfig.class, JpaConfig.class})
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountServiceImpl accountDAO;

    @MockBean
    private AccountPropertiesService accountPropertiesService;

    @MockBean
    private AccountNotificationService accountNotificationService;

    @MockBean
    private AccountCodeService accountCodeService;

    private AccountController accountController;


    @BeforeEach
    void setUp() {
    }


    private static List<AccountResponse> getAccountResponseList(List<AccountPropertiesEntity> accountProperties, List<AccountEntity> entityList, BigDecimal pending, BigDecimal available)
    {
        List<AccountResponse> accountResponseList = new ArrayList<>();

        // Map for quick lookup of AccountProperties by AccountEntity ID
        Map<Long, AccountPropertiesEntity> propertiesMap = new HashMap<>();
        for (AccountPropertiesEntity prop : accountProperties) {
            if (prop.getAccount() != null) {
                propertiesMap.put((long) prop.getAccount().getAcctID(), prop);
            }
        }

        for (AccountEntity entity : entityList) {
            BigDecimal balance = entity.getBalance();
           // String acctCode = entity.getAccountCode();
            String accountName = entity.getAccountName();

            // Lookup account properties using AccountEntity ID
            AccountPropertiesEntity prop = propertiesMap.get(entity.getAcctID());
            String acctColor = prop != null ? prop.getAcct_color() : null;

            String acctImage = prop != null ? prop.getImage_url() : null; // Use image_url as acctImage

            String acctCode = "";
            AccountResponse accountResponse = new AccountResponse(
                    acctCode, balance, pending, available, accountName, acctColor, acctImage);
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




    @ParameterizedTest
    @WithMockUser
    @CsvSource({"-1, A123",
                "0, A325",
                "-1, A327"})
    public void testGetAccountIDByUserIDAndAccountCode_IncorrectValues(String userID, String accountCode) throws Exception {
        mockMvc.perform(get("/api/accounts/{userID}/{accountCode}", userID, accountCode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // or any other expected status code
    }

    @ParameterizedTest
    @WithMockUser
    @CsvSource({
            "1, 1L, 1",
            "1, 2L, 2",
            "2, 4L, 4",
            "1, 3L, 3",
            "-1, 4L, -1", // Assuming -1 indicates an error/invalid response
            "1, , -1"
    })
    public void testGetAccountIDByUserIDAndAccountCode_VariousInputs(int userID, Long accountCode, int expectedAccountID) throws Exception {
        if (expectedAccountID != -1) {
            when(accountDAO.getAccountIDByAcctCodeAndUserID(userID, accountCode)).thenReturn(expectedAccountID);
        }

        ResultActions resultActions = mockMvc.perform(get("/api/accounts/{userID}/{accountCode}", userID, accountCode)
                .contentType(MediaType.APPLICATION_JSON));

        if (expectedAccountID == -1) {
            // Error case: Invalid inputs or server error
            resultActions.andExpect(status().isNotFound()); // or isInternalServerError(), depending on your logic
        } else {
            // Success case: Valid account ID
            resultActions.andExpect(status().isOk())
                    .andExpect(content().json("{\"accountID\":" + expectedAccountID + "}"));
        }
    }

    @AfterEach
    void tearDown() {
    }
}