package com.example.aerobankapp.workbench.home.forecastPane;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.vbutton.VButton;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class ForecastPane extends HBox {
    private VButton forecastBtn = null;

    public ForecastPane(){
        super();

        // Add the Forecasting Button to the pane
        this.getChildren().add(getForecastBtn());

        // Translate the Button to the right
        this.setTranslateX(22);

        // Set the alignment Property
        this.setAlignment(Pos.CENTER_LEFT);
    }

    private VButton getForecastBtn(){
        if(forecastBtn == null){
            forecastBtn = new VButton();
            forecastBtn.setGraphic(new Image("/forecasting.png"), 90, 90);
            forecastBtn.setLabel(CommonLabels.FORECASTING);
            forecastBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            forecastBtn.setVButtonAlignment(Pos.CENTER);
        }
        return forecastBtn;
    }
}
