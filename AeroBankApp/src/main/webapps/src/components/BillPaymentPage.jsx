import {
    Alert,
    Button, Chip, Divider, FormControl,
    Grid, IconButton, InputAdornment, InputLabel, Link,
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
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import VisibilityIcon from '@mui/icons-material/Visibility';
import HistoryIcon from '@mui/icons-material/History';
import RemoveIcon from '@mui/icons-material/Remove';
import LogoutIcon from '@mui/icons-material/Logout';
import EmailIcon from '@mui/icons-material/Email';
import SettingsIcon from '@mui/icons-material/Settings';
import AddIcon from "@mui/icons-material/Add";
import {AlertTitle} from "@mui/lab";

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
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                                <Typography variant="h6">Schedule</Typography>
                                <IconButton size="small">
                                    <RemoveIcon />
                                </IconButton>
                            </Box>
                            <Alert severity="info" sx={{ mb: 2 }} onClose={() => {}}>
                                <AlertTitle>Our goal is to deliver your payment securely and quickly.</AlertTitle>
                                Some payments will process using a single-use, pre-paid card, which means you will not recognize card numbers within payment confirmation communications you receive.
                            </Alert>

                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                                <Button variant="contained" startIcon={<AddIcon />}>
                                    Payee
                                </Button>
                                <Box>
                                    <Button variant="outlined" sx={{ mr: 1 }}>Review all</Button>
                                    <Button variant="contained" color="primary">Pay all</Button>
                                </Box>
                            </Box>

                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>

                                <TextField
                                    placeholder="Payee name or nickname"
                                    variant="outlined"
                                    size="small"
                                    InputProps={{
                                        endAdornment: (
                                            <InputAdornment position="end">
                                                <Button variant="contained" size="small">Search</Button>
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Box>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Pay to</TableCell>
                                            <TableCell>Pay from</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Deliver Date</TableCell>
                                            <TableCell align="right">Actions</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>
                                                <Typography variant="body1" color="primary">
                                                    STATE FARM INS AUTO LIFE FIRE HEALTH PAYMENTS ONLY
                                                </Typography>
                                                <Typography variant="body2" color="textSecondary">
                                                    *6-20
                                                </Typography>
                                                <Typography variant="body2" color="textSecondary">
                                                    Last paid: $78.96 on 05/13/2021
                                                </Typography>
                                                <Typography variant="body2" color="primary">
                                                    STATE FARM INS AUTO LIFE FIRE HEALTH PAYMENTS ONLY
                                                </Typography>
                                                <Chip label="Electronic" size="small" color="primary" />
                                            </TableCell>
                                            <TableCell>
                                                <Select value="primary" fullWidth>
                                                    <MenuItem value="primary">Primary Acct. *2242</MenuItem>
                                                </Select>
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    defaultValue="$ 0.00"
                                                    InputProps={{
                                                        startAdornment: <InputAdornment position="start">$</InputAdornment>,
                                                    }}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    type="date"
                                                    defaultValue="2024-06-24"
                                                    InputProps={{
                                                        endAdornment: (
                                                            <InputAdornment position="end">
                                                                <IconButton>
                                                                    <CalendarTodayIcon />
                                                                </IconButton>
                                                            </InputAdornment>
                                                        ),
                                                    }}
                                                />
                                                <Typography variant="body2" color="textSecondary">
                                                    Deliver by: 06/26/2024
                                                </Typography>
                                            </TableCell>
                                            <TableCell align="right">
                                                <Button variant="contained" color="primary">
                                                    $ Pay
                                                </Button>
                                                <IconButton>
                                                    <MoreVertIcon />
                                                </IconButton>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2, mb: 2 }}>
                                <Typography variant="body2">
                                    <Link href="#" color="primary">
                                        Make it recurring
                                    </Link>
                                </Typography>
                            </Box>
                            <Divider />
                            <Box sx={{ mt: 2 }}>
                                <Typography variant="body1" gutterBottom>
                                    Totals
                                </Typography>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                                    <Typography variant="body2">Primary Account</Typography>
                                    <Typography variant="body2">$0.00</Typography>
                                </Box>
                                <Box sx={{ display: 'flex', justifyContent: 'space-between', fontWeight: 'bold' }}>
                                    <Typography variant="body1">Payment total</Typography>
                                    <Typography variant="body1">$0.00</Typography>
                                </Box>
                            </Box>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2 }}>
                                <Button variant="outlined" startIcon={<VisibilityIcon />}>
                                    View pending transactions
                                </Button>
                                <Button variant="outlined" startIcon={<HistoryIcon />}>
                                    View history
                                </Button>
                                <Box>
                                    <Button variant="outlined" sx={{ mr: 1 }}>
                                        Review all
                                    </Button>
                                    <Button variant="contained" color="primary">
                                        Pay all
                                    </Button>
                                </Box>
                            </Box>
                        </Paper>
                    </Grid>

                    <Grid item xs={12} md={4}>
                        <Box sx={{
                            bgcolor: 'primary.main',
                            color: 'black',
                            p: 2,
                            mb: 2,
                            display: 'flex',
                            flexDirection: 'column',
                            gap: 1
                        }}>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                <Typography variant="body2">
                                    Welcome Alexander King
                                </Typography>
                                <Divider orientation="vertical" flexItem sx={{bgcolor: 'white', mx: 1}}/>
                                <Typography variant="body2">
                                    {userInfo.email}
                                </Typography>
                                <Divider orientation="vertical" flexItem sx={{bgcolor: 'white', mx: 1}}/>
                                <Typography variant="body2">
                                    Last login: {userInfo.lastLogin}
                                </Typography>
                                <Divider orientation="vertical" flexItem sx={{bgcolor: 'white', mx: 1}}/>
                                <Button
                                    variant="text"
                                    color="inherit"
                                    size="small"
                                    startIcon={<LogoutIcon />}
                                >
                                    Log out
                                </Button>
                            </Box>
                        </Box>

                        <Paper sx={{ p: 2, mb: 2 }}>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
                                <Typography variant="h6">Pending</Typography>
                                <IconButton size="small">
                                    <RemoveIcon />
                                </IconButton>
                            </Box>
                            <Typography variant="body2" sx={{ mb: 2 }}>Processing in next 45 days</Typography>
                            <TableContainer component={Paper} variant="outlined">
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Payee</TableCell>
                                            <TableCell align="right">Amount</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {/* Add rows here if there are any pending payments */}
                                        <TableRow>
                                            <TableCell colSpan={2} align="right">
                                                <Typography variant="body2" fontWeight="bold">
                                                    Total $0.00
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
                                <Button variant="outlined" size="small">View more</Button>
                            </Box>
                        </Paper>

                        <Paper sx={{ p: 2 }}>
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
                                <Typography variant="h6">History</Typography>
                                <IconButton size="small">
                                    <RemoveIcon />
                                </IconButton>
                            </Box>
                            <Typography variant="body2" sx={{ mb: 2 }}>Processed in last 45 days</Typography>
                            <TableContainer component={Paper} variant="outlined">
                                <Table size="small">
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Payee</TableCell>
                                            <TableCell align="right">Amount</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {/* Add rows here if there are any historical payments */}
                                        <TableRow>
                                            <TableCell colSpan={2} align="right">
                                                <Typography variant="body2" fontWeight="bold">
                                                    Total $0.00
                                                </Typography>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 2 }}>
                                <Button variant="outlined" size="small">View more</Button>
                            </Box>
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