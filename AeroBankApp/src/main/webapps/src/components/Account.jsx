import React, {useState} from 'react';
import { Box, Typography, Grid, Paper } from '@mui/material';
import CircleIcon from '@mui/icons-material/Circle';
import {blue} from "@mui/material/colors";

export default function Account({ color, accountCode, balance, pending, available })
{
    const [clicked, setClicked] = useState(false);
    const handleClick = () => {
        setClicked(!clicked);
        console.log('Clicked');
    };

    const handleKeyPress = (event) => {
        if(event.key === 'Enter' || event.key === ' ') {
            handleClick();
        }
    };

    const greyColor = '#9e9e9e';
    const gradient = 'linear-gradient(45deg, #fe6b8b 30%, #ff8e53 90%)';


    return (
        <Paper
            elevation={3}
            tabIndex="0" role="button" onClick={handleClick} onKeyPress={handleKeyPress}

               sx={{
                   p: 2,
                   display: 'flex',
                   alignItems: 'center',
                   cursor: 'pointer',
                   backgroundColor: clicked ? gradient : greyColor,
                   mb: 2,
                   transition: 'background-color 0.3s'
               }}>
            <CircleIcon sx={{ color: color || 'green', mr: 2 }} fontSize="large"/>
            <Typography variant="h6" sx={{ flexGrow: 1, ml: 1}}>{accountCode}</Typography>
            <Box>
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