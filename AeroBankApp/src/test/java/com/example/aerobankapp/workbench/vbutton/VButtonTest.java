package com.example.aerobankapp.workbench.vbutton;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;


import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class VButtonTest
{

    private VButton button;
    private ImageView image;
    private String text;

    @BeforeEach
    void setUp()
    {
        text = "Forecasting";
        image = new ImageView(new Image("/forecasting.png"));
        button = new VButton(image, text);
    }

    @Test
    public void testButtonImage()
    {
        ImageView actualImage = button.getGraphic();

        Button button1 = button.getButton();

        assertNotNull(button);
        assertEquals(image, actualImage);

    }

    @Test
    void testButtonFont()
    {

        Font fontExpected = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16);

        button.setFont(fontExpected);
        Font actualFont = button.getFont();

        assertNotNull(actualFont);
        assertEquals(fontExpected, actualFont);
    }

    @Test
    public void testSetButtonGraphic()
    {
        int expectedHeight = 90;
        int expectedWidth = 90;
        boolean e_ratio = true;
        Image expectedImage = new Image("/forecasting.png");
        button.setGraphic(expectedImage, expectedHeight, expectedWidth, e_ratio);

        Image actualDepositImage = button.getImage().getImage();
        double actualWidth = button.getWidth();
        double actualHeight = button.getHeight();
        boolean actualGraphicRatio = button.getGraphic().isPreserveRatio();

        assertNotNull(actualDepositImage);
       // assertEquals(expectedImage, actualDepositImage);
        assertEquals(expectedHeight, actualHeight);
        assertEquals(expectedWidth, actualWidth);
        assertEquals(e_ratio,actualGraphicRatio);
    }

    @Test
    public void testButtonLabel()
    {
        Label expectedLabel = new Label("Transactions");
        button.setLabel(expectedLabel);

        Label actualLabel = button.getLabel();

        assertNotNull(actualLabel);
        assertEquals(expectedLabel, actualLabel);
    }

    @Test
    public void testVButtonChildren()
    {
        String btnText = "Forecasting";
        ImageView testImage = new ImageView(new Image("/forecasting.png"));
        VButton vButton = new VButton(testImage, btnText);
        VBox expectedVBox = new VBox();
        Button button1 = new Button("Button");
        Label forecastLabel = new Label("Label");
        expectedVBox.getChildren().addAll(button1, forecastLabel);

        VBox vButtonBoxActual = vButton.getVBox();
        boolean isChild = vButtonBoxActual.getChildren().contains(button1);

        assertFalse(isChild);
        assertNotEquals(expectedVBox, vButtonBoxActual);
    }

    @AfterEach
    void tearDown() {
    }
}