import {useState} from "react";

export default function AccountBox({accountCode, balance, pending, available})
{
    return (
        <div className="account-box">
            <div className="account-code-circle">{accountCode}</div>
            <div className="account-details">
                <div className="account-item">
                    <span className="account-label">Balance:</span>
                    <span className="account-value">{balance}</span>
                </div>
                <div className="account-item">
                    <span className="account-label">Pending:</span>
                    <span className="account-value">{pending}</span>
                </div>
                <div className="account-item">
                    <span className="account-label">Available:</span>
                    <span className="account-value">{available}</span>
                </div>
            </div>
        </div>
    );
}

