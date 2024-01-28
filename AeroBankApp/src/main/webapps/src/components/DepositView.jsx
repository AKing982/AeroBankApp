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

export default function DepositView()
{
    const [account, setAccount] = useState('');
    const [schedule, setSchedule] = useState('');
    const [selectedDate, setSelectedDate] = useState('');
    const [selectedTime, setSelectedTime] = useState('');
    const [description, setDescription] = useState('');
    const [deposit, setDeposit] = useState('');
    const [data, setData] = useState({
        accountID: account,
        deposit : deposit,
        description : description,
        interval : schedule,
        time : selectedTime,
        date: selectedDate
    });

    const handleScheduleChange = (event) => {
        setSchedule(event.target.value);
    };

    const handleDateChange = (event) => {
        setSelectedDate(event.target.value);
    }

    const handleDeposit = async() => {
      try{
          const response = await axios.post('http://localhost:8080/api/deposits/create', data);
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
                        <input
                            type="text"
                            id="account-deposit"
                            className="input-field2"
                            value={account}
                            onChange={e => setAccount(e.target.value)}
                        />
                    </div>
                    <div className="deposit-deposit-description">
                        <DepositDescription description={description} setDescription={setDescription}/>
                    </div>
                    <div className="deposit-amount-field">
                        <DepositAmount deposit={deposit} setDeposit={setDeposit}/>
                    </div>
                    <div className="deposit-schedule-mode">
                       <ScheduleComboBox schedule={schedule} setSchedule={setSchedule}/>
                    </div>
                    <div className="date-time-container">
                        <div className="deposit-date">
                           <BasicDatePicker />
                        </div>
                        <div className="deposit-time">
                            <TimeComboBox selectedTime={selectedTime} setSelectedTime={setSelectedTime}/>
                        </div>
                        <div className="deposit-submit-button">
                            <button type="button2" className="logout-button" onChange={handleDeposit}>Submit</button>
                        </div>
                </div>
                </div>
                <TableView />
            </div>
        </div>
    );
}

