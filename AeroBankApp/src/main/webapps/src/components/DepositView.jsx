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
import {DesktopDatePicker, LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import BasicTimePicker from "./BasicTimePicker";
import ComboBox from "./ComboBox";
import AccountSelect from "./AccountSelect";
import TimePickerBox from "./TimePickerBox";
import EnhancedTable from "./EnhancedTable";
import {EnhancedTableHead} from "./EnhancedTable";
import {
    Accordion,
    AccordionDetails,
    AccordionSummary, Alert,
    Button, CircularProgress,
    FormControl,
    InputLabel, MenuItem, Select, Snackbar,
    TextField,
    Typography
} from "@mui/material";
import BasicButton from "./BasicButton";
import AlertDialog from "./AlertDialog";
import DataTable from "./DataTable";
import Account from "./Account";
import CollapsiblePanel from "./CollapsiblePanel";
import NumberField from "./NumberField";
import {Container} from "@mui/system";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import DepositAccountCode from "./DepositAccountCode";
import Dialog from "@mui/material/Dialog";
import {format} from "date-fns";

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

    const [accountCodes, setAccountCodes] = useState([]);

    const user = sessionStorage.getItem('username');

    const [selectedAccountCode, setSelectedAccountCode] = useState('');
    const [amount, setAmount] = useState('');
    const [selectedAccount, setSelectedAccount] = useState('');
    const [depositDate, setDepositDate] = useState(new Date());
    const [depositTime, setDepositTime] = useState(new Date());
    const [scheduleInterval, setScheduleInterval] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [deposits, setDeposits] = useState([]);
    const [snackbarmessage, setSnackbarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [depositData, setDepositData] = useState([]);
    const [accounts, setAccounts] = useState(null);
    const [accountData, setAccountData] = useState([]);
    const [selectedAccountType, setSelectedAccountType] = useState(null);
    const [selectedAccountID, setSelectedAccountID] = useState(null);
    const [isAccountCodeLoading, setIsAccountCodeLoading] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');
    const [accountID, setAccountID] = useState(0);

    const backgroundImageUrl = '/images/pexels-pixabay-417173.jpg';

    useEffect(() => {
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/${user}/account-types`)
            .then(response => {
                const accountMapping = response.data;
                const accountPairs = Object.entries(accountMapping).map(([key, value]) => {
                    console.log('Account Type: ', value);
                    return { accountId: key, accountType: value };
                });
                setAccountData(accountPairs);
            })
            .catch(error => {
                console.log('There was an error in fetching account-types: ', error);
            })

    }, [user])

    useEffect(() => {
        setIsAccountCodeLoading(true)// Start showing the loading indicator

        // Simulating a delay using setTimeout
        const timeoutId = setTimeout(() => {
            axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/codes/${user}`)
                .then(response => {
                    if (Array.isArray(response.data)) {
                        setAccountCodes(response.data);
                    } else {
                        console.error('Invalid data format received:', response.data);
                    }
                })
                .catch(error => {
                    console.error('There has been a problem with your fetch operation:', error);
                })
                .finally(() => {
                    setIsAccountCodeLoading(false);// Stop showing the loading indicator
                });
        }, 4000); // Delay of 2000 milliseconds (2 seconds)

        // Cleanup function to clear the timeout if the component unmounts before the timeout is completed
        return () => clearTimeout(timeoutId);
    }, [user]);




    const handleSelectedAccountCode = (event) => {

        setAccountCodes(event.target.value);
    }

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    const handleSearchChange = (event) => {
        setSearchTerm(event.target.value);
    }

    const filteredDeposits = deposits.filter(deposit => {
        deposit.description.toLowerCase().includes(searchTerm.toLowerCase())
    });


    const handleSelectedAccountID = (account) => {
        setSelectedAccountID(account.accountID);
    }

    const handleAccountSelect = (account) => {
        console.log('Account: ', account);
        console.log('Selected Account Type: ', account.accountType);
        setSelectedAccountType(account.accountType);
        setSelectedAccountID(account.accountId);
        console.log('Selected Account ID: ', selectedAccountID);
        // Fetch and set deposits for the selected account
        setDeposits(sampleDeposits); // Replace with actual API call
    };


    const sampleDeposits = [
        // Sample deposit data
    ];

    // Sample data for account codes and deposit intervals
    const scheduleIntervals = ['Once', 'Daily', 'BiWeekly', 'Weekly', 'Monthly'];

    async function prepareAndSendAccountIDRequest(accountCode)
    {
        const accountID = await fetchAccountID(accountCode);

        if(!accountID)
        {
            console.error('Unable to retrieve accountID');
            return;
        }

        return accountID;
    }

    const fetchAccountID = async (accountCode) => {
        if (!accountCode) {
            console.error('Account code is null or undefined.');
            return;
        }

        let userID = sessionStorage.getItem('userID');

        try {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/${userID}/${accountCode}`);
            console.log('AccountID from Response: ', response.data);
            setAccountID(response.data.accountID);
            return response.data.accountID;
        } catch (error) {
            console.error('There has been a problem with your fetch operation: ', error);
            // Handle specific error responses here if needed
        }

    }

    const handleDeposit = async() => {

        const formattedDepositTime = format(depositTime, 'HH:mm:ss');
        if(!amount && !description && !selectedAccountCode && !scheduleInterval)
        {
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackbarMessage('Please Select an Amount, Description, AccountCode, and Schedule Interval.');
            return;
        }

        if(!amount || amount <= 0)
        {
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackbarMessage('Please enter a valid amount.');
            return;
        }
        if(!description || !isNaN(description))
        {
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackbarMessage('Please enter a valid description.');
            return;
        }
        if(!selectedAccountCode)
        {
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackbarMessage('Please select an AccountCode.');
            return;
        }
        if(!scheduleInterval)
        {
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackbarMessage('Please Select a Schedule Interval.');
            return;
        }

       const accountID = await prepareAndSendAccountIDRequest(selectedAccountCode);

        const userID = sessionStorage.getItem('userID');
        console.log('UserID: ', userID);

        const requestData = {
            userID: userID,
            accountCode: selectedAccountCode,
            accountID: accountID,
            amount: amount,
            description: description,
            scheduleInterval: scheduleInterval,
            timeScheduled: formattedDepositTime,
            date: depositDate,
        }

        // Confirmation dialog logic
        const confirm = window.confirm('Do you want to schedule this deposit?'); // This is a simple example. In a real app, you might use a MUI Dialog for better UX.
        if (confirm) {
            // If confirmed, proceed with deposit
            setIsLoading(true); // Show loading indicator
            try {
                // API call to submit the deposit
                const response = await axios.post('http://localhost:8080/AeroBankApp/api/deposits/submit', requestData);
                setSnackbarMessage('Deposit scheduled successfully');
                setSnackBarSeverity('success');
                setOpenSnackbar(true);
                // Handle successful deposit here
            } catch (error) {
                // Handle errors
                console.log('An error has occurred while submitting the deposit request: ', error);
                setSnackbarMessage('Failed to schedule deposit. Please try again.');
                setOpenSnackbar(true);
                setSnackBarSeverity('error');
            } finally {
                setIsLoading(false); // Hide loading indicator
            }
        }

        setIsDialogOpen(true);
        console.log(accountID);
        console.log(deposit);
        console.log(interval);
        console.log(description);

      try{
          console.log("Request Data before POST",requestData);
          const response = await fetch(`http://localhost:8080/AeroBankApp/api/deposits/submit`, {
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
        <Container style={{ marginTop: '20px'}}>
            <Typography variant="h4">Make a Deposit</Typography>

            <Accordion style={{ marginBottom: '20px'}}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                    <Typography>Select Account</Typography>
                </AccordionSummary>
                <AccordionDetails>
                    {accountData.map((account, index) => (
                        <Button key={index} variant="text"
                                onClick={() => handleAccountSelect(account)}
                                className={account.accountType === selectedAccountType ? 'selected' : ''}
                        >
                            {account.accountType}
                        </Button>
                    ))}
                </AccordionDetails>
            </Accordion>

            <form style={{ marginTop: '20px' }}>
                <LocalizationProvider dateAdapter={AdapterDateFns}>

                    <DepositAccountCode
                        accounts={accountCodes}
                        value={selectedAccountCode}
                        onChange={(e) => setSelectedAccountCode(e.target.value)}
                        loading={isAccountCodeLoading}
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
                        label="Deposit Date"
                        value={depositDate}
                        onChange={setDepositDate}
                        renderInput={(params) => <TextField {...params} fullWidth style={{ marginBottom: '20px' }} />}
                    />

                    <TimePicker
                        label="Deposit Time"
                        value={depositTime}
                        onChange={setDepositTime}
                        renderInput={(params) => <TextField {...params} fullWidth style={{ marginBottom: '20px' }} />}
                    />

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

                    <Button variant="contained" color="primary" onClick={handleDeposit}>
                        Schedule Deposit
                    </Button>
                </LocalizationProvider>
            </form>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="Deposit scheduled"
            >
                <Alert onClose={handleCloseSnackbar} severity={snackBarSeverity} variant="filled" sx={{ width: '100%'}}>
                    {snackbarmessage}
                </Alert>
            </Snackbar>

            <Dialog open={IsLoading}>
                <CircularProgress />
            </Dialog>
            <TextField
                fullWidth
                label="Search"
                value={searchTerm}
                onChange={handleSearchChange}
                margin="normal"
                variant="outlined"
            />

            <DataTable selectedAccount={selectedAccountType} accountID={selectedAccountID}/>
        </Container>
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




