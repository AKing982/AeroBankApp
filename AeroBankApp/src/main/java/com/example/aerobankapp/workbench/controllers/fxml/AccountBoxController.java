package com.example.aerobankapp.workbench.controllers.fxml;

import com.example.aerobankapp.account.AccountDetails;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountBoxController implements Initializable
{
    @FXML
    private Label acctID;

    @FXML
    private Label account;

    @FXML
    private Label available;

    @FXML
    private Label pending;

    @FXML
    private Circle circle;

    @FXML
    private HBox accountBox;

    @FXML
    private Button acctBtn;

   private static AccountDetails details;

    private Logger LOGGER = LoggerFactory.getLogger(AccountBoxController.class);

    public AccountBoxController(){

    }

    private void acctBtnAction(){
        this.acctBtn.setOnAction(e -> {
            // Get a connection to the database using a Connection Pool

            // Acquire the data for the account

            String acctID = getAcctID().getText();


            //
        });
    }


    public static void setDetails(AccountDetails accountDetails){
        details = accountDetails;
    }

    public AccountDetails getDetails(){
        return details;
    }

    public Label getAcctID() {
        if(acctID == null){
            acctID = new Label();
        }
        return acctID;
    }

    public void setAcctID(Label acctID) {
        this.acctID = acctID;
    }

    public Label getAccount() {
        if(account == null){
            account = new Label();
        }
        return account;
    }

    public void setAccount(Label account) {
        this.account = account;
    }

    public Label getAvailable() {
        if(available == null){
            available = new Label();
        }
        return available;
    }

    public void setAvailable(Label available) {
        this.available = available;
    }

    public Label getPending() {
        if(pending == null){
            pending = new Label();
        }
        return pending;
    }

    public void setPending(Label pending) {
        this.pending = pending;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // If the accountBox is clicked, load the account details into the pending transactions and transactions table.
        //accountBox.setOnMouseClicked(e -> );

     //   String loggedInUser = new LoggedInUser().getCurrentUser();
       // UserAccountDetailQuery userAccountDetailQuery = new UserAccountDetailQuery(loggedInUser);
        try{

       //     System.out.println("Controller Details ID: " + this.details.getId());
        //    System.out.println("Controller Details Balance: $" + this.details.getAvailable());
        //    System.out.println("Controller Details Name: " + this.details.getName());
       //     System.out.println("Controller Details Pending: $" + this.details.getPending());
       //     Map<String, List<String>> accountDetails = userAccountDetailQuery.getAccountDetailsListElements();
       //     getAcctID().setText(getDetails().getId());
       //     getAccount().setText(getDetails().getName());
       //     getAvailable().setText("Available: $" + getDetails().getAvailable());
            getAvailable().textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
                {
                    getAvailable().setText("Available: $" + t1);
                }
            });

           // getPending().setText("Pending: " + getDetails().getPending());

            getPending().textProperty().addListener(new ChangeListener<String>()
            {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
                {
                    getPending().setText("Pending: $" + t1);
                }
            });

         //   getCircle().setFill(getDetails().getColor());


            LOGGER.debug("AccountID: " + getAcctID().getText());
            LOGGER.debug("Name: " + getAccount().getText());
            LOGGER.debug("Available: " + getAvailable().getText());
            LOGGER.debug("Pending: " + getPending().getText());

        }catch(ClassCastException | NullPointerException ex){
            LOGGER.error("An Error has occurred: ", ex);
        }

    }

    private void AvailableAmountActionListener()
    {
        getAvailable().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
           //     getAvailable().setText("Available: $" + getDetails().getAvailable());
            }
        });
    }

    private void PendingAmountActionListener()
    {
        getPending().textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
              //  getPending().setText("Pending: $" + getDetails().getPending());
            }
        });
    }

}
