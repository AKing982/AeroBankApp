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

    const circleStyle = {
        width: '50px',
        height: '50px',
        borderRadius: '50%',
        backgroundColor: color || '#4CAF50', // Use the color prop if provided, or the default color
        color: 'white',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: '15px',
    };

    return (
        <div className="account-box" role="button" onClick={handleClick} onKeyPress={handleKeyPress} tabIndex="0">
            <div style={circleStyle}>{accountCode}</div>
            <div className="account-details-container">
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

