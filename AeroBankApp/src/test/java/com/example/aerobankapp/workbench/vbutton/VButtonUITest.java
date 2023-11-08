package com.example.aerobankapp.workbench.vbutton;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class VButtonUITest extends ApplicationTest {

    @Override
    public void start(Stage primaryStage) {
        ImageView imageView = new ImageView(new Image("/forecasting.png"));
        VButton vButton = new VButton(imageView, "test");

        StackPane root = new StackPane(vButton);
        Scene scene = new Scene(root, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testButtonAppearance()
    {
        WaitForAsyncUtils.waitForFxEvents();

        clickOn(LabeledMatchers.hasText(""));
    }

    @AfterEach
    void tearDown() {
    }
}