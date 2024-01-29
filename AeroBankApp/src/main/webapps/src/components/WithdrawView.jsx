import {useEffect, useState} from "react";
import AccountSelect from "./AccountSelect";
import axios from "axios";

export default function WithdrawView()
{
    const [accountCode, setAccountCode] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const user = sessionStorage.getItem('username');

    useEffect(() => {
        setIsLoading(true);
        axios.get(`http://localhost:8080/api/accounts/data/codes${user}`)
            .then(response => {
                setAccountCode(response.data)
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error);
            })
            .then(() => {
                setIsLoading(true);
            })

    }, []);

    return (
        <div className="withdraw-view-container">
            <header clasName="withdraw-view-header">
            </header>
            <div className="withdraw-view-center">
                <div className="withdraw-group">
                    <div className="withdraw-account-selection">
                        <label htmlFor="account-withdraw" className="withdraw-account-selection-label">Account to Withdraw:</label>
                        <AccountSelect accounts={accountCode} />
                    </div>
                </div>
            </div>
        </div>
    );
}