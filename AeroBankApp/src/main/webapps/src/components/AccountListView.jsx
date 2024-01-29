import {useEffect, useState} from "react";
import AccountBox from "./AccountBox";
import axios from "axios";

export default function AccountListView()
{
    const [accountData, setAccountData] = useState([]);

    const username = sessionStorage.getItem('username');

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

    return (
        <div>
            <h2>{username}'s Accounts</h2>
            <div className="account-list-body">
                {accountData.map((account) => {
                    return(
                        <AccountBox
                            key={account.id}
                            accountCode={account.accountCode}
                            balance={account.balance}
                            pending={account.pendingAmount}
                            available={account.availableAmount}
                        />
                        );
                })}
            </div>
        </div>
    );
}