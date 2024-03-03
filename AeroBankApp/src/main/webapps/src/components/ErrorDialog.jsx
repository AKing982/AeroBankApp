import DialogTitle from "@mui/material/DialogTitle";
import Dialog from "@mui/material/Dialog";
import {Accordion, AccordionDetails, AccordionSummary, Button, TextareaAutosize, Typography} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import {useState} from "react";


export default function ErrorDialog({ open, onClose, title, headerText, contentText, exceptionText })
{
    const [expanded, setExpanded] = useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="md" fullWidth>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent dividers>
                <Typography gutterBottom>{headerText}</Typography>
                <Typography gutterBottom>{contentText}</Typography>
                <Accordion expanded={expanded} onChange={handleExpandClick}>
                    <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                        <Typography>The exception stacktrace was:</Typography>
                    </AccordionSummary>
                    <AccordionDetails>
                        <TextareaAutosize
                            aria-label="Exception stacktrace"
                            minRows={5}
                            style={{ width: '100%' }}
                            value={exceptionText}
                            readOnly
                        />
                    </AccordionDetails>
                </Accordion>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Close</Button>
            </DialogActions>
        </Dialog>
    );
}