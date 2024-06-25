import {
    Badge,
    ClickAwayListener,
    Grow,
    IconButton,
    List,
    ListItem,
    ListItemText,
    Paper,
    Popover,
    Typography
} from "@mui/material";
import NotificationsIcon from "@mui/icons-material/Notifications";
import {useEffect, useRef, useState} from "react";
import axios from "axios";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import CloseIcon from "@mui/icons-material/Close";
import DialogContent from "@mui/material/DialogContent";



const notificationColors = {
    TRANSACTION_ALERT: "#4caf50", // Green
    BalanceUpdate: "#2196f3", // Blue
    AccountSecurity: "#f44336", // Red
    PaymentReminder: "#ffeb3b", // Yellow
    ScheduledMaintenance: "#9c27b0", // Purple
    AccountUpdate: "#3f51b5" // Indigo
};


function NotificationBell({notificationCount, initialNotifications, onBellClick}){
    const [open, setOpen] = useState(false);
    const [notifications, setNotifications] = useState(initialNotifications);

    useEffect(() => {
        console.log('Current Notifications:', notifications);
        if (notifications.length === 0) {
            console.log('No notifications to display.');
        }
    }, [notifications]); // Dependency array with notifications

    const handleBellClick = (event) => {
        console.log('Notification Bell Clicked. Current Notifications:', notifications);
        setOpen(true);
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
        setOpen(false);
    };

    const id = open ? 'notification-popover' : undefined;

    return (
        <>
            <IconButton color="inherit" onClick={handleBellClick}>
                <Badge badgeContent={notificationCount} color="secondary">
                    <NotificationsIcon />
                </Badge>
            </IconButton>
            <Dialog
                open={open}
                onClose={handleClose}
                fullWidth
                maxWidth="sm"
            >
                <DialogTitle>
                    Notifications
                    <IconButton
                        aria-label="close"
                        onClick={handleClose}
                        sx={{
                            position: 'absolute',
                            right: 8,
                            top: 8,
                            color: (theme) => theme.palette.grey[500],
                        }}
                    >
                        <CloseIcon />
                    </IconButton>
                </DialogTitle>
                <DialogContent dividers>
                    {notifications.length === 0 ? (
                        <Typography>No notifications</Typography>
                    ) : (
                        <List>
                            {notifications.map((notification, index) => (
                                <ListItem
                                    key={index}
                                    button
                                    onClick={() => handleNotificationClick(index)}
                                    sx={{
                                        bgcolor: notificationColors[notification.accountNotificationCategory] || "#ffffff",
                                        color: notification.severe ? "#ff0000" : "#000000",
                                        fontWeight: notification.read ? "normal" : "bold",
                                        '&:hover': {
                                            bgcolor: 'action.hover',
                                        },
                                        mb: 1,
                                        borderRadius: 1,
                                    }}
                                >
                                    <ListItemText
                                        primary={notification.title}
                                        secondary={notification.message}
                                        primaryTypographyProps={{
                                            style: { color: notification.read ? "#000" : "#0000ff" },
                                            variant: 'subtitle2'
                                        }}
                                        secondaryTypographyProps={{
                                            style: { color: notification.read ? "#666" : "#444" },
                                            variant: 'body2'
                                        }}
                                    />
                                </ListItem>
                            ))}
                        </List>
                    )}
                </DialogContent>
            </Dialog>
        </>
    );

    // return (
    //     <>
    //         <IconButton color="inherit" onClick={handleBellClick}>
    //             <Badge badgeContent={notificationCount} color="secondary">
    //                 <NotificationsIcon />
    //             </Badge>
    //         </IconButton>
    //         <Popover
    //             id={id}
    //             open={open}
    //             anchorEl={anchorEl}
    //             onClose={handleClose}
    //             anchorOrigin={{
    //                 vertical: 'bottom',
    //                 horizontal: 'left',
    //             }}
    //             transformOrigin={{
    //                 vertical: 'top',
    //                 horizontal: 'center',
    //             }}
    //         >
    //             <List>
    //                 {notifications.map((notification, index) => (
    //                     <ListItem
    //                         key={index}
    //                         button
    //                         onClick={() => handleNotificationClick(index)}
    //                         style={{
    //                             backgroundColor: notificationColors[notification.accountNotificationCategory] || "#ffffff",
    //                             color: notification.severe ? "#ff0000" : "#000000",
    //                             fontWeight: notification.read ? "normal" : "bold"
    //                         }}
    //                     >
    //                         <ListItemText
    //                             primary={notification.title}
    //                             secondary={notification.message}
    //                             primaryTypographyProps={{ style: { color: notification.read ? "#000" : "#0000ff" } }}
    //                             secondaryTypographyProps={{ style: { color: notification.read ? "#666" : "#444" } }}
    //                         />
    //                     </ListItem>
    //                 ))}
    //             </List>
    //         </Popover>
    //     </>
    // );
}

export default NotificationBell;