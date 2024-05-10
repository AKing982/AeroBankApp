import {Collapse, Grid, IconButton, List, ListItem, ListItemText, Paper, Typography} from "@mui/material";
import {Skeleton} from "@mui/lab";
import EditIcon from "@mui/icons-material/Edit";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import DeleteIcon from "@mui/icons-material/Delete";
import {useState} from "react";
import React from 'react';

function ScheduledPayments({payments}){
    const [open, setOpen] = useState({});
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
                <Typography variant="h6" sx={headingStyle}>Scheduled Payments</Typography>
                {isLoading ? (
                    <Skeleton variant="rectangular" height={200} />
                ) : (
                    <List>
                        {payments.map((payment, index) => (
                            <React.Fragment key={index}>
                                <ListItem button onClick={() => handleClick(index)}>
                                    <ListItemText
                                        primary={`${payment.payee}: ${payment.amount}`}
                                        secondary={`Due: ${payment.dueDate}`}
                                    />
                                    <IconButton edge="end" aria-label="edit">
                                        <EditIcon />
                                    </IconButton>
                                    <IconButton edge="end" aria-label="delete">
                                        <DeleteIcon />
                                    </IconButton>
                                    {open[index] ? <ExpandMoreIcon /> : <ExpandMoreIcon style={{ transform: 'rotate(180deg)' }} />}
                                </ListItem>
                                <Collapse in={open[index]} timeout="auto" unmountOnExit>
                                    <List component="div" disablePadding>
                                        <ListItem>
                                            <ListItemText secondary={`Confirmation: ${payment.confirmationNumber}`} />
                                        </ListItem>
                                        <ListItem>
                                            <ListItemText secondary={`Status: ${payment.status}`} />
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

export default ScheduledPayments;