import AccountSelect from "./AccountSelect";
import {useEffect, useState} from "react";
import '../TransferView.css';
import {
    Alert,
    Button, CircularProgress,
    FormControl, FormControlLabel,
    FormLabel,
    InputLabel,
    MenuItem, Radio, RadioGroup,
    Select,
    Snackbar,
    TextField,
    Typography
} from "@mui/material";
import {Container} from "@mui/system";
import axios from "axios";
import Dialog from "@mui/material/Dialog";

export default function TransferView()
{
    let userID = sessionStorage.getItem('userID');
    const [transferType, setTransferType] = useState('ownAccount');
    const [fromAccount, setFromAccount] = useState('');
    const [toAccount, setToAccount] = useState('');
    const [recipientAccount, setRecipientAccount] = useState('');
    const [recipientAccountCode, setRecipientAccountCode] = useState('');
    const [amount, setAmount] = useState('');
    const [transferDate, setTransferDate] = useState('');
    const [transferTime, setTransferTime] = useState('');
    const [description, setDescription] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [accountNamesList, setAccountNamesList] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        setIsLoading(true);
        const timeoutId = setTimeout(() => {
            axios.get(`http://localhost:8080/AeroBankApp/api/accounts/list/${userID}`)
                .then(response => {
                    console.log('Account Names List: ', response.data);
                    setAccountNamesList(response.data);
                })
                .catch(error => {
                    console.error('Unable to fetch List of Account Names due to the following: ', error);
                })
                .finally(() => {
                    setIsLoading(false);
                })
        }, 2000);

        return () => clearTimeout(timeoutId);
    }, [userID]);

    const handleTransfer = () => {
        // Handle the transfer logic

        if(!fromAccount){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please Select a From Account.');
        }

        if(!toAccount){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please Select a To Account.');
        }

        if(!amount){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please Enter a valid amount.');
        }

        if(!transferDate){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please choose a transfer date.');
        }

        if(!description){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please enter a description.');
        }

    };

    const fetchAccountNumberExists = async (accountNumber) => {
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/exists/${accountNumber}`)

            if(response.data){

            }

        }catch(error){
            console.error('There was an error determining if account number exists: ', error);
        }
    }

    function getTransferRequest(TransferType){
        switch(TransferType){
            case 'To Another User':

            case  'To Own Account':

            default:
                console.log('Selected option not valid');
        }
    }

    function snackBar(isOpen, errorMessage, message){
        setOpenSnackbar(isOpen);
        setSnackBarSeverity(errorMessage);
        setSnackBarMessage(message);
    }

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Container style={{ marginTop: '20px'}}>
            <Typography variant="h4">Make a Transfer</Typography>
            <form style={{ marginTop: '20px' }}>
                <FormControl component="fieldset" style={{ marginBottom: '20px' }}>
                    <FormLabel component="legend">Transfer Type</FormLabel>
                    <RadioGroup
                        row
                        aria-label="transferType"
                        name="row-radio-buttons-group"
                        value={transferType}
                        onChange={(e) => setTransferType(e.target.value)}
                    >
                        <FormControlLabel value="ownAccount" control={<Radio />} label="To Own Account" />
                        <FormControlLabel value="otherUser" control={<Radio />} label="To Another User" />
                    </RadioGroup>
                </FormControl>

                <FormControl fullWidth style={{ marginBottom: '20px' }}>
                    <InputLabel>From Account</InputLabel>
                    <Select
                        value={fromAccount}
                        onChange={(e) => setFromAccount(e.target.value)}
                        displayEmpty
                        renderValue={selected => {
                            if (selected.length === 0) {
                                return <em>Please wait while we load your accounts...</em>;
                            }
                            return selected;
                        }}
                    >
                        {isLoading ? (
                            <MenuItem disabled value="">
                                <CircularProgress size={24} />
                            </MenuItem>
                        ) : (
                            accountNamesList.map((account) => (
                                <MenuItem key={account.id} value={account.accountName}>
                                    {account.accountName}
                                </MenuItem>
                            ))
                        )}
                    </Select>
                </FormControl>

                {transferType === 'ownAccount' ? (
                    <FormControl fullWidth style={{ marginBottom: '20px' }}>
                        <InputLabel>To Account</InputLabel>
                        <Select
                            value={toAccount}
                            onChange={(e) => setToAccount(e.target.value)}
                            displayEmpty
                            renderValue={selected => {
                                if(selected.length === 0){
                                    return <em>Please wait while we load your accounts...</em>;
                                }
                                return selected;
                            }}
                        >
                            {isLoading ? (
                                <MenuItem disabled value="">
                                    <CircularProgress size={24} />
                                </MenuItem>
                            ) : (
                                accountNamesList.filter(account => account.accountName !== fromAccount).map((account) => (
                                        <MenuItem key={account.id} value={account.accountName}>
                                            {account.accountName}
                                        </MenuItem>
                                ))
                            )}
                        </Select>
                    </FormControl>
                ) : (
                    <>
                        <TextField
                            fullWidth
                            label="Recipient's Account Number"
                            type="text"
                            value={recipientAccount}
                            onChange={(e) => setRecipientAccount(e.target.value)}
                            style={{ marginBottom: '20px' }}
                        />
                        <TextField
                            fullWidth
                            type="text"
                            label="Recipient's Account Code"
                            value={recipientAccountCode}
                            onChange={(e) => setRecipientAccountCode(e.target.value)}
                            style={{ marginBottom: '20px' }}
                        />
                    </>
                )}

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
                    label="Transfer Date"
                    type="date"
                    InputLabelProps={{ shrink: true }}
                    value={transferDate}
                    onChange={(e) => setTransferDate(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <TextField
                    fullWidth
                    type="time"
                    label="Transfer Time"
                    value={transferTime}
                    onChange={(e) => setTransferTime(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <TextField
                    fullWidth
                    label="Description"
                    multiline
                    rows={4}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <Button variant="contained" color="primary" onClick={handleTransfer}>
                    Submit Transfer
                </Button>
            </form>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="Transfer successful"
            >
                <Alert onClose={handleCloseSnackbar} severity={snackBarSeverity} variant="filled" sx={{width: '100%'}}>
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </Container>
    );
}