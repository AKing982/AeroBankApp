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
        const loginTime = sessionStorage.getItem('loginTime');
        const loginISO = sessionStorage.getItem('loginISOTime');
        const duration = logoutTime - loginTime;
        console.log('Duration Logged In: ', duration);
        const duration_in_seconds = Math.floor(duration/1000);
        setDuration(duration_in_seconds);
        const userID = sessionStorage.getItem('userID');

        setUserIsActive(0);

        updateUserLogRequest(userID, duration_in_seconds, 0, loginISO, new Date().toISOString());
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
        <div className="home-container">
            <header className="home-header">
                <div className="welcome-section">
                    <div className="welcome-message"><TimeGreeting username={username}/></div>
                    <div className="date-info">Date: {new Date().toLocaleDateString()}</div>
                    <div className="current-time">Time: <CurrentTime /></div>
                </div>
                <div className="account-info-section">
                    <div className="account-number">AccountNumber: {accountNumber}</div>
                    <div className="account-details">
                        <div className="current-balance">Total Balance: ${formatAmount(balance)}</div>
                        <div className="total-accounts">Total Accounts: {totalAccounts}</div>
                    </div>
                    <div className="logout-button-header">
                        <BasicButton text="Logout" submit={handleLogout}/>
                    </div>

                </div>
            </header>
            <div className="home-tabs">
            </div>
            <div className="tab-content">
                <BasicTabs role={role}/>
            </div>
        </div>
    )
}