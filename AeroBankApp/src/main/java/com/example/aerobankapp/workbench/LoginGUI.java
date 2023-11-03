package com.example.aerobankapp.workbench;


import com.example.aerobankapp.workbench.controllers.fxml.LoginController;
import com.example.aerobankapp.workbench.home.Home;
import com.example.aerobankapp.workbench.model.Login;
import com.example.aerobankapp.workbench.utilities.Loader;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;


public class LoginGUI extends Application
{
    private static TextField usernameField = null;
    private static PasswordField passwordField = null;
    private Label usernameLabel = null;
    private Label passwordLabel = null;
    private HBox textBox = null;
    private Text welcomeText = null;
    private static Button signIn = null;
    private Button signUp = null;
    private static Text loginAlert = null;

    private static String textStyle = "-fx-font-size: 32px;\n" +
            "    -fx-font-family: \"Arial Black\";\n" +

            "    -fx-fill: #818181;\n" +
            "    -fx-effect: innershadow( three-pass-box, rgba(0, 0, 0, 0.7), 6, 0.0, 0, 2);";



    private AeroLogger aeroLogger;

    @Override
    public void start(Stage stage) throws Exception
    {

        GridPane paneForText = new GridPane();

        // Set the properties for our VBox
        paneForText.setAlignment(Pos.CENTER);
        paneForText.setPadding(new Insets(10.5, 11.5, 12.5, 13.5));

        HBox paneForButton = new HBox();

        // Set the properties for our HBox
        paneForButton.setAlignment(Pos.CENTER);
        paneForButton.setSpacing(5.5);

        // Add the Login and Create Account buttons to our HBox
        paneForButton.getChildren().addAll(getSignIn(), getSignUp());

        paneForText.add(getUsernameLabel(), 0, 0);
        paneForText.add(getUsernameField(), 1, 0);
        paneForText.add(getPasswordLabel(), 0, 2);
        paneForText.add(getPasswordField(), 1, 2);
        paneForText.add(paneForButton, 1, 3 );
        //paneForText.add(getViewPasswordCheckBox(), 1, 2);
        paneForText.add(getLoginAlert(), 1, 4);

        getUsernameField().setPromptText("Enter User Name");
        getPasswordField().setPromptText("Enter Password");
        getPasswordField().setPrefSize(2,2);

        BorderPane pane = new BorderPane();
        pane.setTop(getTextBox());
        pane.setCenter(paneForText);
        pane.setBottom(paneForButton);

        // Set ID's
        getPasswordField().setId("passwordField");
        getUsernameField().setId("usernameField");
        getSignIn().setId("signInBtn");
        getSignUp().setId("signUpBtn");

        pane.getStylesheets().add(getCSSAsString("/background.css"));

        getSignIn().setOnAction(e -> {
            try
            {
                String username = usernameField.getText();
                String password = passwordField.getText();
                Login login = new Login(username, password);
                LoginController loginController = new LoginController(login);
                loginController.authenticateLogin();

                // Make a POST request to the authentication endpoint

            }catch(Exception ex)
            {

            }
        });

        getPasswordField().setOnKeyPressed(new EventHandler<KeyEvent>() {


            @Override
            public void handle(KeyEvent keyEvent)
            {
                if(keyEvent.getCode() == KeyCode.ENTER)
                {
                    try{
                    //    Login(stage);
                    }catch(Exception e)
                    {
                      //  AeroLogger.handleException(e);

                    }
                }
            }
        });


        Scene scene = new Scene(pane, 410, 260);
        stage.setScene(scene);
        stage.show();

    }

    public String getCSSAsString(String path){
        String cssStr = "";
        URL URL = getClass().getResource(path);
        if(URL != null){
            cssStr = URL.toExternalForm();
        }
        return cssStr;
    }

    public Text getLoginAlert()  {
        if(loginAlert == null){
            loginAlert = new Text();
            loginAlert.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
        }
        return loginAlert;
    }

    public TextField getUsernameField(){
        if(usernameField == null){
            usernameField = new TextField();
            usernameField.getStylesheets().add(getCSSAsString("/textfield.css"));
            usernameField.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
            usernameField.setId("username");

        }
        return usernameField;
    }

    private HBox getTextBox(){
        if(textBox == null){
            textBox = new HBox();
            textBox.getChildren().add(getWelcomeText());
            textBox.setAlignment(Pos.CENTER);
        }
        return textBox;
    }

    public Text getWelcomeText(){
        if(welcomeText == null){
            welcomeText = new Text("Welcome to \n\tAeroBank");
            welcomeText.setStyle(textStyle);
            welcomeText.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
        }
        return welcomeText;
    }

    public Button getSignIn(){
        if(signIn == null){
            signIn = new Button("Sign In");
            signIn.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
            signIn.getStylesheets().add(getCSSAsString("/button.css"));
        }

        return signIn;
    }


    public Button getSignUp(){
        if(signUp == null){
            signUp = new Button("Sign Up");
            signUp.getStylesheets().add(getCSSAsString("/button.css"));
            signUp.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
            signUp.setOnAction(e -> {
                try{
                   // Register();
                }catch(Exception ex){
                  //  AeroLogger.handleException(ex);
                }
            });
        }
        return signUp;
    }

    public Label getUsernameLabel(){
        if(usernameLabel == null){
            usernameLabel = new Label("User Name: ");
            usernameLabel.getStylesheets().add(getCSSAsString("/label.css"));
            usernameLabel.setFont(Font.font("Sans Serif", FontWeight.BOLD, 16));
        }
        return usernameLabel;
    }

    private Label getPasswordLabel(){
        if(passwordLabel == null){
            passwordLabel = new Label("Password: ");
            passwordLabel.getStylesheets().add(getCSSAsString("/label.css"));
            passwordLabel.setFont(Font.font("Sans Serif", FontWeight.BOLD, 16));
        }
        return passwordLabel;
    }

    public PasswordField getPasswordField(){
        if(passwordField == null){
            passwordField = new PasswordField();
            passwordField.getStylesheets().add(getCSSAsString("/textfield.css"));
            passwordField.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
            passwordField.setId("password");
        }
        return passwordField;
    }

}
