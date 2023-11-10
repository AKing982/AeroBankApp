package com.example.aerobankapp.services;

import com.example.aerobankapp.model.UserLogModel;
import com.example.aerobankapp.repositories.UserLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserLogServiceImplTest {

    @InjectMocks
    private UserLogServiceImpl userLogService;

    @Mock
    private UserLogRepository userLogRepository;

    @BeforeEach
    void setUp() {
        userLogRepository = mock(UserLogRepository.class);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll()
    {
        List<UserLogModel> userLogModelList = Arrays.asList(new UserLogModel(), new UserLogModel());
        userLogService = mock(UserLogServiceImpl.class);
       // when(userLogRepository.findAll()).thenReturn(userLogModelList);

      //  List<UserLogModel> result = userLogService.findAll();

     //   assertNotEquals(userLogModelList, result);
//        verify(userLogService.findAll());
    }

    @Test
    public void testFindAllWithConstructor()
    {
     //   UserLogServiceImpl userLogService1 = new UserLogServiceImpl();

        List<UserLogModel> userLogModelList = Arrays.asList(new UserLogModel());
       // List<UserLogModel> actualList = userLogService1.findAll();

       // assertNotEquals(userLogModelList,actualList);
    }

    @AfterEach
    void tearDown() {
    }
}