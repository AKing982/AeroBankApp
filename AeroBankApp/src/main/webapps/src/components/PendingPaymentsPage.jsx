import {Box} from "@mui/system";
import {Divider, Typography} from "@mui/material";

function PendingPaymentsPage()
{
    return(
        <Box sx={{flexGrow: 1, p: 3 }}>
            <Typography variant="h4" gutterBottom>
                Transfers
            </Typography>
            <Divider />
        </Box>
    )
}

export default PendingPaymentsPage;