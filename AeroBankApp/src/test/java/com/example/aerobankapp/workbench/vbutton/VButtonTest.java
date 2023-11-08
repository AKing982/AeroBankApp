package com.example.aerobankapp.workbench.vbutton;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @BeforeEach
    void setUp()
    {
        image = new ImageView(new Image("/forecasting.png"));
        button = new VButton(image);
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

    @AfterEach
    void tearDown() {
    }
}