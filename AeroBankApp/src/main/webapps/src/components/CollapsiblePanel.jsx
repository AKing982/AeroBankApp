import {useEffect, useState} from "react";
import {Accordion} from "@mui/material";
import {AccordionSummary} from "@mui/material";
import {AccordionDetails} from "@mui/material";
import {
    Typography,
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import axios from "axios";

export default function CollapsiblePanel({title, content})
{
    const [totalPending, setTotalPending] = useState(0);

    useEffect(() => {
        axios.get('http://localhost:8080/AeroBankApp/api/pending/size')
            .then(response => {
                setTotalPending(response.data);
            })
            .catch(error => {
                console.log('An error occurred while fetching pending transactions size: ', error);
            })
    }, []);

    return (
       <Accordion>
           <AccordionSummary
               expandIcon={<ExpandMoreIcon />}
               aria-controls="panel-content"
               id="panel-header"
               >
               <Typography variant="h6" style={{textAlign: 'center'}}>({totalPending}) {title}</Typography>
           </AccordionSummary>
           <AccordionDetails>
               <div>{content}</div>
           </AccordionDetails>
       </Accordion>
    );
}