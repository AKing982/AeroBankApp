import {
    Button,
    FormControl, IconButton,
    InputLabel, List, ListItem,
    ListItemSecondaryAction, ListItemText,
    MenuItem, Paper,
    Select,
    TextField,
    Typography
} from "@mui/material";
import {Box, Container} from "@mui/system";
import DeleteIcon from "@mui/icons-material/Delete";
import {useEffect, useState} from "react";
import backgroundImage from "./images/pexels-julius-silver-753325.jpg";


export default function AccountInformation({activeStep, handleStepChange, accountInfo, handleAccountInfoChange, handleAddAccount, handleDeleteAccount}) {

    const [newAccounts, setNewAccounts] = useState([]);
    const [isAddButtonEnabled, setIsAddButtonEnabled] = useState(false);
    const [isNextButtonEnabled, setIsNextButtonEnabled] = useState(false);


    useEffect(() => {
        const {accountName, initialBalance, accountType} = accountInfo;
        const isEnabled = accountName && initialBalance && accountType;
        setIsAddButtonEnabled(isEnabled);
        setIsNextButtonEnabled(isEnabled);
    }, [accountInfo]);

    const accountTypes = [
        {code: "01", name: "CHECKING"},
        {code: "02", name: "SAVINGS"},
        {code: "03", name: "RENT"},
        {code: "04", name: "INVESTMENT"}
    ];


    const handleNextButtonClick = (e) => {
        e.preventDefault();
        handleStepChange(activeStep + 1);
    }

    const handleAddAccountClick = () => {
        // You should validate accountInfo here before adding
        const accountData = {
            accountName: accountInfo.accountName,
            initialBalance: accountInfo.initialBalance,
            accountType: accountInfo.accountType,
        };

        // Calling the handleAddAccount function with account data
        handleAddAccount(accountData);

    };

    const handleAccountDeletion = (index) => {
        handleDeleteAccount(index);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // Handle form submission logic here
    };

    return (
        <Container maxWidth="sm">

                <Typography variant="h6" gutterBottom>
                    Account Information
                </Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Account Name"
                        name="accountName"
                        multiline
                        rows={1}
                        value={accountInfo.accountName}
                        onChange={handleAccountInfoChange}
                        margin="normal"
                        required
                    />
                    <TextField
                        fullWidth
                        label="Initial Balance"
                        name="initialBalance"
                        type="number"
                        value={accountInfo.initialBalance}
                        onChange={handleAccountInfoChange}
                        margin="normal"
                        required
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Account Type</InputLabel>
                        <Select
                            name="accountType"
                            value={accountInfo.accountType}
                            onChange={handleAccountInfoChange}
                            required
                        >
                            {accountTypes.map((type) => (
                                <MenuItem key={type.code} value={type.code}>{type.name}</MenuItem>
                                ))}
                        </Select>
                    </FormControl>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleAddAccountClick}
                        style={{ marginTop: '10px' }}
                        disabled={!isAddButtonEnabled}
                    >
                        Add Account
                    </Button>
                </form>
            <Box style={{maxHeight: '300px', overflowY: 'auto'}}>
                <Paper elevation={3} style={{ marginTop: '20px' }}>
                    {accountInfo.accounts.length > 0 && (
                        <>
                            <Typography variant="h6" gutterBottom style={{ padding: '10px' }}>
                                Added Accounts
                            </Typography>
                            <List>
                                {accountInfo.accounts.map((account, index) => (
                                    <ListItem key={index}>
                                        <ListItemText
                                            primary={`${account.accountName} (${account.accountType})`}
                                            secondary={`Initial Balance: $${account.initialBalance}`}
                                        />
                                        <ListItemSecondaryAction>
                                            <IconButton
                                                edge="end"
                                                aria-label="delete"
                                                onClick={() => handleAccountDeletion(index)}
                                            >
                                                <DeleteIcon />
                                            </IconButton>
                                        </ListItemSecondaryAction>
                                    </ListItem>
                                ))}
                            </List>
                        </>
                    )}
                </Paper>
            </Box>
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    size="large"
                    onClick={handleNextButtonClick}
                    disabled={!isNextButtonEnabled}
                    >
                    Next
                </Button>
        </Container>
    );
}