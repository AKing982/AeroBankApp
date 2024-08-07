import {useTheme} from "@mui/system";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    useMediaQuery
} from "@mui/material";
import React from 'react';

export default function ResponsiveDialog({onAgree, onClose}) {
    const [open, setOpen] = React.useState(false);
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleAgree = () => {
        handleClose();
        if (onAgree) {
            onAgree();
        }
    };


    return (
        <React.Fragment>
            <Button variant="outlined" onClick={handleClickOpen}>
        Test Email Dialog
    </Button>
    <Dialog
    fullScreen={fullScreen}
    open={open}
    onClose={onClose}
    aria-labelledby="responsive-dialog-title"
    >
    <DialogTitle id="responsive-dialog-title">
        Test Email Dialog
        </DialogTitle>
        <DialogContent>
        <DialogContentText>
            Are you sure you want to send a test email?
    </DialogContentText>
    </DialogContent>
    <DialogActions>
    <Button autoFocus onClick={onClose}>
        Disagree
        </Button>
        <Button onClick={onAgree} autoFocus>
    Agree
    </Button>
    </DialogActions>
    </Dialog>
    </React.Fragment>
);
}