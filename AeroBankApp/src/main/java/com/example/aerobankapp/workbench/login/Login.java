package com.example.aerobankapp.workbench.login;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.model.UserProfileModel;
import com.example.aerobankapp.services.AuthenticationServiceImpl;
import com.example.aerobankapp.services.UserProfileService;
import com.example.aerobankapp.workbench.controllers.fxml.LoginController;
import com.example.aerobankapp.workbench.model.LoginModel;
import com.example.aerobankapp.workbench.utilities.UserProfile;
import com.example.aerobankapp.workbench.utilities.UserProfileCache;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

@Slf4j
@Component
@Setter
public class Login extends Application
{
    private static TextField usernameField;
    private static TextField passwordField;
    private static Button signIn;
    private Button registerBtn;
    private Label usernameLabel;
    private Label passwordLabel;
    private Text welcomeText;
    private HBox textBoxPane;
    private static Text loginAlert;
    private Hyperlink forgotPasswordLink;
    private CheckBox showPassword;
    private HBox checkPasswordBox;
    private UserProfile userProfile;
    private UserProfileModel userProfileModel;
    private UserProfileCache userProfileCache = new UserProfileCache();
    private final double BUTTON_HEIGHT = 20;
    private static String textStyle = "-fx-font-size: 32px;\n" +
            "   -fx-font-family: \"Arial Black\";\n" +
            "   -fx-fill: #818181;\n" +
            "   -fx-effect: innershadow( three-pass-box, rgba(0, 0, 0, 0.7), 6, 0.0, 0, 2);";

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Override
    public void start(Stage stage) throws Exception {
        GridPane pane = getGrid();
        HBox buttonBox = getButtonBox();
        BorderPane mother = getMotherPane(pane, buttonBox);

        getUserNameField().setPromptText("Enter User Name");
        getPasswordField().setPromptText("Enter Password");
        setPasswordFieldSize(getPasswordField());
        setMotherScene(mother);
        buttonAction(getSignIn(), stage);
        buttonAction(getRegisterBtn(), stage);


        Scene scene = getScene(mother, 410, 260);
        stage.setScene(scene);
        stage.show();
    }

    private HBox getCheckPasswordBox()
    {
        if(checkPasswordBox == null)
        {
            checkPasswordBox = new HBox();
            checkPasswordBox.getChildren().addAll(getForgotPasswordLink(), getShowPassword());
        }
        return checkPasswordBox;
    }

    private CheckBox getShowPassword()
    {
        if(showPassword == null)
        {
            showPassword = new CheckBox("Show Password");
            showPassword.setAlignment(Pos.CENTER_RIGHT);
            showPassword.getStylesheets().add(getCSSAsString("/label.css"));
            showPassword.setPadding(getInsets(0, 0, 0, 20));
        }
        return showPassword;
    }

    private void setPasswordFieldSize(TextField passwordField) {
        passwordField.setPrefSize(2, 2);
    }

    private void setMotherScene(BorderPane mother) {
        mother.getStylesheets().add(getCSSAsString("/background.css"));
    }

    private Scene getScene(BorderPane mother, double param, double param2) {
        return new Scene(mother, param, param2);
    }

    private GridPane getGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(getInsets(10.5, 11.5, 12.5, 13.5));

        grid.add(getUsernameLabel(), 0, 0);
        grid.add(getUserNameField(), 1, 0);
        grid.add(getPasswordLabel(), 0, 2);
        grid.add(getPasswordField(), 1, 2);
        grid.add(getCheckPasswordBox(), 1, 3);
        grid.add(getLoginAlert(), 1, 4);
        grid.add(getButtonBox(), 1, 5);

