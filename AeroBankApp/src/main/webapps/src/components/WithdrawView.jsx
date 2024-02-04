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
import DataTable from "./DataTable";
import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Autocomplete,
    Button,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Snackbar,
    TextField,
    Typography
} from "@mui/material";
import {TimePicker, DesktopDatePicker, LocalizationProvider} from "@mui/x-date-pickers";

import { Container } from "@mui/system";
import AccountTypeSelect from "./AccountTypeSelect";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import WithdrawAccountCode from "./WithdrawAccountCode";

export default function WithdrawView()
{const [selectedAccountCode, setSelectedAccountCode] = useState('');
    const [amount, setAmount] = useState('');
    const [selectedAccount, setSelectedAccount] = useState('');
    const [description, setDescription] = useState('');
    const [withdrawalDate, setWithdrawalDate] = useState(new Date());
    const [withdrawalTime, setWithdrawalTime] = useState(new Date());
    const [scheduleInterval, setScheduleInterval] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [withdrawals, setWithdrawals] = useState([]);

    const handleWithdraw = () => {
        // Handle the withdrawal logic
        setOpenSnackbar(true);
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    const handleAccountSelect = (account) => {
        setSelectedAccount(account);
        // Fetch and set withdrawals for the selected account
        setWithdrawals(sampleWithdrawals); // Replace with actual API call
    };

    const accounts = ['Checking', 'Savings'];

    const sampleWithdrawals = [
        { id: 1, date: '2024-02-01', time: '10:00 AM', amount: 100.00, status: 'Completed' },
        { id: 2, date: '2024-02-02', time: '11:00 AM', amount: 200.00, status: 'Pending' },
        // ... more withdrawals
    ];

    // Sample data for account codes and withdrawal intervals
    const accountCodes = ['ACC123', 'ACC456', 'ACC789'];
    const scheduleIntervals = ['Once', 'Daily', 'BiWeekly', 'Weekly', 'Monthly'];

    return (
        <Container style={{ marginTop: '20px' }}>
            <Typography variant="h4">Make a Withdrawal</Typography>

            <Accordion style={{ marginBottom: '20px' }}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                    <Typography>Select Account</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    {accounts.map((account, index) => (
                        <Button key={index} variant="text" onClick={() => handleAccountSelect(account)}>
                            {account}
                        </Button>
                    ))}
                </AccordionDetails>
            </Accordion>


            <form style={{ marginTop: '20px' }}>
                    {/* Account Code Selection */}
                <LocalizationProvider dateAdapter={AdapterDateFns}>

                    <WithdrawAccountCode
                        accounts={accountCodes} // Pass the list of account codes as props
                        value={selectedAccountCode}
                        onChange={(event) => setSelectedAccountCode(event.target.value)}
                    />

                    <TextField
                        fullWidth
                        label="Amount"
                        type="number"
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        style={{ marginBottom: '20px' }}
                    />

                    <TextField
                        fullWidth
                        label="Description (Optional)"
                        multiline
                        rows={2}
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        style={{ marginBottom: '20px' }}
                    />

                    <DesktopDatePicker
                        label="Withdrawal Date"
                        value={withdrawalDate}
                        onChange={setWithdrawalDate}
                        renderInput={(params) => <TextField {...params} fullWidth style={{ marginBottom: '20px' }} />}
                    />

                    <TimePicker
                        label="Withdrawal Time"
                        value={withdrawalTime}
                        onChange={setWithdrawalTime}
                        renderInput={(params) => <TextField {...params} fullWidth style={{ marginBottom: '20px' }} />}
                    />

                    {/* Schedule Interval Selection */}
                    <FormControl fullWidth style={{ marginBottom: '20px' }}>
                        <InputLabel>Schedule Interval</InputLabel>
                        <Select
                            value={scheduleInterval}
                            onChange={(e) => setScheduleInterval(e.target.value)}
                        >
                            {scheduleIntervals.map((interval, index) => (
                                <MenuItem key={index} value={interval}>{interval}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <Button variant="contained" color="primary" onClick={handleWithdraw}>
                        Schedule Withdrawal
                    </Button>
                </LocalizationProvider>
            </form>

            {/* Snackbar for Notifications */}
            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="Withdrawal scheduled"
            />
            <WithdrawTable />
        </Container>
    );
}

const options = [
    'Once',
    'Daily',
    'Weekly',
    'Monthly'
]
