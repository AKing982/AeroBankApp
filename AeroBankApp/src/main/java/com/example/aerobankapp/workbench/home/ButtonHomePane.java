package com.example.aerobankapp.workbench.home;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.buttonHeader.ButtonHeader;
import com.example.aerobankapp.workbench.home.depositPane.DepositPane;
import com.example.aerobankapp.workbench.home.forecastPane.ForecastPane;
import com.example.aerobankapp.workbench.home.loansPane.LoansPane;
import com.example.aerobankapp.workbench.home.settingsPane.SettingsPane;
import com.example.aerobankapp.workbench.home.transactionPane.TransactionPane;
import com.example.aerobankapp.workbench.vbutton.VButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.math.BigDecimal;
import java.util.Date;

public class ButtonHomePane extends GridPane
{
    private VButton transactionBtn = null;

    @Deprecated
    private VButton loanBtn = null;

    private VButton depositBtn = null;

    private VButton withdrawBtn = null;
    private VButton moneyTransferBtn = null;
    private VButton settingsBtn = null;
    private VButton forecastingBtn = null;
    private Date date = null;
    private Label welcomeLabel = null;
    private BigDecimal balance = null;
    private ButtonHeader transactionHeader = new ButtonHeader(new Label(CommonLabels.TRANSACTIONS));

    @Deprecated
    private ButtonHeader loansHeader = new ButtonHeader(new Label(CommonLabels.LOANS));

    private ButtonHeader depositHeader = new ButtonHeader(new Label(CommonLabels.DEPOSIT));

    private ButtonHeader forecastingHeader = new ButtonHeader(new Label(CommonLabels.FORECASTING));
    private ButtonHeader userSetupHeader = new ButtonHeader(new Label(CommonLabels.SETTINGS));

    public ButtonHomePane()  {
        super();

        // Set the Alignment for the Button's
        setButtonAlignment();

        this.add(transactionHeader, 0, 0);
        this.add(new TransactionPane(), 0, 1);
        this.add(depositHeader, 0 , 2);
        this.add(new DepositPane(), 0, 3);
      //  this.add(loansHeader, 0, 2);
       // this.add(new LoansPane(), 0, 3);
        this.add(forecastingHeader, 0, 4);
        this.add(new ForecastPane(), 0, 5);
        this.add(userSetupHeader, 0, 6);
        this.add(new SettingsPane(), 0, 7);
        this.getStylesheets().add("background.css");
    }

    private VButton getTransactionBtn(){
        if(transactionBtn == null){
            transactionBtn = new VButton();
            //TODO: Implement new ThreadPool that will spin up the Accounts, User Data and Transaction data and the BankGUI
            // transactionBtn.setButtonAction(e -> new BankThread().start());
        }
        return transactionBtn;
    }

    private VButton getLoanBtn(){
        if(loanBtn == null){
            loanBtn = new VButton();
        }
        return loanBtn;
    }

    private VButton getMoneyTransferBtn(){
        if(moneyTransferBtn == null){
            moneyTransferBtn = new VButton();
        }
        return moneyTransferBtn;
    }

    private VButton getSettingsBtn(){
        if(settingsBtn == null){
            settingsBtn = new VButton();
        }
        return settingsBtn;
    }

    private VButton getForecastingBtn(){
        if(forecastingBtn == null){
            forecastingBtn = new VButton();
        }
        return forecastingBtn;
    }

    private Date getDate(){
        if(date == null){
            date = new Date();
        }
        return date;
    }

    private Label getWelcomeLabel(){
        if(welcomeLabel == null){
            welcomeLabel = new Label();
        }
        return welcomeLabel;
    }

    private void setButtonAlignment(){
        getTransactionBtn().setAlignment(Pos.CENTER_LEFT);
        getMoneyTransferBtn().setAlignment(Pos.CENTER_LEFT);
        getLoanBtn().setAlignment(Pos.CENTER_LEFT);
        getForecastingBtn().setAlignment(Pos.CENTER_LEFT);
        getSettingsBtn().setAlignment(Pos.CENTER_LEFT);
    }

}
