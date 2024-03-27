import '../Home.css';
import HomeTab from "./HomeTab";
import {useEffect, useState} from "react";
import TransactionView from "./TransactionView";
import DepositView from "./DepositView";
import WithdrawView from "./WithdrawView";
import TransferView from "./TransferView";
import SettingsView from "./SettingsView";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import BasicButton from "./BasicButton";
import CustomTabPanel from "./CustomTabPanel";
import BillPayView from "./BillPayView";
import BasicTabs from "./CustomTabPanel";
import DashBoard from "./DashBoard";
import {Box, Container} from "@mui/system";
import {Button, Typography} from "@mui/material";



export default function Home()
{

    const [balance, setBalance] = useState(0);
    const [accountNumber, setAccountNumber] = useState('');
    const [totalAccounts, setTotalAccounts] = useState(0);
    const [userID, setUserID] = useState(0);

    const [data, setData] = useState({balance: 0, accountNumber: '', totalAccounts:0 })

    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('Transactions');
    const [role, setRole] = useState(null);
    const [userIsActive, setUserIsActive] = useState(0);
    const [duration, setDuration] = useState(0);
    const [currentUserLog, setCurrentUserLog] = useState({});

    const navigate = useNavigate();

    const route = '/';

    const username = sessionStorage.getItem('username');
    const backgroundImage = '/images/aerobank3copy.jpg';

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

    const handleUserInteraction = () => {

    };

    const saveUserID = (userID) => {
        sessionStorage.setItem('userID', userID);
    }


    const handleTabClick = (tab) => {
        setActiveTab(tab);
    }

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

    function CurrentTime()
    {
        const [time, setTime] = useState(new Date());

        useEffect(() => {
            const timerID = setInterval(() => setTime(new Date()), 1000);

            return function cleanup() {
                clearInterval(timerID);
            };
        }, []);

        return <span>{time.toLocaleTimeString()}</span>;
    }

    const formatAmount = (amount) => {
        if(amount == null)
        {
            return 0.0;
        }
        return amount.toLocaleString();
    }

    function TimeGreeting({username}) {
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


        return <span>{greeting}, {username}</span>;

    }

    const tabContent = {
        'Dashboard': <DashBoard />,
        'Transactions': <TransactionView />,
        'Make a Deposit': <DepositView />,
        'Make a Withdraw': <WithdrawView />,
        'Make a Transfer': <TransferView />,
        'Bill Pay': <BillPayView />,
        'Settings': <SettingsView />
    };

    useEffect(() => {
        document.addEventListener('click', handleUserInteraction);
        document.addEventListener('keypress', handleUserInteraction);

        return () => {
            document.removeEventListener('click', handleUserInteraction);
            document.removeEventListener('keypress', handleUserInteraction);
        };


    }, []);

    return (
        <Container maxWidth={false} sx={{
            fontFamily: 'Arial, sans-serif',
            margin: 0,
            padding: 0,
            boxSizing: 'border-box',
            width: '100%', // ensure full width
        }}>
            <Box sx={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                background: 'linear-gradient(to right, #0E0F52, #2A5058)', // Linear gradient for the whole header
                color: 'white',
                padding: '15px',
                flexWrap: 'wrap',
            }}>
                <Box sx={{
                    position: 'relative', // Position relative for the pseudo-element
                    flex: 1,
                    display: 'flex',
                    '::before': { // Pseudo-element for the background image
                        content: '""',
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        width: '50%', // Half width for the background image
                        height: '100%',
                        backgroundImage: `url(${backgroundImage})`,
                        backgroundSize: 'cover',
                        backgroundPosition: 'left center', // aligned to the left
                        backgroundRepeat: 'no-repeat', // prevent repeating the background
                        zIndex: 0, // Ensure the pseudo-element is behind the Box content
                    }
                }}>
                    {/* Invisible Box to keep the space for the background */}
                    <Box sx={{ flex: 1 }} />
                </Box>
                <Box sx={{
                    flex: 1, // Takes the remaining space
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'flex-end',
                    justifyContent: 'center',
                    zIndex: 1, // Ensure the content is above the pseudo-element
                }}>
                    <Typography variant="h6" sx={{ fontSize: '1.5em' }}>
                        Account Number: {accountNumber}
                    </Typography>
                    <Typography variant="body1" sx={{ fontSize: '1.5em', margin: '10px 0' }}>
                        Total Balance: ${formatAmount(balance)}
                    </Typography>
                    <Typography variant="body1" sx={{ fontSize: '1.5em' }}>
                        Total Accounts: {totalAccounts}
                    </Typography>
                    <Button variant="outlined" onClick={handleLogout} sx={{ marginTop: '10px' }}>
                        Logout
                    </Button>
                </Box>
            </Box>
            <Box className="home-tabs">
                <BasicTabs role={role}/>
            </Box>
        </Container>
    );
}