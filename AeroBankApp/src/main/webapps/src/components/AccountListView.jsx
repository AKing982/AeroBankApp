import {useEffect, useState} from "react";
import AccountBox from "./AccountBox";
import axios from "axios";
import Account from "./Account";
import {CircularProgress} from "@mui/material";


export default function AccountListView({setAccountCode, updateAccountID})
{
    const [accountData, setAccountData] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    const username = sessionStorage.getItem('username');

    const storeAccountCode = (accountCode) => {
        sessionStorage.setItem('AccountCode', accountCode);
    }

    useEffect(() => {
        if(selectedAccount){
            fetchAccountID();
        }
    }, [selectedAccount])

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
                try{
                    const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/${username}`);

                    console.log('Fetching Accounts: ', response.data);
                    setAccountData(response.data);
                    setIsLoading(false);
                }catch(error)
                {
                    console.error('Error fetching account data: ', error);
                    setIsLoading(false);
                }
            };

            fetchAccounts();
        }, 2000)
    }, [username]);


    const handleAccountButtonClick = (accountCode) => {
       setSelectedAccount(accountCode);
       storeAccountCode(accountCode);
       console.log("Selected AccountCode: ", accountCode);
    };

    return (
        <div>
            <h2>{username}'s Shares</h2>
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
                                available={account.availableAmount}
                                onAccountClick={handleAccountButtonClick}
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