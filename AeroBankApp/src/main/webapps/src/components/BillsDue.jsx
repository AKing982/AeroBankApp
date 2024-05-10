import {Skeleton} from "@mui/lab";
import {
    Collapse,
    Grid,
    IconButton,
    List,
    ListItem,
    ListItemSecondaryAction,
    ListItemText,
    Paper,
    Typography
} from "@mui/material";
import PaymentIcon from "@mui/icons-material/Payment";
import InfoIcon from "@mui/icons-material/Info";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {useState} from "react";
import React from 'react';

function BillsDue({bills}){
    const [open, setOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const handleClick = (id) => {
        setOpen(prev => ({...prev, [id]: !prev[id]}));
    };

    const paperStyle = {
        padding: '16px',
        backgroundColor: 'rgba(255, 255, 255, 0.9)', // Semi-transparent white
        boxShadow: '0px 4px 8px rgba(0,0,0,0.1)',
        borderRadius: '8px'
    };

    const headingStyle = {
        margin: '0 0 16px 0',
        color: '#205375', // Dark blue color for headings
    };


    return (
        <Grid item xs={12} md={6}>
            <Paper style={paperStyle}>
                <Typography variant="h6" sx={headingStyle}>Bills Due</Typography>
                {isLoading ? (
                    <Skeleton variant="rectangular" height={200} />
                ) : (
                    <List>
                        {bills.map((bill, index) => (
                            <React.Fragment key={index}>
                                <ListItem button onClick={() => handleClick(index)}>
                                    <ListItemText
                                        primary={`${bill.billDate} - ${bill.billType}: ${bill.amountDue}`}
                                        secondary={bill.status === 'overdue' ? 'Overdue' : 'Due soon'}
                                    />
                                    <ListItemSecondaryAction>
                                        <IconButton edge="end" aria-label="pay" onClick={() => console.log('Initiate payment')}>
                                            <PaymentIcon />
                                        </IconButton>
                                        <IconButton edge="end" aria-label="details">
                                            <InfoIcon />
                                        </IconButton>
                                    </ListItemSecondaryAction>
                                    {open[index] ? <ExpandMoreIcon /> : <ExpandMoreIcon style={{ transform: 'rotate(180deg)' }} />}
                                </ListItem>
                                <Collapse in={open[index]} timeout="auto" unmountOnExit>
                                    <List component="div" disablePadding>
                                        <ListItem sx={{ pl: 4 }}>
                                            <ListItemText secondary={`Payment method: ${bill.paymentMethod}`} />
                                        </ListItem>
                                    </List>
                                </Collapse>
                            </React.Fragment>
                        ))}
                    </List>
                )}
            </Paper>
        </Grid>
    );
}

export default BillsDue;