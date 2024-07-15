import {useLocation, useNavigate} from "react-router-dom";
import React, {useEffect, useState} from "react";
import UserSessionUtils from "../main/UserSessionUtils";
import axios from "axios";
import PayeesContentPage from "../components/PayeesContentPage";
import TransfersContentPage from "../components/TransfersContentPage";
import CalendarsContentPage from "../components/CalendarsContentPage";
import BillPaymentHistoryPage from "../components/BillPaymentHistoryPage";
import {Box} from "@mui/system";
import MenuAppBar from "../components/MenuAppBar";
import {
    Button, Chip,
    Divider,
    Grid,
    IconButton,
    InputAdornment, Link, MenuItem,
    Paper, Select,
    Tab, Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Tabs,
    TextField,
    Typography
} from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
// @ts-ignore
import backgroundImg from "../components/images/pexels-pixabay-210307.jpg";
import RemoveIcon from "@mui/icons-material/Remove";
import AddIcon from "@mui/icons-material/Add";
import CalendarTodayIcon from "@mui/icons-material/CalendarToday";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import VisibilityIcon from "@mui/icons-material/Visibility";
import HistoryIcon from "@mui/icons-material/History";

interface UserInfo {
    name: string;
    email: string;
    lastLogin: string;
}

interface SessionUtils{
    handleLogout: () => void;
    currentUserLog: any;
}

interface PaymentsContentProps{
    userInfo: UserInfo;
}

function BillPaymentPage() {
    const navigate = useNavigate();
    const location = useLocation();
    const [userInfo, setUserInfo] = useState<UserInfo>({
        name: '',
        email: '',
        lastLogin: ''
    });
    const [tabValue, setTabValue] = useState<number>(0);
    const {handleLogout, currentUserLog} : SessionUtils = UserSessionUtils();
    const userID = sessionStorage.getItem('userID');

    useEffect(() => {
        const path = location.pathname.split('/')[2] || 'Payments';
        setTabValue(['Payments', 'Payees', 'Transfers', 'Calendars'].indexOf(path));
    }, [location]);

    const handleTabChange = (event: React.SyntheticEvent, newValue: number): void => {
        const tabs = ['Payments', 'Payees', 'Transfers', 'Calendar'];

        navigate(`/billPay/${tabs[newValue]}?userID=${userID}`);
    }


    const handleViewHistory = () : void => {
        setTabValue(4);
        navigate('/billPay/Payments/PaymentHistory');
    }

    const handlePending = () : void => {
        navigate('/billPay/Payments/Pending');
    }

    useEffect(() => {
        axios.get(`http://localhost:8080/AeroBankApp/api/profile/${userID}/data`)
            .then(response => {
                const responseData = response.data;
                console.log("User Profile Data Response: ", responseData);
                setUserInfo(responseData);
            })
            .catch(error => {
                console.error("There was an error fetching the User Profile Data: ", error);
            })
    }, [])

    const renderTabContent = ()  => {
        switch(tabValue)
        {
            case 0:
                return <PaymentsContent userInfo={userInfo} />
            case 1:
                return <PayeesContentPage />
            case 2:
                return <TransfersContentPage viewHistory={handleViewHistory}/>
            case 3:
                return <CalendarsContentPage />
            case 4:
                return <BillPaymentHistoryPage />
            default:
                return <PaymentsContent userInfo={userInfo}/>
        }
    };

    // @ts-ignore
    // @ts-ignore
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
            <Box sx={{
                bgcolor: 'primary.main',
                color: 'black',
                p: 2,
                mb: 2,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'flex-end',
                gap: 1
            }}>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Typography variant="body2">
                        Welcome {userInfo.name}
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
                        onClick={handleLogout}
                        startIcon={<LogoutIcon />}
                    >
                        Log out
                    </Button>
                </Box>
            </Box>
            {renderTabContent()}
        </Box>

    );
}

const PaymentsContent: React.FC<PaymentsContentProps> = ({userInfo}) => {
    const navigate = useNavigate();
    const handleViewHistory = () => {
        navigate('/billPay/Payments/PaymentHistory');
    }

    const handlePending = () => {
        navigate('/billPay/Payments/Pending');
    }

    return (
        <div
            style={{
                background: `url(${backgroundImg}) no-repeat center bottom`,
                backgroundSize: 'cover',
                minHeight: 'calc(120vh - 64px)',
                width: '100%',
                position: 'relative',
            }}
        >
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
                            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                                <Button variant="contained" startIcon={<AddIcon />}>
                                    Payee
                                </Button>
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
                                <Button variant="outlined" startIcon={<VisibilityIcon />} onClick={handlePending}>
                                    View pending transactions
                                </Button>
                                <Button variant="outlined" startIcon={<HistoryIcon />} onClick={handleViewHistory}>
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
        </div>

    )
}


export default BillPaymentPage;