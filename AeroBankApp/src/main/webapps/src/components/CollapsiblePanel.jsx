import {useState} from "react";
import {Accordion} from "@mui/material";
import {AccordionSummary} from "@mui/material";
import {AccordionDetails} from "@mui/material";
import {
    Typography,
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

export default function CollapsiblePanel({title, content})
{

    return (
       <Accordion>
           <AccordionSummary
               expandIcon={<ExpandMoreIcon />}
               aria-controls="panel-content"
               id="panel-header"
               >
               <Typography variant="h6" style={{textAlign: 'center'}}>{title}</Typography>
           </AccordionSummary>
           <AccordionDetails>
               <div>{content}</div>
           </AccordionDetails>
       </Accordion>
    );
}