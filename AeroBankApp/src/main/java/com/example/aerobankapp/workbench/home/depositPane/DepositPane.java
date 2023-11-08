package com.example.aerobankapp.workbench.home.depositPane;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.vbutton.VButton;
import com.example.aerobankapp.workbench.vbutton.VButtonOLD;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class DepositPane extends HBox
{
    private VButtonOLD depositBtn = null;
    private VButtonOLD withdrawBtn = null;

    public DepositPane()
    {
        super();
        this.setSpacing(10);
        this.getChildren().addAll(getDepositBtn(), getWithdrawBtn());

        this.setTranslateX(22);

        this.setAlignment(Pos.CENTER_LEFT);
    }

    public VButtonOLD getDepositBtn()
    {
        if(depositBtn == null)
        {
            depositBtn = new VButtonOLD();
            depositBtn.setGraphic(new Image("/deposit.png"), 90, 90);
            depositBtn.setLabel(CommonLabels.DEPOSIT);
            depositBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            depositBtn.setVButtonAlignment(Pos.CENTER);
        }
        return depositBtn;
    }

    public VButtonOLD getWithdrawBtn()
    {
        if(withdrawBtn == null)
        {
            withdrawBtn = new VButtonOLD();
            withdrawBtn.setGraphic(new Image("/withdraw.png"), 90, 90);
            withdrawBtn.setLabel(CommonLabels.WITHDRAW);
            withdrawBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            withdrawBtn.setVButtonAlignment(Pos.CENTER);
        }
        return withdrawBtn;
    }
}

