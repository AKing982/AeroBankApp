package com.example.aerobankapp.workbench.home;

import com.example.aerobankapp.messages.CommonLabels;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class Home extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        Scene scene = new Scene(new ButtonHomePane(), 550, 680);
        stage.setScene(scene);
        stage.setTitle(CommonLabels.HOME);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
