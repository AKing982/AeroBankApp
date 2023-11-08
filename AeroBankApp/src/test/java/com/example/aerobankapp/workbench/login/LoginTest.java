package com.example.aerobankapp.workbench.login;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
class LoginTest extends ApplicationTest {

    private Login login;

    @BeforeEach
    void setUp() throws Exception
    {
        MockitoAnnotations.openMocks(this);
        ApplicationTest.launch(Login.class);
        Platform.runLater(() -> {
            login = new Login();
            try {
                login.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            login.getUserNameField().setId("usernameField");

        });

    }

    @Test
    public void testUserNameTextField(FxRobot robot)
    {
        Button mockSignIn = mock(Button.class);
        TextField usernameField = mock(TextField.class);
        robot.clickOn("#usernameField").write("AKing94");

    }

    @AfterEach
    void tearDown() throws TimeoutException
    {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        login.getUserNameField().clear();
    }
}