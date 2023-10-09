package com.example.aerobankapp.workbench;

import com.example.aerobankapp.workbench.utilities.Loader;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;


public class LoginGUI extends Application implements Loader
{
    private AeroLogger aeroLogger;

    @Override
    public void start(Stage stage) throws Exception
    {
        FXMLLoader loader = getLoader();
    }

    @Override
    public FXMLLoader getLoader()
    {
        FXMLLoader loader = null;
        try
        {
            loader = FXMLLoader.load(getClass().getResource("/fxml/Login.fxml"));

        }catch(IOException ex)
        {

        }
        return loader;
    }
}
