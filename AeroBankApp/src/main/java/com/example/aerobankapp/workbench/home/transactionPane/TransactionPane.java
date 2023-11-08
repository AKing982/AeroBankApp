package com.example.aerobankapp.workbench.home.transactionPane;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.vbutton.VButton;
import com.example.aerobankapp.workbench.vbutton.VButtonOLD;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class TransactionPane extends HBox
{
    private VButtonOLD transactionBtn = null;
    private VButtonOLD moneyTransferBtn = null;

    public TransactionPane(){
        super();

        this.setSpacing(10);
        this.getChildren().addAll(getTransactionBtn(), getMoneyTransferBtn());

        this.setTranslateX(22);

        this.setAlignment(Pos.CENTER_LEFT);
    }

    public VButtonOLD getTransactionBtn(){
        if (transactionBtn == null) {
            transactionBtn = new VButtonOLD();
            transactionBtn.setGraphic(new Image("/transaction-icon.jpeg"), 90, 90);
            transactionBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
            transactionBtn.setLabel("Transactions");
            //transactionBtn.setVButtonAlignment(Pos.CENTER);
           // transactionBtn.setButtonAction(e -> new BankThread().start());
        }
        return transactionBtn;
    }

    public VButtonOLD getMoneyTransferBtn(){
        if(moneyTransferBtn == null){
            moneyTransferBtn = new VButtonOLD();
            moneyTransferBtn.setGraphic(new Image("/money_transfer.png"), 90, 90);
            moneyTransferBtn.setLabel(CommonLabels.MONEY_TRANSFER);
            //moneyTransferBtn.setVButtonAlignment(Pos.CENTER);
            moneyTransferBtn.setVButtonFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        }
        return moneyTransferBtn;
    }

}
