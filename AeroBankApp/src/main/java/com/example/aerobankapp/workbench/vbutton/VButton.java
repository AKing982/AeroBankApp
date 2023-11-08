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
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class VButton extends VBox
{
    private Button button;
    private ImageView image;
    private Font buttonFont;
    private Label buttonLabel;
    private final String labelStylesheet = "label.css";
    private String text;

    public VButton()
    {
        // Should be empty
    }

    /** To be used if only needing the image **/
    public VButton(ImageView imageView, String text)
    {
        initialize(imageView,text);
        addChildren();
    }

    private void addChildren()
    {
        this.getChildren().addAll(getButton(), getLabel());
    }

    private void initialize(ImageView imageView, String text)
    {
        this.image = imageView;
        this.text = text;
        this.button = new Button("", image);
        this.buttonFont = Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16);
        this.buttonLabel = getLabel();
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
        this.image = getImageView(btnImage);
        setDimensions(image, height, width);
        setRatio(image, ratio);
        setButtonPreferences(height, width, image);
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
        button.setPrefSize(height, width);
        button.setGraphic(img);
    }

    public void setLabel(Label label)
    {
        this.buttonLabel = label;
    }

    public Label getLabel()
    {
        if(buttonLabel == null)
        {
            buttonLabel = new Label(text);
            buttonLabel.getStylesheets().add(labelStylesheet);
        }
        return buttonLabel;
    }

    public VBox getVBox()
    {
        return this;
    }
}
