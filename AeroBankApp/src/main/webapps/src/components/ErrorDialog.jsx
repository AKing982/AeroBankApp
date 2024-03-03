import DialogTitle from "@mui/material/DialogTitle";
import Dialog from "@mui/material/Dialog";
import {Accordion, AccordionDetails, AccordionSummary, Button, TextareaAutosize, Typography} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import CancelIcon from '@mui/icons-material/Cancel';
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
                <div style={{ display: 'flex', alignItems: 'center', marginBottom: '16px' }}>
                    <CancelIcon style={{ color: 'red', marginRight: '8px', fontSize: '40px' }} /> {/* Big red X icon */}
                    <Typography variant="h5" component="h2">{headerText}</Typography>
                </div>
                <Typography gutterBottom>{contentText}</Typography>
                <Accordion expanded={expanded} onChange={() => setExpanded(!expanded)}>
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