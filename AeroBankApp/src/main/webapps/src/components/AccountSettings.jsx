import UserTextField from "./UserTextField";
import {useState} from "react";
import AccountTypeSelect from "./AccountTypeSelect";
import NumberField from "./NumberField";
import UserAccountAccess from "./UserAccountAccess";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select, TextField, Typography} from "@mui/material";
import '../AccountSettings.css';
import PercentageField from "./PercentageField";
import {Container} from "@mui/system";

export default function AccountSettings()
{
    const [accountDetails, setAccountDetails] = useState({
        accountName: '',
        accountType: '',
        withdrawalLimit: 0,
        transferLimit: 0,
        overdraftEnabled: false,
        interestRate: 0,
        balance: 0
    });

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setAccountDetails(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSaveChanges = () => {
        console.log("Account details to save:", accountDetails);
        // Add save logic here
    };

    const accountTypes = ['Checking', 'Savings', 'Credit'];

    return (
        <Container maxWidth="lg">
            <Grid container spacing={3}>
                <Grid item xs={12} md={6}>
                    {/* Account Details Form */}
                    <Typography variant="h6">Account Details</Typography>
                    <TextField
                        fullWidth
                        label="Account Name"
                        name="accountName"
                        value={accountDetails.accountName}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <FormControl fullWidth margin="normal">
                        <InputLabel>Account Type</InputLabel>
                        <Select
                            name="accountType"
                            value={accountDetails.accountType}
                            label="Account Type"
                            onChange={handleInputChange}
                        >
                            {accountTypes.map(type => (
                                <MenuItem key={type} value={type}>{type}</MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                    <TextField
                        fullWidth
                        label="Balance"
                        name="balance"
                        type="number"
                        value={accountDetails.balance}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        label="Withdrawal Limit"
                        name="withdrawalLimit"
                        type="number"
                        value={accountDetails.withdrawalLimit}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        label="Transfer Limit"
                        name="transferLimit"
                        type="number"
                        value={accountDetails.transferLimit}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <TextField
                        fullWidth
                        label="Interest Rate (%)"
                        name="interestRate"
                        type="number"
                        value={accountDetails.interestRate}
                        onChange={handleInputChange}
                        margin="normal"
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleSaveChanges}
                    >
                        Save Changes
                    </Button>
                </Grid>
                <Grid item xs={12} md={6}>
                    {/* User List */}
                    <Typography variant="h6">Users List</Typography>
                    {/* Implement UserList component */}
                </Grid>
                <Grid item xs={12}>
                    {/* User Account Access Component Below */}
                    <Typography variant="h6">User Account Access</Typography>
                    <UserAccountAccess />
                </Grid>
            </Grid>
        </Container>
    );
}