package com.example.aerobankapp.workbench.buttonHeader;

import com.example.aerobankapp.workbench.vbutton.VButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ButtonHeader extends VBox
{
    private Line header = null;

    public ButtonHeader(Label headerLabel){

        // Create a Line object
        Line headerLine = new Line();

        // Set the Stroke
        headerLine.setStroke(Color.BLACK);
        headerLine.setStrokeWidth(1);

        headerLine.setStartX(0);
        headerLine.setStartY(0);
        headerLine.setEndX(250);

        // Set the VBox padding
        this.setPadding(new Insets(10.5, 11.5, 12.5, 13.5));

        this.getStylesheets().add("label.css");

        // Set the Font Size
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 16));

        this.getChildren().addAll(headerLabel, headerLine);
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private Line getHeader(){
        if(header == null){
            header = new Line();
        }
        return header;
    }

    private void setHeaderStroke(Color color){
        getHeader().setStroke(color);
    }

    private void setStartX(double v){
        getHeader().setStartX(v);
    }

    private void setEndX(double v){
        getHeader().setEndX(v);
    }

    private void setStartY(double v){
        getHeader().setStartY(v);
    }
}
