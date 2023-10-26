package com.example.aerobankapp.workbench.accountBtn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountButton extends Application
{

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(AccountButton.class.getResource("/fxml/accountBox.fxml"));

        Scene scene = new Scene(loader.load(), 200, 100);
        stage.setScene(scene);
        stage.show();
    }
}
