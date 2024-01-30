import {useEffect, useState} from "react";
import AccountBox from "./AccountBox";
import axios from "axios";
import Account from "./Account";

export default function AccountListView({setAccountCode})
{
    const [accountData, setAccountData] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(null);

    const username = sessionStorage.getItem('username');

    const storeAccountCode = (accountCode) => {
        sessionStorage.setItem('AccountCode', accountCode);
    }

    useEffect(() => {
        const fetchAccounts = async () => {
            try{
                const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/${username}`);

                console.log('Fetching Accounts: ', response.data);
                setAccountData(response.data);
            }catch(error)
            {
                console.error('Error fetching account data: ', error);
            }
        };

        fetchAccounts();

    }, [username]);


    const handleAccountButtonClick = (accountCode) => {
       setSelectedAccount(accountCode);
       storeAccountCode(accountCode);
       console.log("Selected AccountCode: ", accountCode);
    };

    return (
        <div>
            <h2>{username}'s Accounts</h2>
            <div className="account-list-body">
                {accountData.map((account) => {
                    return(
                        <Account
                            key={account.id}
                            accountCode={account.accountCode}
                            balance={account.balance}
                            pending={account.pendingAmount}
                            available={account.availableAmount}
                            onAccountClick={handleAccountButtonClick}
                            isSelected = {selectedAccount === account.accountCode}
                        />
                        );
                })}
            </div>
        </div>
    );
}