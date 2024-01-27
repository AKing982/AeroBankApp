import {useEffect, useState} from "react";
import AccountBox from "./AccountBox";

export default function AccountListView({items})
{
    const [accountData, setAccountData] = useState([]);

    const username = sessionStorage.getItem('username');

    useEffect(() => {
        const fetchAccounts = async () => {
            try{
                const response = await fetch(`http://localhost:8080/api/accounts/${username}`);
                if(!response.ok)
                {
                    throw new Error('Network response was not ok.');
                }
                const data = await response.json();
                setAccountData(data);
            }catch(error)
            {
                console.error('Error fetching account data: ', error);
            }
        };

        fetchAccounts();

    }, []);

    return (
        <div>
            <h2>{username}'s Accounts</h2>
            <div className="account-list-body">
                {accountData.map((account) => {
                    <AccountBox
                        key={account.id}
                        accountCode={account.accountCode}
                        balance={account.balance}
                        pending={account.pending}
                        available={account.available}
                        />
                })}
            </div>
        </div>
    );
}