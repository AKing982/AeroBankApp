package com.example.aerobankapp.workbench.runner;

import com.example.aerobankapp.workbench.home.Home;
import com.example.aerobankapp.workbench.utilities.logging.AeroLogger;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HomeRunner implements Runnable
{
    private final Home home;
    private AeroLogger logger = new AeroLogger(HomeRunner.class);
    @Autowired
    public HomeRunner(Home home)
    {
        this.home = home;
    }

    @Override
    public void run()
    {
        Platform.runLater(() ->
        {
            try
            {

                Stage stage = new Stage();
                home.start(stage);

            }catch(Exception e)
            {
                logger.error("Home Thread Exception: ", e);
            }

        });
    }
}
