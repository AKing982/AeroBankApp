import {
    Alert,
    Button,
    Grid,
    MenuItem,
    Paper,
    Select, Tab, Table,
    TableBody,
    TableCell, TableContainer, TableHead,
    TableRow, Tabs,
    TextField,
    Typography
} from "@mui/material";
import {Box} from "@mui/system";
import MenuAppBar from "./MenuAppBar";
import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";

function TabPanel({children, value, index})
{
    return (
        <div role="tabpanel" hidden={value !== index}>
            {value === index && <Box sx={{p: 3}}>{children} </Box>}
        </div>
    );
}

function BillPaymentPage()
{
    const navigate = useNavigate();
    const location = useLocation();
    const [userInfo, setUserInfo] = useState({
        name: 'Alexander King',
        email: 'alex@utahkings.com',
        lastLogin: '11:31 AM on 06/22/2024'
    });
    const [tabValue, setTabValue] = useState(0);

    useEffect(() => {
        const path = location.pathname.split('/')[2] || 'Payments';
        setTabValue(['Payments', 'Payees', 'Transfers', 'Calendars'].indexOf(path));
    }, [location]);

    const handleTabChange = (event, newValue) => {
        const tabs = ['Payments', 'Payees', 'Transfers', 'Calendar'];
        navigate(`/billPay/${tabs[newValue]}?userID=1`);
    }

    const renderTabContent = () => {
        switch(tabValue)
        {
            case 0:
                return <PaymentsContent />
            case 1:
                return <PayeesContent />
            case 2:
                return <TransfersContent />
            case 3:
                return <CalendarsContent />
            default:
                return <PaymentsContent />
        }
    };

    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
            <MenuAppBar />
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={tabValue} onChange={handleTabChange} aria-label="payment tabs">
                    <Tab label="Payments" />
                    <Tab label="Payees" />
                    <Tab label="Transfers" />
                    <Tab label="Calendar" />
                </Tabs>
            </Box>
            <Box sx={{ flexGrow: 1, p: 3 }}>
                <Typography variant="h4" gutterBottom>
                    Payments
                </Typography>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={8}>
                        <Paper sx={{ p: 2, mb: 2 }}>
                            <Typography variant="h6" gutterBottom>
                                Schedule
                            </Typography>
                            <Alert severity="info" sx={{ mb: 2 }}>
                                Our goal is to deliver your payment securely and quickly.
                                Some payments will process using a single-day, pre-paid card, which means you will not recognize card numbers within payment confirmation communications you receive.
                            </Alert>
                            <Button variant="contained" sx={{ mb: 2 }}>
                                + Payee
                            </Button>
                            <Box sx={{ mb: 2 }}>
                                <TextField placeholder="Search payee or nickname" fullWidth />
                            </Box>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Pay to</TableCell>
                                            <TableCell>Account</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Date</TableCell>
                                            <TableCell>Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>STATE FARM INS AUTO LIFE FIRE HEALTH PAYMENTS O</TableCell>
                                            <TableCell>
                                                <Select value="primary" fullWidth>
                                                    <MenuItem value="primary">Primary Acct - *7242</MenuItem>
                                                </Select>
                                            </TableCell>
                                            <TableCell>
                                                <TextField defaultValue="$ 0.00" />
                                            </TableCell>
                                            <TableCell>
                                                <TextField type="date" defaultValue="2024-06-24" />
                                            </TableCell>
                                            <TableCell>
                                                <Button variant="contained" color="primary">
                                                    $ Pay
                                                </Button>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                                <Typography>Totals</Typography>
                                <Typography>Payment total: $0.00</Typography>
                            </Box>
                            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
                                <Button variant="contained" color="primary">
                                    Pay all
                                </Button>
                            </Box>
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={4}>
                        <Paper sx={{ p: 2, mb: 2 }}>
                            <Typography variant="h6" gutterBottom>
                                Pending
                            </Typography>
                            <Typography>Processing in next 45 days</Typography>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                                <Typography>Total</Typography>
                                <Typography>$0.00</Typography>
                            </Box>
                            <Button variant="text" color="primary">
                                View more
                            </Button>
                        </Paper>
                        <Paper sx={{ p: 2 }}>
                            <Typography variant="h6" gutterBottom>
                                History
                            </Typography>
                            <Typography>Processed in last 45 days</Typography>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                                <Typography>Total</Typography>
                                <Typography>$0.00</Typography>
                            </Box>
                            <Button variant="text" color="primary">
                                View more
                            </Button>
                        </Paper>
                    </Grid>
                </Grid>
            </Box>
        </Box>
    );
}

function PaymentsContent() {
    // Your existing Payments content here
}

function PayeesContent() {
    return <Typography variant="h4">Payees Content</Typography>;
}

function TransfersContent() {
    return <Typography variant="h4">Transfers Content</Typography>;
}

function CalendarsContent() {
    return <Typography variant="h4">Calendar Content</Typography>;
}

export default BillPaymentPage;