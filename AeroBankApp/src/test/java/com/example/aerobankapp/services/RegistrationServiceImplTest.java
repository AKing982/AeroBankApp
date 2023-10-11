package com.example.aerobankapp.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private RegistrationRepository registrationRepository;

    @BeforeEach
    void setUp()
    {
        registrationRepository = mock(RegistrationRepository.class);
        registrationService = new RegistrationServiceImpl(registrationRepository);
    }

    @Test
    public void testFindAll()
    {
     //   Registration registration = new Registration("Alex", "King", "AKing94", "alex@utahkings.com", "3771 W Fontana Way", 84095, 59887, "Halflifer45!", BigDecimal.valueOf(1205), true);

      //  List<Registration> expectedData = Arrays.asList(registration);
        List<Registration> data = registrationService.findAll();
       // when(registrationRepository.findAll()).thenReturn(expectedData);

        verify(registrationService).findAll();
        assertNotNull(data);
    }

    @AfterEach
    void tearDown() {
    }
}