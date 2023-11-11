package com.example.aerobankapp.workbench.login;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.utilities.UserProfile;
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
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@Setter
public class Login extends Application {

    private static TextField usernameField;
    private static PasswordField passwordField;
    private static Button signIn;
    private Button registerBtn;
    private Label usernameLabel;
    private Label passwordLabel;
    private Text welcomeText;
    private HBox textBoxPane;
    private static Text loginAlert;
    private Hyperlink forgotPasswordLink;
    private ToggleButton showPasswordBtn;
    private boolean showPasswordIsSelected;
    private UserProfile userProfile;
    private final double BUTTON_HEIGHT = 20;
    private static String textStyle = "-fx-font-size: 32px;\n" +
            "   -fx-font-family: \"Arial Black\";\n" +
            "   -fx-fill: #818181;\n" +
            "   -fx-effect: innershadow( three-pass-box, rgba(0, 0, 0, 0.7), 6, 0.0, 0, 2);";

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
        hideOrShowPasswordBtn(getShowPasswordBtn());


        Scene scene = getScene(mother);
        stage.setScene(scene);
        stage.show();
    }

    private void setPasswordFieldSize(PasswordField passwordField) {
        passwordField.setPrefSize(2, 2);
    }

    private void setMotherScene(BorderPane mother) {
        mother.getStylesheets().add(getCSSAsString("/background.css"));
    }

    private Scene getScene(BorderPane mother) {
        return new Scene(mother, 410, 260);
    }

    private GridPane getGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10.5, 11.5, 12.5, 13.5));

        grid.add(getUsernameLabel(), 0, 0);
        grid.add(getUserNameField(), 1, 0);
        grid.add(getPasswordLabel(), 0, 2);
        grid.add(getPasswordField(), 1, 2);
        grid.add(getShowPasswordBtn(), 2, 2);
        grid.add(getLoginAlert(), 1, 3);
        grid.add(getButtonBox(), 1, 4);

        return grid;
    }

    private Hyperlink getForgotPasswordLink() {
        if (forgotPasswordLink == null) {
            forgotPasswordLink = new Hyperlink("Forgot password");
        }
        return forgotPasswordLink;
    }

    private void showPassword(PasswordField passwordField)
    {
        passwordField.setText(passwordField.getText());
        passwordField.setPromptText("Password");
    }

    private void hidePassword(PasswordField passwordField)
    {
        passwordField.setPromptText("");
        passwordField.setText("");
    }

    private void switchShowPasswordGraphics(boolean isSelected)
    {

        if(isSelected)
        {
            ImageView imageView = getNewImage("hide_eye.png", BUTTON_HEIGHT);
            setShowPasswordGraphic(imageView);
        }
        else
        {
            ImageView imageView = getNewImage("/eye.png", BUTTON_HEIGHT);
            setShowPasswordGraphic(imageView);
        }
    }

    private void setShowPasswordGraphic(ImageView image)
    {
        getShowPasswordBtn().setGraphic(image);
    }

    private ImageView getNewImage(String path, double height)
    {
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(height);
        return imageView;
    }

    private void hideOrShowPasswordBtn(ToggleButton btn)
    {
        btn.setOnAction(e -> {
            if(btn.isSelected())
            {
                //TODO: Show password in password field and change button image
                showPassword(getPasswordField());
                switchShowPasswordGraphics(true);
            }
            else
            {
                // TODO: Hide password in password field and change button image to eye.png
                hidePassword(getPasswordField());
                switchShowPasswordGraphics(false);
            }
        });
    }


    private ToggleButton getShowPasswordBtn()
    {
        ImageView imageView = getNewImage("/eye.png", BUTTON_HEIGHT);
        if(showPasswordBtn == null)
        {
            showPasswordBtn = new ToggleButton();
            showPasswordBtn.setGraphic(imageView);
            showPasswordBtn.setPrefSize(Region.USE_COMPUTED_SIZE, BUTTON_HEIGHT);
            showPasswordBtn.setContentDisplay(ContentDisplay.CENTER);
        }
        return showPasswordBtn;
    }

    private Text getLoginAlert()
    {
        if(loginAlert == null)
        {
            loginAlert = new Text();
            loginAlert.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
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
            usernameField.setFont(Font.font("Sans Serif", FontWeight.NORMAL, 16));
            usernameField.setId("username");
        }
        return usernameField;
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
            registerBtn = new Button("Register");
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
         this.userProfile = new UserProfile(user);
     }

     private void registerAction()
     {

     }


    public Button getSignIn()
    {
        if(signIn == null)
        {
            signIn = new Button("Sign In");
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

    public PasswordField getPasswordField()
    {
        if(passwordField == null)
        {
            passwordField = new PasswordField();
            passwordField.getStylesheets().add(getCSSAsString("/textfield.css"));
            passwordField.setFont(Font.font(CommonLabels.SANS_SERIF, FontWeight.NORMAL, 16));
            passwordField.setId("password");
        }
        return passwordField;
    }

}
