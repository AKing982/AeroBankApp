package com.example.aerobankapp.workbench.vbutton;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class VButton extends VBox
{
    private Button button;
    private ImageView image;
    private Font buttonFont;
    private Label buttonLabel;
    private boolean isPreserv;

    public VButton()
    {
        // Should be empty
    }

    public VButton(ImageView imageView)
    {
        initialize(imageView);
    }

    void initialize(ImageView imageView)
    {
        this.image = imageView;
        this.button = new Button("", image);
        this.buttonFont = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16);
    }

    public ImageView getGraphic()
    {
        return image;
    }

    public void setFont(Font fontExpected)
    {
        button.setFont(fontExpected);
    }

    public Font getFont()
    {
        return buttonFont;
    }

    public void setGraphic(Image btnImage, double height, double width, boolean ratio)
    {
        ImageView imageView = getImageView(btnImage);
        setDimensions(imageView, height, width);
        setRatio(imageView, ratio);
        setButtonPreferences(height, width, imageView);
    }

    private static ImageView getImageView(Image image)
    {
        return new ImageView(image);
    }

    private static void setRatio(ImageView image, boolean ratio)
    {
        image.setPreserveRatio(ratio);
    }

    private static void setDimensions(ImageView image, double height, double width)
    {
        image.setFitWidth(width);
        image.setFitHeight(height);
    }

    public void setButtonPreferences(double height, double width, ImageView img)
    {
        getButton().setPrefSize(height, width);
        getButton().setGraphic(img);
    }

    public void setLabel(Label label)
    {
        this.buttonLabel = label;
    }

    public Label getLabel()
    {
        return buttonLabel;
    }
}
