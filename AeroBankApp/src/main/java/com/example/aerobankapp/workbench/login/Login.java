package com.example.aerobankapp.workbench.login;

import com.example.aerobankapp.messages.CommonLabels;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Slf4j
@Component
@Setter
public class Login extends Application
{

    private static TextField usernameField;
    private static PasswordField passwordField;
    private static Button signIn;
    private Button registerBtn;
    private Label usernameLabel;
    private Label passwordLabel;
    private Text welcomeText;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(10.5, 11.5, 12.5, 13.5));

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5.5);

        buttonBox.getChildren().addAll(getSignIn(), getRegisterBtn());

        pane.add(getUsernameLabel(), 0, 0);
        pane.add(getUserNameField(), 1, 0);
        pane.add(getPasswordLabel(), 0, 2);
        pane.add(getPasswordField(),1 ,2);

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
            usernameField.setId("usernameField");
        }
        return usernameField;
    }

    public Button getRegisterBtn()
    {
        if(registerBtn == null)
        {
            registerBtn = new Button();
        }
        return registerBtn;
    }

    public Button getSignIn()
    {
        if(signIn == null)
        {
            signIn = new Button();
        }
        return signIn;
    }

    public Label getUsernameLabel()
    {
        if(usernameLabel == null)
        {
            usernameLabel = new Label(CommonLabels.USER_NAME);
            usernameLabel.getStylesheets().add(CommonLabels.LABEL_CSS);
            usernameLabel.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.BOLD, 16));
        }
        return usernameLabel;
    }

    public Label getPasswordLabel()
    {
        if(passwordLabel == null)
        {
            passwordLabel = new Label(CommonLabels.PASSWORD);
            passwordLabel.getStylesheets().add(CommonLabels.LABEL_CSS);
            passwordLabel.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.BOLD, 16));
        }
        return passwordLabel;
    }

    public PasswordField getPasswordField()
    {
        if(passwordField == null)
        {
            passwordField = new PasswordField();
            passwordField.getStylesheets().add(CommonLabels.PASSWORD_CSS);
            passwordField.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
            passwordField.setId("password");
        }
        return passwordField;
    }

}
