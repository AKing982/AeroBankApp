import AccountBox from "./AccountBox";
import '../AccountBox.css';
import './TransactionView.css';
import ListView from "./AccountListView";
import {
    Divider,
    Grid, IconButton, Link,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Paper,
    SwipeableDrawer,
    Typography
} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import TransactionTable from "./TransactionTable";
import PendingTransactionsTable from "./PendingTransactionsTable";
import {Box, Container} from "@mui/system";
import Home from "./Home";
import MenuAppBar from "./MenuAppBar";
import GradientSeparator from "./GradientSeparator";
import backgroundImage from './images/pexels-pixabay-210307.jpg';
import MailIcon from "@mui/icons-material/Mail";
import MenuIcon from '@mui/icons-material/Menu'; // Icon for the drawer button
import InboxIcon from '@mui/icons-material/MoveToInbox';

import DashboardIcon from '@mui/icons-material/Dashboard';
import HistoryIcon from '@mui/icons-material/History';
import SwapHorizIcon from '@mui/icons-material/SwapHoriz';
import PaymentIcon from '@mui/icons-material/Payment';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import PieChartIcon from '@mui/icons-material/PieChart';
import SettingsIcon from '@mui/icons-material/Settings';
import HelpIcon from '@mui/icons-material/Help';
import AccountOverview from "./AccountOverview";

