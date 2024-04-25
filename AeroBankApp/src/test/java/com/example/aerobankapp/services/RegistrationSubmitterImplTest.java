package com.example.aerobankapp.services;

import com.example.aerobankapp.dto.AccountInfoDTO;
import com.example.aerobankapp.dto.RegistrationDTO;
import com.example.aerobankapp.exceptions.InvalidUserStringException;
import com.example.aerobankapp.exceptions.NonEmptyListRequiredException;
import com.example.aerobankapp.exceptions.NullAccountInfoException;
import com.example.aerobankapp.model.AccountNumber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RegistrationSubmitterImplTest {

    @InjectMocks
    private RegistrationSubmitterImpl registrationSubmitter;

    @Mock
    private UserService userService;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountSecurityService accountSecurityService;

    @Mock
    private AccountPropertiesService accountPropertiesService;

    private RegistrationDTO registrationDTO;



    @BeforeEach
    void setUp() {
        registrationSubmitter = new RegistrationSubmitterImpl(userService, accountService, accountSecurityService, accountPropertiesService);

        registrationDTO = new RegistrationDTO("Alex", "King", "AKing94", "alex@utahkings.com", new ArrayList<>(), new ArrayList<>(), "5988", true, "Halflifer45");
    }

    @Test
    public void testValidateGeneratedAccountNumber_InvalidAccountNumber(){
        final AccountNumber accountNumber = new AccountNumber(0, 0, 0);

       // boolean isValidAccountNumber = registrationSubmitter.validateGeneratedAccountNumber(accountNumber);

      //  assertFalse(isValidAccountNumber);
    }

    @Test
    public void testValidateGeneratedAccountNumber_ValidAccountNumber(){
        final AccountNumber accountNumber = new AccountNumber(22, 55, 12);

      //  boolean isValidAccountNumber = registrationSubmitter.validateGeneratedAccountNumber(accountNumber);

      //  assertTrue(isValidAccountNumber);
    }

    @Test
    public void testValidatedGeneratedAccountNumber_ValidAccountNumber_NotFoundInDatabase(){
        final AccountNumber accountNumber = new AccountNumber(22, 55, 12);

      //  boolean isValid = registrationSubmitter.validateGeneratedAccountNumber(accountNumber);

      //  assertFalse(isValid);
    }

    @Test
    public void testGetGeneratedAccountNumber_EmptyUserName(){
        final String user = "";

        assertThrows(InvalidUserStringException.class, () -> {
      //      AccountNumber accountNumber = registrationSubmitter.getGeneratedAccountNumber(user);
        });
    }

    @Test
    public void testGetGeneratedAccountNumber_ValidUserName(){
        final String user = "AKing94";

        AccountNumber accountNumber = new AccountNumber(89, 42, 48);

    //    AccountNumber actualAccountNumber = registrationSubmitter.getGeneratedAccountNumber(user);

       // assertNotNull(actualAccountNumber);
      //  assertEquals(accountNumber.getAccountNumberToString(), actualAccountNumber.getAccountNumberToString());
      //  assertTrue(actualAccountNumber.isValid());
    }

    @Test
    public void testGetListOfAccountInfos_EmptyList(){
       assertThrows(NonEmptyListRequiredException.class, () -> {
           registrationSubmitter.getListOfAccountInfos(registrationDTO);
       });
    }

    @Test
    public void testGetListOfAccountInfos_ListWithNullData(){

        AccountInfoDTO accountInfoDTO = new AccountInfoDTO("Alex's Checking", null, null);
        List<AccountInfoDTO> accountInfoDTOList = List.of(accountInfoDTO);
        RegistrationDTO registrationDTO1 = new RegistrationDTO("Alex", "King", "AKing94", "alex@utahkings.com", accountInfoDTOList, new ArrayList<>(), "5988", true, "Halflifer45!");

        assertThrows(NullAccountInfoException.class, () -> {
            registrationSubmitter.getListOfAccountInfos(registrationDTO1);
        });
    }

    @Test
    public void testGetListOfAccountInfos_ListWithNonNullData(){
        AccountInfoDTO accountInfoDTO = new AccountInfoDTO("Alex's Checking", "Checking", new BigDecimal("6789"));
        List<AccountInfoDTO> accountInfoDTOList = List.of(accountInfoDTO);
        RegistrationDTO registrationDTO1 = new RegistrationDTO("Alex", "King", "AKing94", "alex@utahkings.com", accountInfoDTOList, new ArrayList<>(), "5988", true, "Halflifer45!");

        List<AccountInfoDTO> actualAccounts = registrationSubmitter.getListOfAccountInfos(registrationDTO1);

        assertNotNull(actualAccounts);
        assertFalse(actualAccounts.isEmpty());
        assertEquals(accountInfoDTOList, actualAccounts);
        assertEquals(1, actualAccounts.size());
    }

    @AfterEach
    void tearDown() {
    }
}