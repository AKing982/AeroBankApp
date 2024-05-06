import React, {useState} from 'react';
import {Box, Typography, Grid, Paper, Avatar, CardContent, Card, IconButton, Badge} from '@mui/material';
import CircleIcon from '@mui/icons-material/Circle';
import {blue} from "@mui/material/colors";
import NotificationsIcon from '@mui/icons-material/Notifications';
import axios from "axios";
import NotificationBell from "./NotificationBell";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import VisibilityIcon from '@mui/icons-material/Visibility';

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
    const [expanded, setExpanded] = useState(false);  // State to manage expansion of account details

    const handleUpdateNotificationCount = (newCount) => {
        setNewNotificationCount(newCount);
    };

    const handleClick = () => {
        onAccountClick(acctID);
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            handleClick();
        }
    };

    const toggleDetails = (event) => {
        event.stopPropagation(); // Prevent the card's onClick event from firing
        setExpanded(!expanded);
    };

    const defaultColor = '#9e9e9e'; // Default color if none is provided
    const avatarColor = color || defaultColor; // Use provided color or default
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
        color: 'contrastText', // Adjust text color for readability
    };

    const iconButtonStyle = {
        position: 'absolute',
        top: 0,
        right: 0,
        color: 'blue',  // Make the icon more visible
        backgroundColor: 'rgba(0, 0, 0, 0.5)',  // Background to enhance visibility
        margin: '8px',  // Ensuring it's not flush against the edges
        '&:hover': {
            backgroundColor: 'rgba(0, 0, 0, 0.8)',  // Darker on hover for better UI interaction
        }
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
                <Card sx={{ bgcolor: 'white', p: 2, mr: 2, minWidth: 200, position: 'relative' }}>
                    {notificationCount > 0 && (
                        <NotificationBell
                            notificationCount={newNotificationCount}
                            initialNotifications={notifications}
                            onBellClick={handleUpdateNotificationCount}
                        />
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
                    {expanded && (
                        <>
                            <Typography variant="body1" sx={{ mb: 1 }}>Pending: <strong>${pending}</strong></Typography>
                            <Typography variant="body1">Available: <strong>${available}</strong></Typography>
                            {/* Additional details can be added here */}
                        </>
                    )}
                    <IconButton
                        onClick={toggleDetails}
                        aria-expanded={expanded}
                        aria-label="show more"// Positioning the toggle button
                        sx={{
                            position: 'absolute',
                            right: 180,
                            top: '80%',
                            transform: 'translateY(-50%)',  // Center vertically
                            color: 'primary.main',
                            backgroundColor: 'background.paper',
                            '&:hover': {
                                backgroundColor: 'background.default',
                            }
                        }}

                    >
                        <ExpandMoreIcon />
                    </IconButton>
                </Card>

            </CardContent>
        </Card>
    );
}