import UserTextField from "./UserTextField";
import {useState} from "react";

export default function AccountSettings()
{
    const [accountName, setAccountName] = useState(null);
    const [accountType, setAccountType] = useState(null);
    const [isAccountEnabled, setIsAccountEnabled] = useState(false);
    const [withdrawalLimit, setWithdrawalLimit] = useState(0);
    const [transferLimit, setTransferLimit] = useState(0);
    const [overdraftEnabled, setOverdraftEnabled] = useState(false);
    const [interestRate, setInterestRate] = useState(0);

    const handleAccountNameChange = (event) => {
        setAccountName(event.target.value);
    }

    return (
        <div className="account-settings-container">
            <header className="account-settings-header">
            </header>
            <div className="account-properties">
                <div className="account-name">
                    <label htmlFor="account-properties-name" className="account-name-label">Account Name: </label>
                    <UserTextField label="Account Name" onChange={handleAccountNameChange} value={accountName}/>
                </div>
                <div className="account-type">
                </div>
                <div className="account-access">
                </div>
                <div className="account-withdrawal-limit">
                </div>
                <div className="account-transfer-limit">
                </div>
                <div className="account-users-access">
                </div>
                <div className="account-overdraft-access">
                </div>
                <div className="account-interest-rate">
                </div>
                <div className="account-warnings"></div>
            </div>
            <div className="users-account-list">
            </div>
            <div className="link-users-account-transfer">

            </div>
        </div>
    )
}