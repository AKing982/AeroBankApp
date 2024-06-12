package com.example.aerobankapp.repositories;

import com.example.aerobankapp.configuration.JpaConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BillPaymentRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void shouldFindBillPaymentDueDate(){

    }

    @AfterEach
    void tearDown() {
    }
}