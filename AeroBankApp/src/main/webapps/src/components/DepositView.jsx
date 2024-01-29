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

export default function DepositView()
{
    const [schedule, setSchedule] = useState('');
    const [selectedDate, setSelectedDate] = useState('');
    const [selectedTime, setSelectedTime] = useState('');
    const [description, setDescription] = useState('');
    const [interval, setInterval] = useState('');
    const [IsLoading, setIsLoading] = useState(false);
    const [deposit, setDeposit] = useState('');
    const [accountID, setAccountID] = useState('');

    const [accountCodes, setAccountCodes] = useState([]);

    const user = sessionStorage.getItem('username');

    useEffect(() => {
        setIsLoading(true);
      axios.get(`http://localhost:8080/api/accounts/data/codes/${user}`)
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
        const requestData = {
            accountID,
            deposit,
            description,
            interval,
            selectedTime,
            selectedDate,
            schedule
        }

      try{
          console.log(requestData);
          const response = await axios.post('http://localhost:8080/api/deposits/create', requestData);
          console.log('Deposit posted successfully:', response.data);
      }catch(error)
      {
          console.error('There was an error!', error);
      }
    };

    return (
        <div className="deposit-view-container">
            <header className="deposit-view-header">
            </header>
            <div className="deposit-account-list">
                <ListView items={<AccountBox accountCode={"A1"} available={4500} balance={5600} pending={15} color="red"/>}
                />
            </div>
            <div className="vertical-line">
            </div>
            <div className="deposit-view-right">
                <div className="form-group">
                    <div className="deposit-account-selection">
                        <label htmlFor="account-deposit" className="deposit-account-label">Account to Deposit: </label>
                        <AccountSelect accounts={accountCodes} setAccountCodes={setAccountCodes}/>
                    </div>
                    <div className="deposit-deposit-description">
                        <DepositDescription description={description}
                                            setDescription={setDescription} />
                    </div>
                    <div className="deposit-amount-field">
                        <DepositAmount deposit={deposit} setDeposit={setDeposit}/>
                    </div>
                    <div className="deposit-schedule-mode">
                       <ScheduleComboBox data={options} setSchedule={setSchedule}/>
                    </div>
                    <div className="date-time-container">
                        <BasicDatePicker label="Select Date" height="60" width="20" title="Choose a Date: "
                        selectedDate={selectedDate}
                                         onDateChange={setSelectedDate}/>
                </div>
                    <div className="deposit-time">
                        <TimePickerBox height="60"/>
                    </div>
                    <div className="deposit-submit-button">
                        <BasicButton text="Submit" submit={handleDeposit}/>
                    </div>
                </div>
                <TableView />
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




