import {Container} from "@mui/system";
import {
    Button,
    Checkbox,
    FormControl,
    FormControlLabel,
    InputLabel,
    MenuItem,
    Select, Snackbar,
    TextField,
    Typography
} from "@mui/material";
import {useState} from "react";

export default function BillPayView()
{
    const [selectedBiller, setSelectedBiller] = useState('');
    const [fromAccount, setFromAccount] = useState('');
    const [amount, setAmount] = useState('');
    const [paymentDate, setPaymentDate] = useState('');
    const [newBiller, setNewBiller] = useState(false);
    const [billerInfo, setBillerInfo] = useState({ name: '', accountNumber: '' });
    const [openSnackbar, setOpenSnackbar] = useState(false);

    const handlePayment = () => {
        // Handle the payment logic
        setOpenSnackbar(true);
    };

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    return (
        <Container style={{ marginTop: '20px' }}>
            <Typography variant="h4">Bill Pay</Typography>
            <form style={{ marginTop: '20px' }}>
                <FormControl fullWidth style={{ marginBottom: '20px' }}>
                    <InputLabel>Biller</InputLabel>
                    <Select
                        value={selectedBiller}
                        onChange={(e) => setSelectedBiller(e.target.value)}
                    >
                        {/* Populate with available billers */}
                        <MenuItem value="electricity">Electricity</MenuItem>
                        <MenuItem value="water">Water</MenuItem>
                        {/* ... other billers */}
                    </Select>
                </FormControl>

                <FormControlLabel
                    control={<Checkbox checked={newBiller} onChange={(e) => setNewBiller(e.target.checked)} />}
                    label="Add New Biller"
                />

                {newBiller && (
                    <>
                        <TextField
                            fullWidth
                            label="Biller Name"
                            value={billerInfo.name}
                            onChange={(e) => setBillerInfo({ ...billerInfo, name: e.target.value })}
                            style={{ marginBottom: '20px' }}
                        />
                        <TextField
                            fullWidth
                            label="Biller Account Number"
                            value={billerInfo.accountNumber}
                            onChange={(e) => setBillerInfo({ ...billerInfo, accountNumber: e.target.value })}
                            style={{ marginBottom: '20px' }}
                        />
                    </>
                )}

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
                    label="Payment Date"
                    type="date"
                    InputLabelProps={{ shrink: true }}
                    value={paymentDate}
                    onChange={(e) => setPaymentDate(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <Button variant="contained" color="primary" onClick={handlePayment}>
                    Pay Bill
                </Button>
            </form>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="Payment successful"
            />
        </Container>
    );
}
