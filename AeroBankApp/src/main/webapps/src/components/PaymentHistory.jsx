import {Card, CardContent, Divider, List, ListItem, ListItemText, Typography} from "@mui/material";
import React from 'react';

function PaymentHistory({paymentHistory}){
    return (
        <Card raised>
            <CardContent>
                <Typography variant="h6" gutterBottom>
                    Payment History
                </Typography>
                <List>
                    {paymentHistory.map((payment, index) => (
                        <React.Fragment key={payment.id}>
                            <ListItem>
                                <ListItemText
                                    primary={`Payee: ${payment.payeeName}`}
                                    secondary={
                                        <>
                                            <Typography component="span" variant="body2">
                                                Amount: ${payment.paymentAmount}
                                            </Typography>
                                            <br />
                                            <Typography component="span" variant="body2">
                                                Date: {new Date(payment.postedDate).toLocaleDateString()}
                                            </Typography>
                                            <br />
                                            <Typography component="span" variant="body2" color="textSecondary">
                                                Status: {payment.scheduledStatus}
                                            </Typography>
                                        </>
                                    }
                                />
                            </ListItem>
                            {index < paymentHistory.length - 1 && <Divider />}
                        </React.Fragment>
                    ))}
                </List>
            </CardContent>
        </Card>
    );
}

export default PaymentHistory;