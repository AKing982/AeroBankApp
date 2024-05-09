import React from 'react';
import { List, ListItem, ListItemText, IconButton, Tooltip } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';

function BillScheduler({ payees }){
    return (
        <List sx={{
            width: '100%',
            maxWidth: 360,
            bgcolor: 'rgba(255, 255, 255, 0.7)', // Semi-transparent background
            borderRadius: '8px', // Optional: rounded corners for aesthetic
            boxShadow: 1, // Optional: adds shadow for better separation from background
            overflow: 'hidden', // Ensures the background radius applies properly
        }}>
            {payees.map(payee => (
                <ListItem
                    key={payee.name}
                    divider
                    sx={{ justifyContent: 'space-between', padding: '10px 20px', bgcolor: 'transparent' }} // Ensure items are transparent
                >
                    <ListItemText
                        primary={payee.name}
                        secondary={`Next payment of $${payee.amount} due on ${payee.nextPaymentDue}`}
                        primaryTypographyProps={{ noWrap: false }}
                        secondaryTypographyProps={{ noWrap: false }}
                        sx={{ minWidth: 0, mr: 2 }}
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