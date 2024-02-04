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
import {Container} from "@mui/system";
import DeleteIcon from "@mui/icons-material/Delete";
import {useState} from "react";

export default function AccountInformation() {
    const [accountInfo, setAccountInfo] = useState({
        accountName: '',
        initialBalance: '',
        accountType: '',
    });

    const [newAccounts, setNewAccounts] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState('');

    const handleAccountInfoChange = (event) => {
        const { name, value } = event.target;
        setAccountInfo({
            ...accountInfo,
            [name]: value,
        });
    };

    const handleAddAccount = () => {
        if (accountInfo.accountName && accountInfo.initialBalance && accountInfo.accountType) {
            setNewAccounts([...newAccounts, accountInfo]);
            setAccountInfo({
                accountName: '',
                initialBalance: '',
                accountType: '',
            });
        }
    };

    const handleDeleteAccount = (index) => {
        const updatedAccounts = [...newAccounts];
        updatedAccounts.splice(index, 1);
        setNewAccounts(updatedAccounts);
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
                        <MenuItem value="Checking">Checking</MenuItem>
                        <MenuItem value="Savings">Savings</MenuItem>
                        <MenuItem value="Investment">Investment</MenuItem>
                        <MenuItem value="Rent">Rent</MenuItem>
                        {/* Add more account types as needed */}
                    </Select>
                </FormControl>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleAddAccount}
                    style={{ marginTop: '10px' }}
                >
                    Add Account
                </Button>
            </form>
            <Paper elevation={3} style={{ marginTop: '20px' }}>
                <Typography variant="h6" gutterBottom style={{ padding: '10px' }}>
                    Added Accounts
                </Typography>
                <List>
                    {newAccounts.map((account, index) => (
                        <ListItem key={index}>
                            <ListItemText
                                primary={`${account.accountName} (${account.accountType})`}
                                secondary={`Initial Balance: $${account.initialBalance}`}
                            />
                            <ListItemSecondaryAction>
                                <IconButton
                                    edge="end"
                                    aria-label="delete"
                                    onClick={() => handleDeleteAccount(index)}
                                >
                                    <DeleteIcon />
                                </IconButton>
                            </ListItemSecondaryAction>
                        </ListItem>
                    ))}
                </List>
            </Paper>
        </Container>
    );
}