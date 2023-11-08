package com.example.aerobankapp.workbench.home.loansPane;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.vbutton.VButton;
import com.example.aerobankapp.workbench.vbutton.VButtonOLD;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class LoansPane extends HBox {
    private VButtonOLD loanBtn = null;

    public LoansPane(){
        super();

        // Add the children to the pane
        this.getChildren().add(getLoanBtn());

        // Translate the Loan Button to the right
        this.setTranslateX(22);

        // Set the Alignment
        this.setAlignment(Pos.CENTER_LEFT);

    }

    private VButtonOLD getLoanBtn(){
        if(loanBtn == null){
            loanBtn = new VButtonOLD();
            loanBtn.setGraphic(new Image("/loan-vector-icon.jpeg"), 90, 90);
            loanBtn.setLabel(CommonLabels.LOAN);
            loanBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            loanBtn.setVButtonAlignment(Pos.CENTER);

        }
        return loanBtn;
    }
}
