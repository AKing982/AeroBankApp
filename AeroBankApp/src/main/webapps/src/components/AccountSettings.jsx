import UserTextField from "./UserTextField";
import {useState} from "react";
import AccountTypeSelect from "./AccountTypeSelect";
import NumberField from "./NumberField";
import UserAccountAccess from "./UserAccountAccess";
import {Button, Grid, Typography} from "@mui/material";
import '../AccountSettings.css';
import PercentageField from "./PercentageField";

export default function AccountSettings()
{
    const [accountName, setAccountName] = useState(null);
    const [accountType, setAccountType] = useState(null);
    const [isAccountEnabled, setIsAccountEnabled] = useState(false);
    const [withdrawalLimit, setWithdrawalLimit] = useState(0);
    const [transferLimit, setTransferLimit] = useState(0);
    const [overdraftEnabled, setOverdraftEnabled] = useState(false);
    const [interestRate, setInterestRate] = useState(0);
    const [balance, setBalance] = useState(0);
    const [isSaved, setIsSaved] = useState(false);

    const handleAccountNameChange = (event) => {
        setAccountName(event.target.value);
    }

    const handleAccountTypeChange = (event, newValue) => {
        if(newValue !== null && newValue !== undefined)
        {
            setAccountType(newValue);
        }
    }

    const handleWithdrawalLimitChange = (event) => {
        setWithdrawalLimit(event.target.value);
    }

    const handleTransferLimitChange = (event) => {
        setTransferLimit(event.target.value);
    }

    const handleInterestRateChange = (event, newValue) => {
        setInterestRate(newValue);
    }

    const handleBalanceChange = (event) => {
        setBalance(event.target.value);
    }

    const handleSaveChanges = (event) => {
        setIsSaved(event.target.value);
    }

    const usersList = ['User1', 'User2', 'User3']; // Replace with actual user data

    return (
        <div className="account-settings-container">
            <Grid container spacing={2}>
                <Grid item xs={12} md={6}>
                    {/* Left side - Account Properties */}
                    <div className="account-properties">
                        <Grid container spacing={2} alignItem="center">
                            <Grid item xs={4}>
                                <Typography>Account Name:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <UserTextField label="Account Name" onChange={handleAccountNameChange} value={accountName}/>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography>Account Type:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <AccountTypeSelect value={accountType} onChange={handleAccountTypeChange}/>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography>Balance:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <NumberField value={balance} label="Balance" onChange={handleBalanceChange}/>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography>Withdraw Limit:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <NumberField value={withdrawalLimit} label="Withdrawal Limit" onChange={handleWithdrawalLimitChange}/>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography>Transfer Limit:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <NumberField value={transferLimit} label="Transfer Limit" onChange={handleTransferLimitChange}/>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography>Interest Rate:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <PercentageField value={interestRate} label="Interest Rate" onChange={handleInterestRateChange}/>
                            </Grid>
                            <Grid item xs={8}>
                                <Button
                                    variant="contained"
                                    color="primary"
                                    onClick={handleSaveChanges}
                                    disabled={isSaved}
                                >
                                    Save
                                </Button>
                            </Grid>
                        </Grid>






                        {/* Include Overdraft access and other fields if needed */}
                    </div>
                </Grid>
                <Grid item xs={12} md={6}>
                    {/* Right side - List of users */}
                    <div className="users-list">
                        {usersList.map(user => (
                            <div key={user}>{user}</div> // Replace with your list component
                        ))}
                    </div>
                </Grid>
                <Grid item xs={12}>
                    {/* Bottom - User Account Access */}
                    <UserAccountAccess />
                </Grid>
            </Grid>
        </div>
    );
}