import '../Home.css';
import HomeTab from "./HomeTab";
import {useEffect, useState} from "react";
import TransactionView from "./TransactionView";
import DepositView from "./DepositView";
import WithdrawView from "./WithdrawView";
import TransferView from "./TransferView";
import SettingsView from "./SettingsView";

export default function Home()
{
    const [activeTab, setIsActiveTab] = useState('Transactions');
    const [balance, setBalance] = useState(0);
    const [accountNumber, setAccountNumber] = useState('');
    const [totalAccounts, setTotalAccounts] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    const username = sessionStorage.getItem('username');

    useEffect(() => {
            setIsLoading(true);
            fetch(`http://localhost:8080/api/profile/data/${username}`)
                .then(response => {
                    if(!response.ok)
                    {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    setBalance(data.balance);
                    setAccountNumber(data.accountNumber);
                    setTotalAccounts(data.totalAccounts);
                    setIsLoading(false);
                })
                .catch(error => {
                    console.error('There has been a problem with your fetch operation: ', error);
                    setError(error);
                    setIsLoading(false);
                });
        }, []);

    const handleTabClick = (tab) => {
        setIsActiveTab(tab);
    }

    return (
        <div className="home-container">
            <header className="home-header">
                <div className="welcome-message">Welcome, {username}</div>
                <div className="date-info">Date: {new Date().toLocaleDateString()}</div>
                <div className="current-balance">Total Balance: {balance}</div>
                <div className="account-number">Account Number: {accountNumber}</div>
                <div className="total-accounts">Total Accounts: {totalAccounts}</div>
            </header>
            <div className="home-tabs">
                <HomeTab label="Transactions" isActive={activeTab === 'Transactions'} onTabClick={() => setIsActiveTab('Transactions')} />
                <HomeTab label="Make a Deposit" isActive={activeTab === 'Make a Deposit'} onTabClick={() => setIsActiveTab('Make a Deposit')} />
                <HomeTab label="Make a Withdrawal" isActive={activeTab === 'Make a Withdraw'} onTabClick={() => setIsActiveTab('Make a Withdraw')}/>
                <HomeTab label="Make a Transfer" isActive={activeTab === 'Make a Transfer'} onTabClick={() => setIsActiveTab('Make a Transfer')}/>
                <HomeTab label="Settings" isActive={activeTab === 'Settings'} onTabClick={() => setIsActiveTab('Settings')}
                />
            </div>
            <div className="tab-content">
                {activeTab === 'Transactions' && <div><TransactionView /></div>}
                {activeTab === 'Make a Deposit' && <div><DepositView /></div>}
                {activeTab === 'Make a Withdraw' && <div><WithdrawView /></div>}
                {activeTab === 'Make a Transfer' && <div><TransferView /></div>}
                {activeTab === 'Settings' && <div><SettingsView/></div>}
            </div>
        </div>
    )
}