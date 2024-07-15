import React, {useEffect, useState} from "react";
import {Box, Container} from "@mui/system";
import {
    Button,
    FormControl, IconButton,
    InputLabel,
    List,
    ListItem, ListItemSecondaryAction, ListItemText,
    MenuItem,
    Paper,
    Select,
    TextField,
    Typography
} from "@mui/material";
import {SelectChangeEvent} from '@mui/material/Select';
import DeleteIcon from "@mui/icons-material/Delete";

interface AccountType {
    code: string;
    name: string;
}


interface AccountInformationProps{
    activeStep: number;
    handleStepChange: (step: number) => void;
    accountInfo: any;
    handleAccountInfoChange: (e: SelectChangeEvent<string>) => void;
    handleAddAccount: (accountData: any) => void;
    handleDeleteAccount: (index: React.Key | null | undefined) => void;
}


export default function AccountInformation({activeStep, handleStepChange, accountInfo, handleAccountInfoChange, handleAddAccount, handleDeleteAccount} : AccountInformationProps)
{
    const [newAccounts, setNewAccounts] = useState<any[]>([]);
    const [isAddButtonEnabled, setIsAddButtonEnabled] = useState<boolean>(false);
    const [isNextButtonEnabled, setIsNextButtonEnabled] = useState<boolean>(false);


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


    const handleNextButtonClick = (e: React.FormEvent) : void => {
        e.preventDefault();
        handleStepChange(activeStep + 1);
    }

    const handleAddAccountClick = () : void => {
        // You should validate accountInfo here before adding
        const accountData = {
            accountName: accountInfo.accountName,
            initialBalance: accountInfo.initialBalance,
            accountType: accountInfo.accountType,
        };

        // Calling the handleAddAccount function with account data
        handleAddAccount(accountData);

    };

    const handleAccountDeletion = (index: React.Key | null | undefined) : void => {
        handleDeleteAccount(index);
    };

    const handleSubmit = (event: React.FormEvent) => {
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
                    // onChange={handleAccountInfoChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Initial Balance"
                    name="initialBalance"
                    type="number"
                    value={accountInfo.initialBalance}
                    // onChange={handleAccountInfoChange}
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
                                {accountInfo.accounts.map((account: { accountName: any; accountType: any; initialBalance: any; }, index: React.Key | null | undefined) => (
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