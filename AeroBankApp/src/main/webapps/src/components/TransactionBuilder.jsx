import {Autocomplete, Button, FormControlLabel, Paper, Radio, Switch, TextField, Typography} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";
import MenuAppBar from "./MenuAppBar";
import backgroundImage from './images/pexels-krivec-ales-547115.jpg';

function TransactionBuilder(){
    const [transactionType, setTransactionType] = useState('');
    const [fromAccount, setFromAccount] = useState('');
    const [toAccount, setToAccount] = useState('');
    const [amount, setAmount] = useState('');
    const [scheduleType, setScheduleType] = useState('');
    const [scheduleDate, setScheduleDate] = useState('');
    const [scheduleTime, setScheduleTime] = useState('');
    const [description, setDescription] = useState('');
    const [enableNotifications, setEnableNotifications] = useState(false);

    const transactionTypes = [
        { label: 'Deposit', value: 'Deposit' },
        { label: 'Withdraw', value: 'Withdraw' }
    ];

    const accounts = [
        { label: 'Account 1', value: 'Account1' },
        { label: 'Account 2', value: 'Account2' }
    ];

    const scheduleOptions = [
        { label: 'Once', value: 'Once' },
        { label: 'Daily', value: 'Daily' },
        { label: 'Bi-Weekly', value: 'Bi-Weekly' },
        { label: 'Monthly', value: 'Monthly' },
        { label: 'Every Two Days', value: 'Every Two Days' }
    ];

    return (
        <div>
            <MenuAppBar />
            <Box sx={{
                flexGrow: 1,
                padding: 3,
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                minHeight: '100vh'
            }}>
                <Paper elevation={3} sx={{padding: 3, maxWidth: 600, width: '100%', margin: '20px', backgroundColor: 'rgba(255, 255, 255, 0.8)', backdropFilter: 'blur(10px)'}}>
                    <Typography variant="h4" gutterBottom>Configure Transaction</Typography>
                    <Autocomplete
                        options={transactionTypes}
                        getOptionLabel={(option) => option.label}
                        renderInput={(params) => <TextField {...params} label="Select Transaction Type" />}
                        value={transactionType}
                        onChange={(event, newValue) => setTransactionType(newValue)}
                        sx={{ mb: 2 }}
                    />

                    {transactionType && transactionType.value === 'Deposit' && (
                        <Autocomplete
                            options={accounts}
                            getOptionLabel={(option) => option.label}
                            renderInput={(params) => <TextField {...params} label="Select Account to Deposit" />}
                            value={toAccount}
                            onChange={(event, newValue) => setToAccount(newValue)}
                            sx={{ mb: 2 }}
                        />
                    )}

                    {transactionType && transactionType.value === 'Withdraw' && (
                        <>
                            <Autocomplete
                                options={accounts}
                                getOptionLabel={(option) => option.label}
                                renderInput={(params) => <TextField {...params} label="Select From Account" />}
                                value={fromAccount}
                                onChange={(event, newValue) => setFromAccount(newValue)}
                                sx={{ mb: 2 }}
                            />
                            <Autocomplete
                                options={accounts}
                                getOptionLabel={(option) => option.label}
                                renderInput={(params) => <TextField {...params} label="Select To Account" />}
                                value={toAccount}
                                onChange={(event, newValue) => setToAccount(newValue)}
                                sx={{ mb: 2 }}
                            />
                        </>
                    )}

                    <TextField
                        label="Amount"
                        type="number"
                        fullWidth
                        value={amount}
                        onChange={(e) => setAmount(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <Autocomplete
                        options={scheduleOptions}
                        getOptionLabel={(option) => option.label}
                        renderInput={(params) => <TextField {...params} label="Select Schedule Type" />}
                        value={scheduleType}
                        onChange={(event, newValue) => setScheduleType(newValue)}
                        sx={{ mb: 2 }}
                    />

                    <TextField
                        label="Schedule Date"
                        type="date"
                        fullWidth
                        InputLabelProps={{ shrink: true }}
                        value={scheduleDate}
                        onChange={(e) => setScheduleDate(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <TextField
                        label="Schedule Time"
                        type="time"
                        fullWidth
                        InputLabelProps={{ shrink: true }}
                        value={scheduleTime}
                        onChange={(e) => setScheduleTime(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <TextField
                        label="Description"
                        multiline
                        rows={4}
                        fullWidth
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        sx={{ mb: 2 }}
                    />

                    <FormControlLabel
                        control={<Switch checked={enableNotifications} onChange={(e) => setEnableNotifications(e.target.checked)} />}
                        label="Enable notifications when this transaction is completed"
                        sx={{ mb: 2 }}
                    />

                    <Button variant="contained" color="primary" onClick={() => console.log("Submit Transaction")}>
                        Submit Transaction
                    </Button>
                </Paper>

            </Box>
        </div>

    );
}

export default TransactionBuilder;