        return grid;
    }

    private Hyperlink getForgotPasswordLink() {
        if (forgotPasswordLink == null) {
            forgotPasswordLink = new Hyperlink("Forgot password");
        }
        return forgotPasswordLink;
    }

    private void showPassword(TextField passwordField)
    {
        String text = passwordField.getText();
        passwordField.setText(text);
        passwordField.clear();
        passwordField.setDisable(true);

    }

    private void hidePassword(TextField passwordField)
    {
        passwordField.setText(passwordField.getText());
        passwordField.clear();
        passwordField.setDisable(false);
    }

    private void checkBoxPasswordChange(final CheckBox checkBox, final TextField passwordField)
    {
        if(checkBox != null)
        {
            if(checkBox.isSelected())
            {
                showPassword(passwordField);
            }
            else
            {
                hidePassword(passwordField);
            }
        }

    }


    private ImageView getNewImage(String path, double height)
    {
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(height);
        return imageView;
    }


    private Text getLoginAlert()
    {
        if(loginAlert == null)
        {
            loginAlert = new Text();
            loginAlert.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
        }
        return loginAlert;
    }
    
    private HBox getButtonBox()
    {
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(5.5);
        buttonBox.getChildren().addAll(getSignIn(), getRegisterBtn());
        return buttonBox;
    }
    
    private BorderPane getMotherPane(GridPane pane, HBox buttonPane)
    {
        BorderPane mother = new BorderPane();
        mother.setTop(getTextBoxPane());
        mother.setCenter(pane);
        mother.setBottom(buttonPane);
        return mother;
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
            usernameField.getStylesheets().add(getCSSAsString("/textfield.css"));
            usernameField.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
            usernameField.setId("username");
        }
        return usernameField;
    }

    private Insets getInsets(double param1, double param2, double param3, double param4)
    {
        return new Insets(param1, param2, param3, param4);
    }

    private String getCSSAsString(String path)
    {
        URL url = getClass().getResource(path);
        if(url != null)
        {
            return url.toExternalForm();
        }
        return "";
    }

    public Button getRegisterBtn()
    {
        if(registerBtn == null)
        {
            registerBtn = new Button(CommonLabels.REGISTER);
            registerBtn.getStylesheets().add(getCSSAsString("/button.css"));
            registerBtn.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
            registerBtn.setId("register");
        }
        return registerBtn;
    }


    private void buttonAction(Button btn, Stage s)
    {
        try
        {
            if(btn.getId().equals("login"))
            {
                String username = getUserNameField().getText();
                String password = getPasswordField().getText();
                loginAction(username, password);
                closeStage(s);
            }
            else if(btn.getId().equals("register"))
            {

            }

        }catch(Exception e)
        {

        }
    }

     private void loginAction(String user, String password)
     {
         UserProfile userProfile = loadUserProfile(user);

         // TODO: Load the UserProfile data
         LoginModel loginModel = new LoginModel(user, password);

         // TODO: Execute Login ThreadPool Process
         LoginController loginController = new LoginController(authenticationService, loginModel);
     }

     private UserProfileModel getUserProfileModel(String user)
     {
         return new UserProfileModel(user);
     }

     private UserProfile loadUserProfile(String user)
     {
         this.userProfileModel = getUserProfileModel(user);
         this.userProfile = userProfileCache.getCachedProfileByUser(user);
         return userProfile;
     }

     private void registerAction()
     {

     }


    public Button getSignIn()
    {
        if(signIn == null)
        {
            signIn = new Button(CommonLabels.SIGN_IN);
            signIn.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
            signIn.getStylesheets().add(getCSSAsString("/button.css"));
            signIn.setId("login");
        }
        return signIn;
    }

    public Label getUsernameLabel()
    {
        if(usernameLabel == null)
        {
            usernameLabel = new Label(CommonLabels.USER_NAME);
            usernameLabel.getStylesheets().add(getCSSAsString(CommonLabels.LABEL_CSS));
            usernameLabel.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.BOLD, 16));
        }
        return usernameLabel;
    }

    public Label getPasswordLabel()
    {
        if(passwordLabel == null)
        {
            passwordLabel = new Label(CommonLabels.PASSWORD);
            passwordLabel.getStylesheets().add(getCSSAsString(CommonLabels.LABEL_CSS));
            passwordLabel.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.BOLD, 16));
        }
        return passwordLabel;
    }

    private HBox getTextBoxPane()
    {
        if(textBoxPane == null)
        {
            textBoxPane = new HBox();
            textBoxPane.getChildren().add(getWelcomeText());
            textBoxPane.setAlignment(Pos.CENTER);
        }
        return textBoxPane;
    }

    private Text getWelcomeText()
    {
        if(welcomeText == null)
        {
            welcomeText = new Text("Welcome to \n\tAeroBank");
            welcomeText.setStyle(textStyle);
            welcomeText.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
        }
        return welcomeText;
    }

    private void closeStage(Stage s)
    {
        s.close();
    }

    public TextField getPasswordField()
    {
        if(passwordField == null)
        {
            passwordField = new TextField();
            passwordField.getStylesheets().add(getCSSAsString("/textfield.css"));
            passwordField.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
            passwordField.setId("password");
        }
        return passwordField;
    }

}