import {Badge, IconButton, List, ListItem, ListItemText, Popover} from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import {useState} from "react";



const notificationColors = {
    TransactionAlert: "#4caf50", // Green
    BalanceUpdate: "#2196f3", // Blue
    AccountSecurity: "#f44336", // Red
    PaymentReminder: "#ffeb3b", // Yellow
    ScheduledMaintenance: "#9c27b0", // Purple
    AccountUpdate: "#3f51b5" // Indigo
};


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
                    {notifications.map((notification, index) => (
                        <ListItem
                            key={index}
                            button
                            style={{ backgroundColor: notificationColors[notification.category] || "#ffffff" }}
                        >
                            <ListItemText
                                primary={notification.title}
                                secondary={notification.message}
                                primaryTypographyProps={{ style: { color: "#fff" } }}
                                secondaryTypographyProps={{ style: { color: "#fff" } }}
                            />
                        </ListItem>
                    ))}
                </List>
            </Popover>
        </>
    );
}

export default NotificationBell;