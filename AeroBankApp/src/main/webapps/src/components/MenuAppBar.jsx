import * as React from 'react';
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import InputBase from '@mui/material/InputBase';
import Badge from '@mui/material/Badge';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import AccountCircle from '@mui/icons-material/AccountCircle';
import MailIcon from '@mui/icons-material/Mail';
import NotificationsIcon from '@mui/icons-material/Notifications';
import {Search} from "@mui/icons-material";
import MoreIcon from '@mui/icons-material/MoreVert';
import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import NavigationMenu from "./NavigationMenu";
import axios from "axios";
import {Autocomplete, Avatar, TextField} from "@mui/material";
import TransactionDetails from "./TransactionDetails";

const StyledInputBase = styled(InputBase)(({ theme }) => ({
    color: 'inherit',
    '& .MuiInputBase-input': {
        padding: theme.spacing(1, 1, 1, 0),
        // vertical padding + font size from searchIcon
        paddingLeft: `calc(1em + ${theme.spacing(4)})`,
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('md')]: {
            width: '20ch',
        },
    },
}));

const SearchIconWrapper = styled(SearchIcon)({
    // Custom styles here
    color: 'blue',
    cursor: 'pointer',
    // other styles as needed
});

function formatAmount(amount, currencyCode = 'USD', locale = 'en-US') {
    const formatter = new Intl.NumberFormat(locale, {
        style: 'currency',
        currency: currencyCode,
        // Additional options can be set here if needed
    });
    return formatter.format(amount);
}

