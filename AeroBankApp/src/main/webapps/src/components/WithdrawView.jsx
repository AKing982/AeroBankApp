import {useEffect, useState} from "react";
import AccountSelect from "./AccountSelect";
import axios from "axios";
import AccountListView from "./AccountListView";
import Account from "./Account";
import WithdrawDescription from "./WithdrawDescription";
import '../WithdrawView.css';

export default function WithdrawView()
{
    const [accountCode, setAccountCode] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [description, setDescription] = useState(null);

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

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    return (
        <div className="withdraw-view-container">
            <header className="withdraw-view-header">
            </header>
            <div className="withdraw-account-list">
                <AccountListView items={<Account accountCode={"A1"} available={4500} balance={5600} pending={15} color="red" />} />
            </div>

            <div className="withdraw-view-center">
                <div className="withdraw-group">
                    <div className="withdraw-account-selection">
                        <label htmlFor="account-withdraw" className="withdraw-account-selection-label">Account to Withdraw:</label>
                        <AccountSelect accounts={accountCode} />
                    </div>
                    <div className="withdraw-description">
                        <WithdrawDescription value={description} onChange={handleDescriptionChange}/>
                    </div>
                </div>
            </div>
        </div>
    );
}