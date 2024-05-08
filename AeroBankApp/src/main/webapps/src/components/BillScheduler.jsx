import React from 'react';
import { List, ListItem, ListItemText, IconButton, Tooltip } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';

function BillScheduler({ payees }){
    return (
        <List sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}>
            {payees.map(payee => (
                <ListItem
                    key={payee.name}
                    divider
                    sx={{ justifyContent: 'space-between', padding: '10px 20px' }} // Adjust padding as needed
                >
                    <ListItemText
                        primary={payee.name}
                        secondary={`Next payment of $${payee.amount} due on ${payee.nextPaymentDue}`}
                        primaryTypographyProps={{ noWrap: false }} // Ensure text wraps if needed
                        secondaryTypographyProps={{ noWrap: false }}
                        sx={{ minWidth: 0, mr: 2 }} // Adjust text spacing and wrapping
                    />
                    <Tooltip title="Edit Payment">
                        <IconButton edge="end" aria-label="edit">
                            <EditIcon />
                        </IconButton>
                    </Tooltip>
                </ListItem>
            ))}
        </List>
    );
}

export default BillScheduler;