export default function MenuAppBar(){
    const [anchorElNav, setAnchorElNav] = useState(null);
    const [anchorElProfile, setAnchorElProfile] = useState(null);
    const isMenuOpen = Boolean(anchorElNav);
    const isProfileMenuOpen = Boolean(anchorElProfile);
    const navigate = useNavigate();
    const [role, setRole] = useState(null);
    const [balance, setBalance] = useState(0);
    const [accountNumber, setAccountNumber] = useState('');
    const [totalAccounts, setTotalAccounts] = useState(0);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [userIsActive, setUserIsActive] = useState(0);
    const [duration, setDuration] = useState(0);
    const [currentUserLog, setCurrentUserLog] = useState({});
    const [userImage, setUserImage] = useState('');
    const [searchQuery, setSearchQuery] = useState('');
    const location = useLocation();
    const [selectedTransaction, setSelectedTransaction] = useState(null);


    const handleSearchChange = (event) => {
        setSearchQuery(event.target.value);
    };

    const handleSearchSubmit = (event) => {
        event.preventDefault();
        // Implement search functionality based on searchQuery
        console.log("Searching for:", searchQuery);
        // You might want to navigate to the search results page or filter items on the current page
    };

    const handleNavigate = (path) => {
        handleClose();
        navigate(path);
    };

    const StyledMenuItem = styled(MenuItem)(({ theme }) => ({
        background: 'linear-gradient(to bottom, #666666 0%, #4d4d4d 100%)', // Medium grey gradient
        color: 'white', //// Set text color to white for high contrast
        padding: '5px 10px',
        '&:first-of-type': {
            borderTop: 'none', // Explicitly remove border from the first item
        },
        '&:not(:first-of-type)': {
            borderTop: '1px solid #FFFFFF', // Apply top border to all except the first item
        },
        '&:hover': {
            background: 'linear-gradient(to bottom, #4d4d4d 0%, #333333 100%)', // Darker grey gradient on hover
            textDecoration: 'none',
        },
        '& .MuiTypography-root': {
            fontWeight: 'bold',
            color: 'white', // Ensure text in typography also uses white color
        }
    }));

    const handleTransactionSelect = (event, value) => {
        if (value) { // Ensure value is not null before accessing properties
            console.log("Selected Transaction ID:", value.id);
            setSelectedTransaction(value);
        } else {
            setSelectedTransaction(null); // Explicitly set to null when cleared
            console.log("Selection cleared.");
        }
    };

        const StyledMenuIcon = styled(MenuIcon)({
        // Add your custom styles here
        color: 'white', // Example: Change color to white
        fontSize: '48px', // Example: Increase font size
    });

    const isActive = (path) => {
        // Implement your logic to determine if the path is active
        // For example, comparing with the current URL
        return window.location.pathname === path;
    };

    const handleMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };

    const handleProfileMenuOpen = (event) => {
        setAnchorElProfile(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorElNav(null);
        setAnchorElProfile(null);
    };

    const saveUserID = (userID) => {
        sessionStorage.setItem('userID', userID);
    }

    const username = sessionStorage.getItem('username');


    useEffect(() => {
        setIsLoading(true);
        axios.get(`http://localhost:8080/AeroBankApp/api/profile/${username}`)
            .then(response => {
                console.log('User Profile Response: ', response.data);
                setAccountNumber(response.data.accountNumber);
                setTotalAccounts(response.data.totalAccounts);
                setBalance(response.data.totalBalance);
                setRole(response.data.role);
                console.log('Response UserID: ', response.data.userID);
                saveUserID(response.data.userID)

                console.log('Role:', role);
                console.log(response.data);
                console.log(response.data.totalBalance);
                console.log("AccountNumber: ", accountNumber);

            })

            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error);
                setError(error);
                setIsLoading(false);
            })
            .then(() => {
                setIsLoading(false);
            })
    }, []);

    const accounts = [
        { label: 'Account 12345', id: 12345 },
        { label: 'Account 67890', id: 67890 },
        { label: 'Account 54321', id: 54321 },
        // More accounts...
    ];

    async function fetchCurrentUserLogSession(){
        const userID = sessionStorage.getItem('userID');
        console.log('UserID: ', userID);
        return axios.get(`http://localhost:8080/AeroBankApp/api/session/currentSession/${userID}`)
            .then(response => {
                const userLogSession = response.data;
                console.log('Successfully fetched user log id');
                setCurrentUserLog(userLogSession);
                console.log('User Log session: ', response.data);
                return userLogSession;
            })
            .catch(error => {
                console.error('There was an error fetching the current user log id: ', error);
            });
    }


    async function updateUserLogRequest(userID, duration, isActive, loginTime, lastLogout)
    {

        try {
            const session = await fetchCurrentUserLogSession();
            if (!session || !session.id) {
                console.error('Failed to fetch current user log session or session ID not found.');
                return; // Exit function if no session or session ID is found
            }

            const sessionID = session.id;


            console.log('sessionID: ', sessionID);
            console.log('Session: ', session);
            console.log("Type of SessionID: ", typeof sessionID);

            const userLogData = {
                id: sessionID,
                userID: userID,
                lastLogin: loginTime,
                lastLogout: lastLogout,
                sessionDuration: duration,
                isActive: isActive
            }

            console.log('UserLog Sign Out Request: ', userLogData);

            const response = await axios.put(`http://localhost:8080/AeroBankApp/api/session/updateUserLog/${sessionID}`, userLogData);
            console.log('Response Status was ok: ', response.data.ok);
            if (response.status === 200) {
                console.log('User Log Data Successfully posted...', response.data);
            } else {
                console.log(`There was an error updating the User Log: Status ${response.status}`, response.data);
            }
        } catch (error) {
            if(error.response){
                console.error(`Server responded with status ${error.response.status}: `, error.response.data);
            }else if(error.request){
                console.error('No Response received for the update request.', error.request);
            }else{
                console.error('Error setting up the update request: ', error.message);
            }
        }
    }


    const handleLogout = () => {

        const logoutTime = new Date().getTime();
        const lastLoginTime = sessionStorage.getItem('currentLoginTime');
        const loginTime = sessionStorage.getItem('loginTime');
        const loginISO = sessionStorage.getItem('loginISOTime');
        const duration = logoutTime - loginTime;
        const duration_in_seconds = Math.floor(duration / 1000);
        console.log('Duration Logged In: ', duration);
        setDuration(duration_in_seconds);
        const userID = sessionStorage.getItem('userID');
        const currentTime = new Date();
        setUserIsActive(0);

        updateUserLogRequest(userID, duration_in_seconds, 0, lastLoginTime, currentTime.toLocaleString());
        sessionStorage.removeItem('loginTime');
        sessionStorage.clear();

        navigate('/');
    }

    const transactions = [
        {
            id: 'TX123',
            date: '2023-05-01',
            amount: 76.43,
            type: 'Debit',
            status: 'Completed',
            method: 'Credit Card',
            payee: 'Grocery Store',
            description: 'Weekly grocery shopping',
            category: 'Groceries',
            fee: 1.25,
            label: 'Grocery Store - $76.43', // For display in Autocomplete
        },
        {
            id: 'TX124',
            date: '2023-05-02',
            amount: 2000,
            type: 'Credit',
            status: 'Completed',
            method: 'Direct Deposit',
            payee: 'ABC Corp',
            description: 'Monthly salary',
            category: 'Salary',
            fee: 0.00,
            label: 'Salary Deposit - $2,000', // For display in Autocomplete
        },
        {
            id: 'TX125',
            date: '2023-05-03',
            amount: 120.15,
            type: 'Debit',
            status: 'Completed',
            method: 'Automatic Bill Pay',
            payee: 'Electric Company',
            description: 'Monthly electric bill',
            category: 'Utilities',
            fee: 1.50,
            label: 'Electric Bill - $120.15', // For display in Autocomplete
        }
    ];

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static" sx={{
                background: 'linear-gradient(to right, #333333, #1a1a1a)'  // Gradient from dark grey to black
            }}>
                <Toolbar>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        sx={{ mr: 2 }}
                        onClick={handleMenu}
                    >
                        <StyledMenuIcon />
                    </IconButton>


                    {location.pathname.includes('/accounts') && (
                        <Autocomplete
                            disablePortal
                            options={transactions}
                            getOptionLabel={(option) => option.label}
                            sx={{ width: 300, bgcolor: 'background.paper' }}
                            renderInput={(params) => (
                                <TextField {...params} label="Search transactions..." size="small" />
                            )}
                            onChange={handleTransactionSelect}
                            isOptionEqualToValue={(option, value) => option.id === value.id}
                            value={selectedTransaction}
                        />
                    )}
                    {/* Display Account Info */}
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1, ml: 2 }}>
                        AccountNumber: {accountNumber} | Balance: {formatAmount(balance)} | Total Accounts: {totalAccounts}
                    </Typography>
                    <Box sx={{ flexGrow: 1 }} />
                    <IconButton
                        size="large"
                        aria-label="show new mails"
                        color="inherit"
                    >
                        <Badge badgeContent={4} color="error">
                            <MailIcon />
                        </Badge>
                    </IconButton>
                    <IconButton
                        size="large"
                        aria-label="show new notifications"
                        color="inherit"
                    >
                        <Badge badgeContent={17} color="error">
                            <NotificationsIcon />
                        </Badge>
                    </IconButton>
                    <IconButton
                        size="large"
                        edge="end"
                        aria-label="account of current user"
                        aria-haspopup="true"
                        color="inherit"
                        onClick={handleProfileMenuOpen}
                    >
                        {userImage ? (
                            <Avatar src={userImage} alt="Profile" />
                        ) : (
                            <AccountCircle />
                        )}
                    </IconButton>
                    <NavigationMenu
                        anchorEl={anchorElNav}
                        isOpen={isMenuOpen}
                        onClose={handleClose}
                        handleNavigation={handleNavigate}
                        userRole={role}
                        isActive={isActive} />
                    <Menu
                        id="profile-menu"
                        anchorEl={anchorElProfile}
                        anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        keepMounted
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        open={isProfileMenuOpen}
                        onClose={handleClose}
                    >
                        <StyledMenuItem onClick={() => handleNavigate("/profile")}>
                            <Typography>Profile</Typography>
                        </StyledMenuItem>
                        <StyledMenuItem onClick={() => handleNavigate("/myAccount")}>
                            <Typography>My account</Typography>
                        </StyledMenuItem>
                        <StyledMenuItem onClick={() => handleLogout()}>
                            <Typography>Logout</Typography>
                        </StyledMenuItem>
                    </Menu>
                </Toolbar>
                {selectedTransaction && (
                    <TransactionDetails transaction={selectedTransaction} />
                )}
            </AppBar>
        </Box>
    );
}