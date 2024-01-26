import {useState} from "react";

export default function AccountBox({color, accountCode, balance, pending, available})
{
    const handleClick = () => {
        console.log('Clicked');
    }

    const handleKeyPress = (event) => {
        if(event.key === 'Enter' || event.key === ' ')
        {
            handleClick();
        }
    }

    return (
        <div className="account-box" role="button" onClick={handleClick} onKeyPress={handleKeyPress} tabIndex="0">
            <div className="account-code-circle">{accountCode}</div>
            <div className="account-details">
                <div className="account-item">
                    <span className="account-label">Balance:</span>
                    <span className="account-value">${balance}</span>
                </div>
                <div className="account-item">
                    <span className="account-label">Pending:</span>
                    <span className="account-value">${pending}</span>
                </div>
                <div className="account-item">
                    <span className="account-label">Available:</span>
                    <span className="account-value">${available}</span>
                </div>
            </div>
        </div>
    );
}

