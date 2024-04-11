import {useEffect, useState} from "react";
import axios from "axios";
import Account from "./Account";
import {CircularProgress} from "@mui/material";


export default function AccountListView({updateAccountID})
{
    const [accountData, setAccountData] = useState([]);
    const [accountProperties, setAccountProperties] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState('');
    const [isLoading, setIsLoading] = useState(true);
    const [fullName, setFullName] = useState('');

    const username = sessionStorage.getItem('username');

    const storeAccountCode = (accountCode) => {
        sessionStorage.setItem('AccountCode', accountCode);
    }

    useEffect(() => {
        console.log('Selected Account: ', selectedAccount);
        if(selectedAccount){
            fetchAccountID();
        }
        if(selectedAccount === ''){
            console.log('Going inside fetchAccountIDByUserID');
            fetchAccountIDByUserID();
        }
    }, [selectedAccount])

    const fetchAccountIDByUserID = () => {
        const userID = sessionStorage.getItem('userID');
        console.log('Generating Random AccountID');
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/rand/${userID}`)
            .then(response => {
                console.log('Random AccountID Response: ', response.data);
                const randomAcctID = response.data;
                updateAccountID(randomAcctID);
            })
            .catch(error => {
                console.error('There was an error fetching a random acctID: ', error);
            });
    }

    const fetchAccountID = () => {
        const accountCode = selectedAccount;

        const currentUserID = sessionStorage.getItem('userID');
        console.log('Selected Account Code from Fetch: ', accountCode);
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/${currentUserID}/${accountCode}`)
            .then(response => {
                    const accountID = response.data.accountID;
                    console.log('AccountID Response: ', accountID);
                    updateAccountID(accountID)
                    sessionStorage.setItem('AccountID', accountID);
                    console.log('Fetching Account ID: ', accountID);
            })
            .catch(error => {
                console.error('There was an error fetching the accountID: ', error);
            });
    }

    useEffect(() => {
        const timeout = setTimeout(() => {
            const fetchAccounts = async () => {
                axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/${username}`)
                    .then(response => {
                        console.log('Fetching Accounts: ', response.data);
                        setAccountData(response.data);
                        setIsLoading(false);
                    })
                    .catch(error => {
                        console.error('Error fetching account data: ', error);
                        setIsLoading(false);
                    });
            }
            fetchAccounts();
        }, 2000)
    }, [username]);

    useEffect(() => {
        const fetchUsersFullName = async () => {
            let userID = sessionStorage.getItem('userID');
            console.log('UserID fetched: ', userID);
            axios.get(`http://localhost:8080/AeroBankApp/api/users/name/${userID}`)
                .then(response => {
                    const first = response.data.firstName;
                    const last = response.data.lastName;
                    const fullName = first + " " + last;
                    setFullName(fullName);
                })
                .catch(error => {
                    console.error('There was an error fetching the full name: ', error);
                });
        }
        fetchUsersFullName();
    }, [])


    const handleAccountButtonClick = (accountCode) => {
       setSelectedAccount(accountCode);
       storeAccountCode(accountCode);
       console.log("Selected AccountCode: ", accountCode);
    };

    return (
        <div>
            <h2>{fullName}'s Shares</h2>
            {isLoading ? (
                <CircularProgress />
            ) : (
                <div className="account-list-body">
                    {accountData.map((account) => {
                        return (
                            <Account
                                key={account.id}
                                accountCode={account.accountCode}
                                balance={account.balance}
                                pending={account.pendingAmount}
                                accountName={account.accountName}
                                available={account.availableAmount}
                                backgroundImageUrl={account.acctImage}
                                onAccountClick={handleAccountButtonClick}
                                notificationCount={1}
                                color={account.acctColor}
                                isSelected={selectedAccount === account.accountCode}
                            />
                        );
                    })}
                </div>
            )}
        </div>
    );
}