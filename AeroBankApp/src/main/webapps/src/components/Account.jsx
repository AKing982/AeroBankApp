import React, {useState} from 'react';
import {Box, Typography, Grid, Paper, Avatar, CardContent, Card, IconButton, Badge} from '@mui/material';
import CircleIcon from '@mui/icons-material/Circle';
import {blue} from "@mui/material/colors";
import NotificationsIcon from '@mui/icons-material/Notifications';


export default function Account({ color, accountCode, accountName, balance, pending, available, notificationCount, onAccountClick, isSelected, backgroundImageUrl})
{
    const handleClick = () => {
        onAccountClick(accountCode);
    };

    const handleKeyPress = (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            handleClick();
        }
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
                <Card sx={{ bgcolor: 'white', p: 2, mr: 2, minWidth: 200, position: 'relative' }}>
                    {notificationCount > 0 && (
                        <IconButton
                            color="inherit"
                            sx={{position: 'absolute', top: 8, right: 8}} >
                            <Badge badgeContent={notificationCount} color="secondary">
                                <NotificationsIcon />
                            </Badge>
                        </IconButton>
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