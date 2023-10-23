package com.example.aerobankapp.workbench.vbutton;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class VButton extends VBox
{
    private Button button = null;
    private Label btnLabel = null;
    private VBox btnBox = null;

    private final String STYLE_PRESSED = "-fx-background-color: transparent; -fx-padding: 3, 1, 1, 3;";
    private final String STYLE_NORMAL = "-fx-background-color: transparent; -fx-padding: 2, 2, 2, 2;";

    public VButton(){
        super();

        this.initialize();
    }

    void initialize(){
        getChildren().addAll(getButton(), getLabel());
    }

    private Button getButton(){
        if(button == null){
            button = new Button();
        }
        return button;
    }


    private VBox getBtnBox(){
        if(btnBox == null){
            btnBox = new VBox();
        }
        return btnBox;
    }

    public void setVButtonFont(Font font){
        getBtnLabel().setFont(font);
    }

    public void setLabel(String name){
        getBtnLabel().setText(name);
    }

    public void setText(String name){
        getBtnLabel().setText(name);
    }

    public Label getLabel(){
        return getBtnLabel();
    }

    public Label getBtnLabel(){
        if(btnLabel == null){
            btnLabel = new Label();
            btnLabel.getStylesheets().add("label.css");
        }
        return btnLabel;
    }

    public Node getImage(){
        return getButton().getGraphic();
    }

    public void setButtonStyle(String style){
        this.setStyle(style);
    }

    public void setGraphic(Image img, double height, double width){
        ImageView image = new ImageView(img);
        image.setFitHeight(height);
        image.setFitWidth(width);
        image.setPreserveRatio(true);
        getButton().setPrefSize(height, width);

        getButton().setGraphic(image);
    }

    public void setVButtonBorder(Border border){
        getButton().setBorder(border);
    }

    public void setButtonAction(EventHandler<ActionEvent> action){
        getButton().setOnAction(action);
    }

    public void setOnMousePressedImage(EventHandler<? super MouseEvent> event){

    }
    public void setVButtonVisibility(boolean isVisible){
        getButton().setVisible(isVisible);
    }

    public void setVButtonTooltip(Tooltip tip){
        getButton().setTooltip(tip);
    }

    public void setVButtonAlignment(Pos alignment){
        this.setAlignment(alignment);
    }
}
