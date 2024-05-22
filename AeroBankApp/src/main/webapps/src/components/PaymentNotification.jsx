import {Alert, Card, CardContent, Divider, List, ListItem, Typography} from "@mui/material";
import {AlertTitle} from "@mui/lab";
import React from 'react';

function PaymentNotification({notifications}){
    return (
        <Card raised>
            <CardContent>
                <Typography variant="h6" gutterBottom>
                    Notifications
                </Typography>
                <List>
                    {notifications.map((notification, index) => (
                        <React.Fragment key={notification.id}>
                            <ListItem>
                                <Alert severity={notification.severity}>
                                    <AlertTitle>{notification.title}</AlertTitle>
                                    {notification.message}
                                </Alert>
                            </ListItem>
                            {index < notifications.length - 1 && <Divider />}
                        </React.Fragment>
                    ))}
                </List>
            </CardContent>
        </Card>
    );
}

export default PaymentNotification;