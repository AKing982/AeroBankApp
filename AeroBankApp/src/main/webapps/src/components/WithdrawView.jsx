import {useEffect, useState} from "react";
import AccountSelect from "./AccountSelect";
import axios from "axios";
import AccountListView from "./AccountListView";
import Account from "./Account";
import WithdrawDescription from "./WithdrawDescription";
import '../WithdrawView.css';
import ScheduleComboBox from "./ScheduleComboBox";
import BasicTextField from "./BasicTextField";
import NumberField from "./NumberField";
import BasicDatePicker from "./BasicDatePicker";
import TimePickerBox from "./TimePickerBox";
import BasicButton from "./BasicButton";
import WithdrawTable from "./WithdrawTable";

export default function WithdrawView()
{
    const [accountCode, setAccountCode] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [description, setDescription] = useState(null);
    const [schedule, setSchedule] = useState(null);
    const [amount, setAmount] = useState(null);
    const [selectedDate, setSelectedDate] = useState(null);
    const [selectedTime, setSelectedTime] = useState(null);
    const user = sessionStorage.getItem('username');

    useEffect(() => {
        setIsLoading(true);
        axios.get(`http://localhost:8080/api/accounts/data/codes${user}`)
            .then(response => {
                setAccountCode(response.data)
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error);
            })
            .then(() => {
                setIsLoading(true);
            })

    }, []);

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    const handleScheduleChange = (event) => {
        setSchedule(event.target.value);
    }

    const handleAmountChange = (event) => {
        setAmount(event.target.value);
    }

    const handleDateChange = (event) => {
        setSelectedDate(event.target.value);
    }

    const handleTimeChange = (event) => {
        setSelectedTime(event.target.value);
    }

    const handleWithdraw = async() => {

    }


    return (
        <div className="withdraw-view-container">
            <header className="withdraw-view-header">
            </header>
            <div className="withdraw-view-center">
                <div className="withdraw-form-container">
                    <div className="withdraw-row">
                        <div className="withdraw-account-selection">
                            <label htmlFor="account-withdraw" className="withdraw-account-selection-label">Account to Withdraw:</label>
                            <AccountSelect accounts={accountCode} />
                        </div>
                        <div className="withdraw-schedule">
                            <ScheduleComboBox data={options} value={schedule} onChange={handleScheduleChange}/>
                        </div>
                        <div className="withdraw-description">
                            <WithdrawDescription label="Description" value={description} onChange={handleDescriptionChange}/>
                        </div>
                    </div>

                    <div className="withdraw-row">
                        <div className="withdraw-amount-field">
                            <NumberField label="Amount" value={amount} onChange={handleAmountChange}/>
                        </div>
                        <div className="withdraw-data-time">
                            <BasicDatePicker height="67" label="Select Date" value={selectedDate} onChange={handleDateChange}/>
                        </div>
                        <div className="withdraw-time-picker">
                            <TimePickerBox height="67" value={selectedTime} onChange={handleTimeChange}/>
                        </div>
                    </div>
                    <div className="withdraw-submit-button">
                        <BasicButton text="Submit" submit={handleWithdraw}/>
                    </div>
                </div>
                <WithdrawTable />
            </div>
        </div>
    );
}

const options = [
    'Once',
    'Daily',
    'Weekly',
    'Monthly'
]
