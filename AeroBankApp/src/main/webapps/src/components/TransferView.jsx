import AccountSelect from "./AccountSelect";
import {useState} from "react";
import '../TransferView.css';
import {
    Alert,
    Button,
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

export default function TransferView()
{
    const [transferType, setTransferType] = useState('ownAccount');
    const [fromAccount, setFromAccount] = useState('');
    const [toAccount, setToAccount] = useState('');
    const [recipientAccount, setRecipientAccount] = useState('');
    const [recipientAccountCode, setRecipientAccountCode] = useState('');
    const [amount, setAmount] = useState('');
    const [transferDate, setTransferDate] = useState('');
    const [description, setDescription] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');

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
                    >
                        {/* Populate with user's accounts */}
                        <MenuItem value="checking">Checking</MenuItem>
                        <MenuItem value="savings">Savings</MenuItem>
                    </Select>
                </FormControl>

                {transferType === 'ownAccount' ? (
                    <FormControl fullWidth style={{ marginBottom: '20px' }}>
                        <InputLabel>To Account</InputLabel>
                        <Select
                            value={toAccount}
                            onChange={(e) => setToAccount(e.target.value)}
                        >
                            {/* Populate with user's other accounts */}
                            <MenuItem value="otherChecking">Other Checking</MenuItem>
                            <MenuItem value="otherSavings">Other Savings</MenuItem>
                        </Select>
                    </FormControl>
                ) : (
                    <>
                        <TextField
                            fullWidth
                            height="55"
                            label="Recipient's Account Number"
                            value={recipientAccount}
                            onChange={(e) => setRecipientAccount(e.target.value)}
                            style={{ marginBottom: '20px' }}
                        />
                        <TextField
                            fullWidth
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