import React, {useState} from "react";
import {Avatar, Box, Card, CardContent, Collapse, IconButton, Typography} from "@mui/material";
import NotificationBell from "../components/NotificationBell";
import ExpandLessIcon from "@mui/icons-material/ExpandLess";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

interface AccountProps{
    color: string;
    accountCode: string;
    acctID: number;
    acctCodeID: number;
    accountName: string;
    balance: number;
    pending: number;
    available: number;
    notifications: any[];
    onNotificationClick: (count: number) => void;
    notificationCount: number;
    onAccountClick: (id: number) => void;
    isSelected: boolean;
    backgroundImageUrl: string;

}

export default function({ color, accountCode, acctID, acctCodeID, accountName, balance, pending, available, notifications, onNotificationClick, notificationCount, onAccountClick, isSelected, backgroundImageUrl} : AccountProps){
    const [newNotificationCount, setNewNotificationCount] = useState<number>(notifications.length);
    const [expanded, setExpanded] = useState<boolean>(false);

    const handleUpdateNotificationCount = (newCount: number) => {
        setNewNotificationCount(newCount);
    }

    const handleClick = () : void => {
        onAccountClick(acctID);
    };

    const handleKeyPress = (event: React.KeyboardEvent<HTMLButtonElement>): void => {
        if (event.key === 'Enter' || event.key === ' ') {
            handleClick();
        }
    };

    const handleOnNotificationClick = (event: React.MouseEvent<HTMLButtonElement>) : void => {
        // Send a request to fetch notifications for the account with accountID
        //  event.stopPropagation();
        //  console.log('Notification Clicked');
        //  onNotificationClick(accountCode);
    };

    const defaultColor = '#9e9e9e'; // Default color if none is provided
    const avatarColor = color || defaultColor; // Use provided color or default

    // Adding a semi-transparent overlay if the account is selected
    const backgroundImageWithOverlay = isSelected ?
        `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url(${backgroundImageUrl})` :
        `url(${backgroundImageUrl})`;


    const avatarStyle = {

        bgcolor: avatarColor,
        width: 56,
        height: 56,
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: '16px',
        // If the selected color is a gradient, we'll use the text color that ensures readability
        color: 'contrastText', // Adjust text color for readability if necessary
    };

    const handleExpandClick = (event: React.MouseEvent) => {
        event.stopPropagation(); // Prevent the card click event from firing
        setExpanded(!expanded);
    };


    // return (
    //     <Card
    //         tabIndex="0"
    //         role="button"
    //         onClick={handleClick}
    //         onKeyPress={handleKeyPress}
    //         sx={{
    //             cursor: 'pointer',
    //             m: 1,
    //             transition: 'background-color 0.3s',
    //             backgroundImage: backgroundImageWithOverlay,
    //             backgroundSize: 'cover',
    //             backgroundPosition: 'center',
    //             '&:focus-visible': {
    //                 boxShadow: '0 0 0 2px #ff8e53',
    //             },
    //         }}
    //     >
    //         <CardContent sx={{ display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
    //             {/* Inner Card for Account Details */}
    //             <Card sx={{ bgcolor: 'white', p: 2, mr: 2, minWidth: 200, position: 'right' }}>
    //                 {notificationCount > 0 && (
    //                     <Box sx={{
    //                         position: 'relative',
    //                         top: 0,
    //                         right: 0,
    //                         transform: 'translate(40%, -20%)' // Adjusts the bell icon to be partly outside the card
    //                     }}>
    //                         <NotificationBell
    //                             notificationCount={newNotificationCount}
    //                             initialNotifications={notifications}
    //                             onBellClick={handleUpdateNotificationCount}
    //                         />
    //                     </Box>
    //                 )}
    //                 <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2 }}>
    //                     <Avatar sx={avatarStyle}>
    //                         <Typography variant="subtitle1">
    //                             {accountCode}
    //                         </Typography>
    //                     </Avatar>
    //                 </Box>
    //                 <Typography variant="h6" component="div" sx={{ mb: 2, textAlign: 'center' }}>
    //                     {accountName}
    //                 </Typography>
    //                 <Typography variant="body1" sx={{ mb: 1 }}>Balance: <strong>${balance}</strong></Typography>
    //                 <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
    //                     <IconButton
    //                         onClick={handleExpandClick}
    //                         aria-expanded={expanded}
    //                         aria-label="show more"
    //                         size="small"
    //                     >
    //                         {expanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
    //                     </IconButton>
    //                 </Box>
    //
    //                 <Collapse in={expanded} timeout="auto" unmountOnExit>
    //                     <Box sx={{ mt: 1 }}>
    //                         <Typography variant="body1" sx={{ mb: 1 }}>Pending: <strong>${pending}</strong></Typography>
    //                         <Typography variant="body1">Available: <strong>${available}</strong></Typography>
    //                     </Box>
    //                 </Collapse>
    //                 {/*<Typography variant="body1" sx={{ mb: 1 }}>Pending: <strong>${pending}</strong></Typography>*/}
    //                 {/*<Typography variant="body1">Available: <strong>${available}</strong></Typography>*/}
    //             </Card>
    //             {/* Existing Content */}
    //
    //
    //         </CardContent>
    //     </Card>
    // );
    return null;
}