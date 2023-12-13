package com.example.aerobankapp.workbench.utilities.quartz;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class QuartzWriterTest {

    /** Define SUT **/
    @MockBean
    private QuartzWriter quartzWriter;

    @Autowired
    private QuartzDTO quartzDTO;

    @BeforeEach
    void setUp()
    {
        quartzWriter = new QuartzWriter(quartzDTO);
    }

    @Test
    public void testNull()
    {
        QuartzDTO nullQuartz = null;
        QuartzWriter quartzWriter1 = new QuartzWriter(nullQuartz);
        QuartzDTO quartz = quartzWriter1.getQuartzDTO();

        assertNotNull(quartzWriter1);
        assertNotNull(quartz);
        assertNull(nullQuartz);
    }



    @AfterEach
    void tearDown() {
    }
}