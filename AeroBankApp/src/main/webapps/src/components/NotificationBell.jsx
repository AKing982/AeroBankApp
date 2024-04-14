import {Badge, IconButton, List, ListItem, ListItemText, Popover} from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import {useEffect, useState} from "react";
import axios from "axios";



const notificationColors = {
    TRANSACTION_ALERT: "#4caf50", // Green
    BalanceUpdate: "#2196f3", // Blue
    AccountSecurity: "#f44336", // Red
    PaymentReminder: "#ffeb3b", // Yellow
    ScheduledMaintenance: "#9c27b0", // Purple
    AccountUpdate: "#3f51b5" // Indigo
};


function NotificationBell({notificationCount, initialNotifications, onBellClick}){
    const [anchorEl, setAnchorEl] = useState(null);
    const [notifications, setNotifications] = useState(initialNotifications);


    useEffect(() => {
        console.log('Current Notifications:', notifications);
        if (notifications.length === 0) {
            console.log('No notifications to display.');
        }
    }, [notifications]); // Dependency array with notifications

    const handleBellClick = (event) => {
        console.log('Notification Bell Clicked. Current Notifications:', notifications);
        setAnchorEl(event.currentTarget);
    };

    const handleNotificationClick = async(index) => {
        const notificationID = notifications[index].notificationID;
        const acctID = notifications[index].accountID;
        console.log('Notifications: ', notifications[index]);
        console.log('NotificationID: ', notificationID);
        console.log('AccountID: ', acctID);

        try
        {
            await axios.put(`http://localhost:8080/AeroBankApp/api/accounts/notifications/update/${acctID}/${notificationID}`);
            const newNotifications = notifications.filter((_, i) => i !== index);
            setNotifications(newNotifications);

            onBellClick(newNotifications.length);

        }catch(error){
            console.error("Failed to mark notification as read: ", error);
        }
        // Create a new array without the clicked notification

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
                            onClick={() => handleNotificationClick(index)}
                            style={{
                                backgroundColor: notificationColors[notification.accountNotificationCategory] || "#ffffff",
                                color: notification.severe ? "#ff0000" : "#000000",
                                fontWeight: notification.read ? "normal" : "bold"
                            }}
                        >
                            <ListItemText
                                primary={notification.title}
                                secondary={notification.message}
                                primaryTypographyProps={{ style: { color: notification.read ? "#000" : "#0000ff" } }}
                                secondaryTypographyProps={{ style: { color: notification.read ? "#666" : "#444" } }}
                            />
                        </ListItem>
                    ))}
                </List>
            </Popover>
        </>
    );
}

export default NotificationBell;