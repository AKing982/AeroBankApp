import {useState} from "react";
import {Box} from "@mui/system";
import {Card, CardContent, Typography} from "@mui/material";

export default function AccountBox({color, accountCode, accountName, balance, pending, available})
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
        <Card
            component="div"
            role="button"
            onClick={handleClick}
            onKeyPress={handleKeyPress}
            tabIndex="0"
            sx={{ display: 'flex', alignItems: 'center', padding: 2, cursor: 'pointer' }}
        >
            <Box sx={circleStyle}>{accountCode}</Box>
            <CardContent>
                <Typography variant="h6" component="div">{accountName}</Typography>
                <Typography variant="body1" component="div" sx={{ mb: 1.5 }}>
                    Balance: ${balance}
                </Typography>
                <Typography variant="body1" component="div" sx={{ mb: 1.5 }}>
                    Pending: ${pending}
                </Typography>
                <Typography variant="body1" component="div">
                    Available: ${available}
                </Typography>
            </CardContent>
        </Card>
    );
}

