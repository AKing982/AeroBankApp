import {Button, Paper, Typography} from "@mui/material";
import {Box} from "@mui/system";
import DownloadIcon from '@mui/icons-material/Download';

function TransactionDetails({transaction}){
    return (
        <Paper elevation={3} sx={{ padding: 2, margin: 2 }}>
            <Box sx={{ my: 2 }}>
                <Typography variant="subtitle1">Transaction ID: {transaction.id}</Typography>
                <Typography variant="subtitle1">Date: {transaction.transactionDate}</Typography>
                <Typography variant="subtitle1">Debit: {transaction.debit}</Typography>
                <Typography variant="subtitle1">Credit: {transaction.credit}</Typography>
                <Typography variant="subtitle1">Balance: {transaction.balance}</Typography>
                <Typography variant="subtitle1">Type: {transaction.type}</Typography>
                <Typography variant="subtitle1">Status: {transaction.status}</Typography>
                <Typography variant="subtitle1">Method: {transaction.method}</Typography>
                <Typography variant="subtitle1">Payee: {transaction.payee}</Typography>
                <Typography variant="subtitle1">Description: {transaction.description}</Typography>
                <Typography variant="subtitle1">Category: {transaction.category}</Typography>
                <Typography variant="subtitle1">Fee: ${transaction.fee}</Typography>
            </Box>
            <Button startIcon={<DownloadIcon />} variant="contained">
                Download Receipt
            </Button>
        </Paper>
    );
}

export default TransactionDetails;