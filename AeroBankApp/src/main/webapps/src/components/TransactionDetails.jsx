import {Button, IconButton, Paper, Table, TableBody, TableCell, TableRow, Typography} from "@mui/material";
import {Box} from "@mui/system";
import DownloadIcon from '@mui/icons-material/Download';
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import Dialog from "@mui/material/Dialog";
import PrintIcon from '@mui/icons-material/Print';
import CloseIcon from '@mui/icons-material/Close';
import HelpOutlineIcon from '@mui/icons-material/HelpOutline';

function TransactionDetails({transaction, open, close}){
    return (
        <Dialog open={open} onClose={close} maxWidth="md" fullWidth>
            <DialogTitle>
                <Box display="flex" justifyContent="space-between" alignItems="center">
                    <Typography variant="h6">Transaction Detail</Typography>
                    <Box>
                        <IconButton aria-label="help">
                            <HelpOutlineIcon />
                        </IconButton>
                        <IconButton aria-label="print">
                            <PrintIcon />
                        </IconButton>
                        <IconButton aria-label="close" onClick={close}>
                            <CloseIcon />
                        </IconButton>
                    </Box>
                </Box>
            </DialogTitle>
            <DialogContent>
                <Table>
                    <TableBody>
                        <TableRow>
                            <TableCell component="th" scope="row">Description</TableCell>
                            <TableCell>{transaction.type}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell component="th" scope="row">Detail Description</TableCell>
                            <TableCell>{transaction.description}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell component="th" scope="row">Date</TableCell>
                            <TableCell>{transaction.transactionDate}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell component="th" scope="row">Transaction Amount</TableCell>
                            <TableCell style={{ color: transaction.debit ? 'red' : 'green' }}>
                                {transaction.debit ? `-${transaction.debit}` : `${transaction.credit}`}
                            </TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell component="th" scope="row">New Balance</TableCell>
                            <TableCell style={{ color: 'green' }}>{transaction.balance}</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell component="th" scope="row">Reference Number</TableCell>
                            <TableCell>{transaction.id}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
            </DialogContent>
            <Box display="flex" justifyContent="flex-end" p={2}>
                <Button
                    onClick={close}
                    color="primary"
                    style={{textTransform: 'uppercase'}}
                    >
                    OK
                </Button>
            </Box>
        </Dialog>
    );
}

export default TransactionDetails;