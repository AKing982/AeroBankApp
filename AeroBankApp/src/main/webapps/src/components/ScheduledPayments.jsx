import {
    Button, Card,
    CardActions, CardContent,
    Collapse,
    Grid,
    IconButton,
    List,
    ListItem,
    ListItemText,
    Paper,
    Typography
} from "@mui/material";
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

    const cardStyle = {
        marginBottom: '16px',
        backgroundColor: '#f5f5f5',
    };

    return (
        <Grid item xs={12} md={6}>
            <Paper style={paperStyle}>
                <Typography variant="h6" sx={headingStyle}>Scheduled Payments</Typography>
                {isLoading ? (
                    <Skeleton variant="rectangular" height={200} />
                ) : (
                    <Grid container spacing={2}>
                        {payments.map((payment, index) => (
                            <Grid item xs={12} key={index}>
                                <Card sx={cardStyle}>
                                    <CardContent>
                                        <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>{payment.payee}</Typography>
                                        <Typography variant="body2">Amount: <span style={{ color: 'green' }}>{payment.amount}</span></Typography>
                                        <Typography variant="body2" sx={{ fontWeight: 'bold', color: '#d32f2f' }}>
                                            Due: {payment.dueDate}
                                        </Typography>
                                        <Collapse in={open[index]} timeout="auto" unmountOnExit>
                                            <Typography variant="body2" sx={{ mt: 2, fontWeight: 'bold', color: '#1976d2' }}>
                                                Confirmation: {payment.confirmationNumber}
                                            </Typography>
                                            <Typography variant="body2" sx={{ fontWeight: 'bold', color: '#388e3c' }}>
                                                Status: {payment.status}
                                            </Typography>
                                        </Collapse>
                                    </CardContent>
                                    <CardActions>
                                        <Button size="small" onClick={() => handleClick(index)}>
                                            {open[index] ? 'Hide Details' : 'Show Details'}
                                            <ExpandMoreIcon sx={{ transform: open[index] ? 'rotate(180deg)' : 'rotate(0deg)' }} />
                                        </Button>
                                        <IconButton edge="end" aria-label="edit">
                                            <EditIcon sx={{ color: '#1976d2' }} />
                                        </IconButton>
                                        <IconButton edge="end" aria-label="delete">
                                            <DeleteIcon sx={{ color: '#d32f2f' }} />
                                        </IconButton>
                                    </CardActions>
                                </Card>
                            </Grid>
                        ))}
                    </Grid>
                )}
            </Paper>
        </Grid>
        // <Grid item xs={12} md={6}>
        //     <Paper style={paperStyle}>
        //         <Typography variant="h6" sx={headingStyle}>Scheduled Payments</Typography>
        //         {isLoading ? (
        //             <Skeleton variant="rectangular" height={200} />
        //         ) : (
        //             <List>
        //                 {payments.map((payment, index) => (
        //                     <React.Fragment key={index}>
        //                         <ListItem button onClick={() => handleClick(index)}>
        //                             <ListItemText
        //                                 primary={`${payment.payee}: ${payment.amount}`}
        //                                 secondary={`Due: ${payment.dueDate}`}
        //                             />
        //                             <IconButton edge="end" aria-label="edit">
        //                                 <EditIcon />
        //                             </IconButton>
        //                             <IconButton edge="end" aria-label="delete">
        //                                 <DeleteIcon />
        //                             </IconButton>
        //                             {open[index] ? <ExpandMoreIcon /> : <ExpandMoreIcon style={{ transform: 'rotate(180deg)' }} />}
        //                         </ListItem>
        //                         <Collapse in={open[index]} timeout="auto" unmountOnExit>
        //                             <List component="div" disablePadding>
        //                                 <ListItem>
        //                                     <ListItemText secondary={<Typography variant="body2" sx={{ color: '#616161' }}>{`Confirmation: ${payment.confirmationNumber}`}</Typography>} />
        //                                     {/*<ListItemText secondary={`Confirmation: ${payment.confirmationNumber}`} />*/}
        //                                 </ListItem>
        //                                 <ListItem>
        //                                     <ListItemText secondary={`Status: ${payment.status}`} />
        //                                 </ListItem>
        //                             </List>
        //                         </Collapse>
        //                     </React.Fragment>
        //                 ))}
        //             </List>
        //         )}
        //     </Paper>
        // </Grid>
    );
}

export default ScheduledPayments;