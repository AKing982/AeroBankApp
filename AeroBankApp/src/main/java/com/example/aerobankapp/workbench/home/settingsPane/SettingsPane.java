package com.example.aerobankapp.workbench.home.settingsPane;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.vbutton.VButton;
import com.example.aerobankapp.workbench.vbutton.VButtonOLD;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class SettingsPane extends HBox
{
    private VButtonOLD settingsBtn = null;

    public SettingsPane() {
        super();

        // Add the settingsBtn to the pane
        this.getChildren().add(getSettingsBtn());

        // Translate the button to the right
        this.setTranslateX(22);

        // Set the Alignment
        this.setAlignment(Pos.CENTER_LEFT);
    }

    public VButtonOLD getSettingsBtn(){
        if(settingsBtn == null){
            settingsBtn = new VButtonOLD();
            settingsBtn.setGraphic(new Image("/601.9-setting-icon-iconbunny.jpg"), 90, 90);
            settingsBtn.setLabel(CommonLabels.SETTINGS);
            settingsBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            settingsBtn.setVButtonAlignment(Pos.CENTER);
          //  settingsBtn.setButtonAction(e -> new SettingsThread().start());
        }
        return settingsBtn;
    }
}