export default function TransactionView()
{
    const [accountID, setAccountID] = useState(0);
    const [fullName, setFullName] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [drawerOpen, setDrawerOpen] = useState(false);

    const toggleDrawer = (open) => (event) => {
        if (event && event.type === 'keydown' && (event.key === 'Tab' || event.key === 'Shift')) {
            return;
        }
        setDrawerOpen(open);
    };

    const DrawerContent = ({ toggleDrawer }) => (
        <Box
            sx={{ width: 250 }}
            role="presentation"
            onClick={toggleDrawer(false)}
            onKeyDown={toggleDrawer(false)}
        >
            <List>
                {drawerItems.map(({ text, icon }) => (
                    <ListItem button key={text}>
                        <ListItemIcon>{icon}</ListItemIcon>
                        <ListItemText primary={text} />
                    </ListItem>
                ))}
            </List>
        </Box>
    );

    const Footer = () => (
        <Box component="footer" sx={{ bgcolor: 'background.paper', py: 6 }}>
            <Container maxWidth="lg">
                <Grid container spacing={4}>
                    <Grid item xs={12} sm={4}>
                        <Typography variant="h6">Resources</Typography>
                        <Link href="#" sx={{ display: 'block' }}>FAQs</Link>
                        <Link href="#" sx={{ display: 'block' }}>Contact Us</Link>
                        <Link href="#" sx={{ display: 'block' }}>Download App</Link>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <Typography variant="h6">Legal</Typography>
                        <Link href="#" sx={{ display: 'block' }}>Terms & Conditions</Link>
                        <Link href="#" sx={{ display: 'block' }}>Privacy Policy</Link>
                        <Link href="#" sx={{ display: 'block' }}>Regulatory Info</Link>
                    </Grid>
                    <Grid item xs={12} sm={4}>
                        <Typography variant="h6">Tools</Typography>
                        <Link href="#" sx={{ display: 'block' }}>Currency Converter</Link>
                        <Link href="#" sx={{ display: 'block' }}>Tax Tools</Link>
                        <Link href="#" sx={{ display: 'block' }}>Security Tips</Link>
                    </Grid>
                </Grid>
            </Container>
        </Box>
    );

    const drawerItems = [
        { text: 'Accounts Overview', icon: <DashboardIcon /> },
        { text: 'Transaction History', icon: <HistoryIcon /> },
        { text: 'Transfer Funds', icon: <SwapHorizIcon /> },
        { text: 'Payment Services', icon: <PaymentIcon /> },
        { text: 'Investments', icon: <TrendingUpIcon /> },
        { text: 'Budgets and Planning', icon: <PieChartIcon /> },
        { text: 'Settings', icon: <SettingsIcon /> },
        { text: 'Support', icon: <HelpIcon /> }
    ];

    useEffect(() => {
        const fetchUsersName = () => {
            const userID = sessionStorage.getItem('userID');
            axios.get(`http://localhost:8080/AeroBankApp/api/users/name/${userID}`)
                .then(response => {
                    console.log('Full Name Response: ', response.data);
                    setFirstName(response.data.firstName);
                    setLastName(response.data.lastName);
                })
                .catch(error => {
                    console.error('An Error occurred fetching the users full name: ', error);
                });
        }
        fetchUsersName();

    }, [])

    function TimeGreeting({name}) {
        const [greeting, setGreeting] = useState('');

        useEffect(() => {
            const updateGreeting = () => {
                const now = new Date();
                const hours = now.getHours();
                let currentGreeting = '';

                if(hours >= 5 && hours < 12)
                {
                    currentGreeting = 'Good Morning';
                }
                else if(hours >= 12 && hours < 17)
                {
                    currentGreeting = 'Good Afternoon';
                }
                else if(hours >= 17 || hours < 5)
                {
                    currentGreeting = 'Good Evening';
                }

                setGreeting(currentGreeting);
            };

            updateGreeting();

            const timerID = setInterval(updateGreeting, 3600000);

            return function cleanup()
            {
                clearInterval(timerID);
            }
        }, []);


        return (
            <span style={{
                color: '#555', // Slightly darker text for the greeting
                textShadow: '1px 1px 1px #eee' // Subtle shadow for depth
            }}>
            {greeting},
            <span style={{ color: '#000', fontWeight: 'bold' }}>{name}</span>
        </span>
        );

    }

    return (
        <div style={{
            background: `url(${backgroundImage}) no-repeat center bottom`,
            backgroundSize: 'cover',
            minHeight: 'calc(120vh - 64px)',
            width: '100%',
            position: 'relative',
        }}>
            <MenuAppBar />
            <IconButton
                color="inherit"
                aria-label="open drawer"
                edge="start"
                sx={{ margin: 1, position: 'absolute', top: 10, left: 10 }}
                onClick={toggleDrawer(true)}
            >
                <MenuIcon />
            </IconButton>
            <SwipeableDrawer
                anchor="left"
                open={drawerOpen}
                onClose={toggleDrawer(false)}
                onOpen={toggleDrawer(true)}
            >
                <DrawerContent toggleDrawer={toggleDrawer}/>
            </SwipeableDrawer>

            <Box sx={{ flexGrow: 1}}>
                <Grid container spacing={1}>
                    <Grid item xs={12} md={6}>
                        <AccountOverview
                            firstName={firstName}
                            totalBalance="$1500"
                            recentTransaction="Paid Rent"
                            alerts="No New Alerts"
                        />
                        <Paper elevation={3} sx={{
                            margin: 2,
                            padding: 2,
                            backgroundColor: 'rgba(255, 255, 255, 0.95)', // Slightly transparent background to blend with the backdrop
                            backdropFilter: 'blur(10px)', // Softening the background image underneath for better readability
                            border: '1px solid #ccc', // Adding a subtle border to define the Paper's boundaries
                            borderRadius: '8px', // Soft rounded corners for a smoother look
                            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)' // Enhancing the shadow for better depth perception
                        }}>
                            <ListView updateAccountID={setAccountID} />
                        </Paper>
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <Paper elevation={3}
                               sx={{
                                   margin: 2,
                                   padding: 2,
                                   backdropFilter: 'blur(10px)',
                                   border: '1px solid #ccc',
                                   borderRadius: '8px',
                                   height: 'auto',
                                   width: { xs: '100%', sm: '90%', md: '100%' }, // Responsive widths
                                   maxWidth: '1200px', // Maximum width to ensure it doesn't get too wide on large screens
                                   mx: 'auto' // Centers the Paper component within its container
                               }}>
                            <PendingTransactionsTable accountID={accountID} />
                            <TransactionTable accountID={accountID} />
                        </Paper>
                    </Grid>
                </Grid>
                <Box sx={{ pt: 2, textAlign: 'center' }}>
                    {/* Footer content can go here */}
                    <Footer />
                </Box>
            </Box>
        </div>

    );
}