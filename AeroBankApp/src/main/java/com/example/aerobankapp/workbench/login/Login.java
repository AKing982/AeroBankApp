package com.example.aerobankapp.workbench.login;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Slf4j
@Component
@Getter
@Setter
public class Login extends Application
{

    private static TextField usernameField;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);

        setUserNameFieldId("usernameField");

    }

    private void setUserNameFieldId(String id)
    {
        getUserNameField().setId("usernameField");
    }

    public TextField getUserNameField()
    {
        if(usernameField == null)
        {
            usernameField = new TextField();
        }
        return usernameField;
    }

}
