import React, {useState} from 'react';
import {Box, Typography, Grid, Paper, Avatar} from '@mui/material';
import CircleIcon from '@mui/icons-material/Circle';
import {blue} from "@mui/material/colors";

export default function Account({ color, accountCode, balance, pending, available, onAccountClick, isSelected})
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
    const selectedColor = color || defaultColor; // Use provided color or default
    const gradient = isSelected ? 'linear-gradient(45deg, #fe6b8b 30%, #ff8e53 90%)' : selectedColor;

    const avatarStyle = {
        bgcolor: gradient,
        width: 56,
        height: 56,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: '16px',
        // If the selected color is a gradient, we'll use the text color that ensures readability
        color: isSelected ? '#fff' : 'contrastText', // Adjust text color for readability if necessary
    };

    return (
        <Paper
            elevation={3}
            tabIndex="0"
            role="button"
            onClick={handleClick}
            onKeyPress={handleKeyPress}
            sx={{
                p: 2,
                display: 'flex',
                alignItems: 'center',
                cursor: 'pointer',
                mb: 2,
                transition: 'background-color 0.3s'
            }}
        >
            <Avatar sx={avatarStyle}>
                <Typography variant="subtitle1">
                    {accountCode}
                </Typography>
            </Avatar>
            <Box sx={{ flexGrow: 1, ml: 2 }}>
                <Grid container>
                    <Grid item xs={12}>
                        <Typography variant="body1">Balance: <strong>${balance}</strong></Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant="body1">Pending: <strong>${pending}</strong></Typography>
                    </Grid>
                    <Grid item xs={12}>
                        <Typography variant="body1">Available: <strong>${available}</strong></Typography>
                    </Grid>
                </Grid>
            </Box>
        </Paper>
    );
}