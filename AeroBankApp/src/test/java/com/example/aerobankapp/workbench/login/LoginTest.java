package com.example.aerobankapp.workbench.login;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

@ExtendWith(ApplicationExtension.class)
class LoginTest extends ApplicationTest {

    private Login login;

    @Override
    public void start(Stage stage) throws Exception
    {
        Button signIn = new Button();
        Button register = new Button();
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10.5, 11.5, 12.5, 13.5));
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5.5);
        buttonBox.getChildren().addAll(signIn, register);
    }

    @BeforeEach
    void setUp()
    {
        login = new Login();
    }

    @Test
    public void testUserNameTextField()
    {
        String expectedUserName = "AKing94";

        clickOn("#usernameField");
        verifyThat("#usernameField", LabeledMatchers.hasText("AKing94"));
        String actualUserName = login.getUserNameField().getText();


        assertEquals(expectedUserName, actualUserName);
    }

    @AfterEach
    void tearDown() {
    }
}