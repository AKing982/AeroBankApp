package com.example.aerobankapp.workbench.home.withdrawPane;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.transactions.Withdraw;
import com.example.aerobankapp.workbench.vbutton.VButton;
import com.example.aerobankapp.workbench.vbutton.VButtonOLD;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class WithdrawPane extends HBox
{
    private VButtonOLD withdrawBtn;

    public WithdrawPane()
    {
        super();
        this.getChildren().add(getWithdrawBtn());

        this.setTranslateX(22);

        this.setAlignment(Pos.CENTER_LEFT);
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
