import TableView from "./TableView";
import ListView from './AccountListView';
import './AccountBox';
import AccountBox from "./AccountBox";
import {useEffect, useState} from "react";
import '../DepositView.css';

import TimeComboBox from "./TimeComboBox";
import ScheduleComboBox from "./ScheduleComboBox";
import DepositAmount from "./DepositAmount";
import {DepositDescription} from "./DepositDescription";
import axios from "axios";
import BasicDatePicker from "./BasicDatePicker";
import {TimePicker} from "@mui/x-date-pickers";
import BasicTimePicker from "./BasicTimePicker";
import ComboBox from "./ComboBox";
import AccountSelect from "./AccountSelect";
import TimePickerBox from "./TimePickerBox";
import EnhancedTable from "./EnhancedTable";
import {EnhancedTableHead} from "./EnhancedTable";
import {Button} from "@mui/material";
import BasicButton from "./BasicButton";
import AlertDialog from "./AlertDialog";
import DataTable from "./DataTable";
import Account from "./Account";
import CollapsiblePanel from "./CollapsiblePanel";

export default function DepositView()
{
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [schedule, setSchedule] = useState(null);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedTime, setSelectedTime] = useState(null);
    const [description, setDescription] = useState('');
    const [interval, setInterval] = useState('');
    const [IsLoading, setIsLoading] = useState(false);
    const [deposit, setDeposit] = useState('');
    const [accountID, setAccountID] = useState('');

    const [accountCodes, setAccountCodes] = useState([]);

    const user = sessionStorage.getItem('username');

    useEffect(() => {
        setIsLoading(true);
      axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/codes/${user}`)
          .then(response => {
              setAccountCodes(response.data)
              console.log('Fetching Account Codes: ', response.data);
          })
          .catch(error => {
              console.error('There has been a problem with your fetch operation: ', error);
          })
          .then(() => {
              setIsLoading(false);
          })

    }, []);

    const handleDeposit = async() => {
        setIsDialogOpen(true);
        console.log(accountID);
        console.log(deposit);
        console.log(interval);
        console.log(description);
        const requestData = {
            accountCode: accountID,
            amount: deposit,
            description: description,
            scheduleInterval: schedule,
            timeScheduled: selectedTime,
            date: selectedDate,
        }

      try{
          console.log("Request Data before POST",requestData);
          const response = await fetch(`http://localhost:8080/AeroBankApp/api/deposits/create`, {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
                  'Accept': 'application/json',
              },
              body: JSON.stringify(requestData)
          });
          const responseData = await response.json();

          console.log('Deposit posted successfully:', responseData);
      }catch(error)
      {
          console.error('There was an error!', error);
      }

    };

    const handleAccountIDChange = (event) => {
        console.log(event.target.value);
        setAccountID(event.target.value);
    }

    const handleCloseDialog = () => {
        setIsDialogOpen(false);
    }

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    const handleAmountChange = (event) => {
        setDeposit(event.target.value);
    }

    const handleDateChange = (newValue) => {
        let stringDate = parseDateToString(newValue);
        setSelectedDate(stringDate);
    }

    const handleTimeChange = (newValue) => {

        setSelectedTime(newValue);
    }

    const parseDateToString = (date) => {
        return date.toLocaleString();
    }

    const handleScheduleChange = (event, newValue) => {
        if(newValue !== null && newValue !== undefined)
        {
            setSchedule(newValue);
        }
    }

    return (
        <div className="deposit-view-container">
            <header className="deposit-view-header">
            </header>
            <div className="deposit-account-list">
                <CollapsiblePanel title="Accounts" content={<ListView items={<Account accountCode={"A1"} available={4500} balance={5600} pending={15} color="red" onAccountClick={handleAccountIDChange}/>}
                />}/>

            </div>
            <div className="vertical-line">
            </div>
            <div className="deposit-view-right">
                <div className="form-group">
                    <div className="deposit-account-selection">
                        <label htmlFor="account-deposit" className="deposit-account-label">Account to Deposit: </label>
                        <AccountSelect accounts={accountCodes} value={accountID} onChange={handleAccountIDChange}/>
                    </div>
                    <div className="deposit-deposit-description">
                        <DepositDescription value={description}
                                            onChange={handleDescriptionChange} />
                    </div>
                    <div className="deposit-amount-field">
                        <DepositAmount value={deposit} onChange={handleAmountChange}/>
                    </div>
                    <div className="deposit-schedule-mode">
                       <ScheduleComboBox data={options} value={schedule} onChange={handleScheduleChange}/>
                    </div>
                    <div className="date-time-container">
                        <BasicDatePicker label="Select Date" height="60" width="20" title="Choose a Date: "
                        value={selectedDate}
                                         onChange={handleDateChange}/>
                </div>
                    <div className="deposit-time">
                        <TimePickerBox height="60" value={selectedTime} onChange={handleTimeChange}/>
                    </div>
                    <div className="deposit-submit-button">
                        <BasicButton text="Submit" submit={handleDeposit}/>
                    </div>
                </div>
                <DataTable selectedAccount={accountID} />
            </div>
        </div>
    );
}

const accounts = [
    {value: 'A1', label: 'A1'},
    {value: 'A2', label: 'A2'}
]

const options = [
    'Once',
    'Daily',
    'Weekly',
    'Monthly'
]




