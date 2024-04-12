import {Badge, IconButton, List, ListItem, ListItemText, Popover} from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import {useState} from "react";

const testNotifications = [
    {
        id: 1,
        title: "Payment Received",
        message: "You have received a payment of $150 from John Doe."
    },
    {
        id: 2,
        title: "Account Alert",
        message: "Your account balance is lower than $100."
    },
    {
        id: 3,
        title: "Scheduled Maintenance",
        message: "Our banking services will be unavailable this Sunday from 2 AM to 5 AM due to scheduled maintenance."
    },
    {
        id: 4,
        title: "New Offer",
        message: "A new savings account with an attractive interest rate is available now. Check it out!"
    }
];


function NotificationBell({notificationCount, notifications, onBellClick}){
    const [anchorEl, setAnchorEl] = useState(null);

    const handleBellClick = (event) => {
        onBellClick();  // Fetch notifications on click, if not already passed
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const open = Boolean(anchorEl);
    const id = open ? 'notification-popover' : undefined;

    return (
        <>
            <IconButton color="inherit" onClick={handleBellClick}>
                <Badge badgeContent={notificationCount} color="secondary">
                    <NotificationsIcon />
                </Badge>
            </IconButton>
            <Popover
                id={id}
                open={open}
                anchorEl={anchorEl}
                onClose={handleClose}
                anchorOrigin={{
                    vertical: 'bottom',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'center',
                }}
            >
                <List>
                    {testNotifications.map((notification, index) => (
                        <ListItem key={index} button>
                            <ListItemText primary={notification.title} secondary={notification.message} />
                        </ListItem>
                    ))}
                </List>
            </Popover>
        </>
    );
}

export default NotificationBell;