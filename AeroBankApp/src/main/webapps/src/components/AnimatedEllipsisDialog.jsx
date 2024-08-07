import React, {useEffect, useState} from "react";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import {CircularProgress} from "@mui/material";

function AnimatedEllipsisDialog({showBackdrop, setShowBackDrop}){
    const [ellpsis, setEllipsis] = useState('.');

    useEffect(() => {
        const intervalId = setInterval(() => {
            setEllipsis(prev => {
                switch(prev){
                    case ".":
                        return "..";
                    case "..":
                        return '...';
                    case "...":
                        return ".";
                    default:
                        return ".";
                }
            })
        }, 500);
        return () => clearInterval(intervalId);
    }, []);

    return (
        <Dialog
            open={showBackdrop}
            onClose={() => setShowBackDrop(false)}  // Optionally allow closing by clicking outside the dialog
            aria-labelledby="registration-dialog-title"
            aria-describedby="registration-dialog-description"
        >
            <DialogTitle id="registration-dialog-title">{"Registration in Progress"}</DialogTitle>
            <DialogContent>
                <DialogContentText id="registration-dialog-description">
                    Please wait while we register your account{ellpsis}
                </DialogContentText>
                <CircularProgress color="inherit" />
            </DialogContent>
        </Dialog>
    );
}
export default AnimatedEllipsisDialog;