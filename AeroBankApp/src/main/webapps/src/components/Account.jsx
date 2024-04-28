import React, {useState} from 'react';
import {Box, Typography, Grid, Paper, Avatar, CardContent, Card, IconButton, Badge} from '@mui/material';
import CircleIcon from '@mui/icons-material/Circle';
import {blue} from "@mui/material/colors";
import NotificationsIcon from '@mui/icons-material/Notifications';
import axios from "axios";
import NotificationBell from "./NotificationBell";

const NotificationCategory = {
    TRANSACTION_ALERT: "TransactionAlert",
    BALANCE_UPDATE: "BalanceUpdate",
    ACCOUNT_SECURITY: "AccountSecurity",
    PAYMENT_REMINDER: "PaymentReminder",
    SCHEDULED_MAINTENANCE: "ScheduledMaintenance",
    ACCOUNT_UPDATE: "AccountUpdate"
};

const testNotifications = [
    {
        id: 1,
        title: "Payment Received",
        message: "You have received a payment of $150 from John Doe.",
        category: NotificationCategory.TRANSACTION_ALERT
    },
    {
        id: 2,
        title: "Account Alert",
        message: "Your account balance is lower than $100.",
        category: NotificationCategory.BALANCE_UPDATE
    },
    {
        id: 3,
        title: "Scheduled Maintenance",
        message: "Our banking services will be unavailable this Sunday from 2 AM to 5 AM due to scheduled maintenance.",
        category: NotificationCategory.SCHEDULED_MAINTENANCE
    },
    {
        id: 4,
        title: "New Offer",
        message: "A new savings account with an attractive interest rate is available now. Check it out!",
        category: NotificationCategory.ACCOUNT_UPDATE
    }
];

export default function Account({ color, accountCode, acctID, acctCodeID, accountName, balance, pending, available, notifications, onNotificationClick, notificationCount, onAccountClick, isSelected, backgroundImageUrl})
{
    const [newNotificationCount, setNewNotificationCount] = useState(notifications.length);

    const handleUpdateNotificationCount = (newCount) => {
        setNewNotificationCount(newCount);
    }

    const handleClick = () => {
        onAccountClick(acctID);
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            handleClick();
        }
    };

    const handleOnNotificationClick = (event) => {
        // Send a request to fetch notifications for the account with accountID
      //  event.stopPropagation();
      //  console.log('Notification Clicked');
      //  onNotificationClick(accountCode);
    };

    const defaultColor = '#9e9e9e'; // Default color if none is provided
    const avatarColor = color || defaultColor; // Use provided color or default

    // Adding a semi-transparent overlay if the account is selected
    const backgroundImageWithOverlay = isSelected ?
        `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${backgroundImageUrl})` :
        `url(${backgroundImageUrl})`;


    const avatarStyle = {
        bgcolor: avatarColor,
        width: 56,
        height: 56,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: '16px',
        // If the selected color is a gradient, we'll use the text color that ensures readability
        color: 'contrastText', // Adjust text color for readability if necessary
    };

    return (
        <Card
            tabIndex="0"
            role="button"
            onClick={handleClick}
            onKeyPress={handleKeyPress}
            sx={{
                cursor: 'pointer',
                m: 1,
                transition: 'background-color 0.3s',
                backgroundImage: backgroundImageWithOverlay,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                '&:focus-visible': {
                    boxShadow: '0 0 0 2px #ff8e53',
                },
            }}
        >
            <CardContent sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                {/* Inner Card for Account Details */}
                <Card sx={{ bgcolor: 'white', p: 2, mr: 2, minWidth: 200, position: 'right' }}>
                    {notificationCount > 0 && (
                        <Box sx={{
                            position: 'relative',
                            top: 0,
                            right: 0,
                            transform: 'translate(40%, -20%)' // Adjusts the bell icon to be partly outside the card
                        }}>
                            <NotificationBell
                                notificationCount={newNotificationCount}
                                initialNotifications={notifications}
                                onBellClick={handleUpdateNotificationCount}
                            />
                        </Box>
                    )}
                    <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2 }}>
                        <Avatar sx={avatarStyle}>
                            <Typography variant="subtitle1">
                                {accountCode}
                            </Typography>
                        </Avatar>
                    </Box>
                    <Typography variant="h6" component="div" sx={{ mb: 2, textAlign: 'center' }}>
                        {accountName}
                    </Typography>
                    <Typography variant="body1" sx={{ mb: 1 }}>Balance: <strong>${balance}</strong></Typography>
                    <Typography variant="body1" sx={{ mb: 1 }}>Pending: <strong>${pending}</strong></Typography>
                    <Typography variant="body1">Available: <strong>${available}</strong></Typography>
                </Card>
                {/* Existing Content */}


            </CardContent>
        </Card>
    );
}