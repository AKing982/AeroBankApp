package com.example.aerobankapp.workbench.passwordSkin;

import javafx.application.Platform;
import javafx.scene.control.PasswordField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class ShowPasswordFieldTest {

    private ShowPasswordField showPasswordField;
    private PasswordField passwordField = new PasswordField();


    @BeforeAll
    public static void initJFX()
    {
        System.setProperty("javafx.headless", "true");
       // new JFXPanel();
    }

    @BeforeEach
    void setUp()
    {
        Platform.runLater(() -> {
            showPasswordField = new ShowPasswordField(passwordField);
        });

    }

    @Test
    public void testMaskTest()
    {
        Platform.runLater(() -> {
            String text = "Halflifer45!";

            String masked = showPasswordField.maskText(text);
            assertEquals(text, masked);

        });



    }

    @AfterEach
    void tearDown() {
    }
}