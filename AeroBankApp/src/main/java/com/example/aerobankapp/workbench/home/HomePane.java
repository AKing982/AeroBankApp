package com.example.aerobankapp.workbench.home;

import com.example.aerobankapp.messages.CommonLabels;
import com.example.aerobankapp.workbench.buttonHeader.ButtonHeader;
import com.example.aerobankapp.workbench.home.depositPane.DepositPane;
import com.example.aerobankapp.workbench.home.forecastPane.ForecastPane;
import com.example.aerobankapp.workbench.home.settingsPane.SettingsPane;
import com.example.aerobankapp.workbench.home.transactionPane.TransactionPane;
import com.example.aerobankapp.workbench.vbutton.VButton;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomePane extends GridPane
{
    private TransactionPane transactionPane;
    private DepositPane depositPane;
    private ForecastPane forecastPane;
    private SettingsPane settingsPane;
    private ButtonHeader transactionHeader;
    private ButtonHeader depositHeader;
    private ButtonHeader forecastHeader;
    private ButtonHeader settingsHeader;
    private final String backgroundStylePath = "background.css";
    private Label welcomeLabel;
    private Label accountBalanceLabel;

    public HomePane()
    {
        super();
        setPaneAlignment();
        initializeGridPane();
        setBackgroundStyle();
    }

    private void setBackgroundStyle()
    {
        this.getStylesheets().add(backgroundStylePath);
    }

    private TransactionPane getTransactionPane()
    {
        if(transactionPane == null)
        {
            transactionPane = new TransactionPane();
        }
        return transactionPane;
    }

    private DepositPane getDepositPane()
    {
        if(depositPane == null)
        {
            depositPane = new DepositPane();
        }
        return depositPane;
    }

    private ForecastPane getForecastPane()
    {
        if(forecastPane == null)
        {
            forecastPane = new ForecastPane();
        }
        return forecastPane;
    }

    public SettingsPane getSettingsPane()
    {
        if(settingsPane == null)
        {
            settingsPane = new SettingsPane();
        }
        return settingsPane;
    }

    private void setPaneAlignment()
    {
        getTransactionPane().getTransactionBtn().setAlignment(Pos.CENTER_LEFT);
        getTransactionPane().getMoneyTransferBtn().setAlignment(Pos.CENTER_LEFT);
        getDepositPane().getDepositBtn().setAlignment(Pos.CENTER_LEFT);
        getDepositPane().getWithdrawBtn().setAlignment(Pos.CENTER_LEFT);
        getForecastPane().getForecastBtn().setAlignment(Pos.CENTER_LEFT);
        getSettingsPane().getSettingsBtn().setAlignment(Pos.CENTER_LEFT);
    }

    private void initializeGridPane()
    {
        this.add(getTransactionHeader(), 0, 0);
        this.add(getTransactionPane(), 0, 1);
        this.add(getDepositHeader(), 0, 2);
        this.add(getDepositPane(), 0, 3);
        this.add(getForecastHeader(), 0, 4);
        this.add(getForecastPane(), 0, 5);
        this.add(getSettingsHeader(), 0, 6);
        this.add(getSettingsPane(), 0, 7);

    }

    private ButtonHeader getTransactionHeader()
    {
        if(transactionHeader == null)
        {
            transactionHeader = new ButtonHeader(new Label(CommonLabels.TRANSACTIONS));
        }
        return transactionHeader;
    }

    private ButtonHeader getDepositHeader()
    {
        if(depositHeader == null)
        {
            depositHeader = new ButtonHeader(new Label(CommonLabels.DEPOSIT));
        }
        return depositHeader;
    }

    private ButtonHeader getForecastHeader()
    {
        if(forecastHeader == null)
        {
            forecastHeader = new ButtonHeader(new Label(CommonLabels.FORECASTING));
        }
        return forecastHeader;
    }

    private ButtonHeader getSettingsHeader()
    {
        if(settingsHeader == null)
        {
            settingsHeader = new ButtonHeader(new Label(CommonLabels.SETTINGS));
        }
        return settingsHeader;
    }
}
