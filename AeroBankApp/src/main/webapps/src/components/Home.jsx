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

    const [data, setData] = useState({balance: 0, accountNumber: '', totalAccounts:0 })

    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState('Transactions');
    const [role, setRole] = useState(null);
    const navigate = useNavigate();

    const route = '/';



    const username = sessionStorage.getItem('username');

    useEffect(() => {
            setIsLoading(true);
            axios.get(`http://localhost:8080/AeroBankApp/api/profile/${username}`)
                .then(response => {
                    setAccountNumber(response.data.accountNumber);
                    setTotalAccounts(response.data.totalAccounts);
                    setBalance(response.data.totalBalance);
                    setRole(response.data.role);
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




    const handleTabClick = (tab) => {
        setActiveTab(tab);
    }

    const handleLogout = () => {

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