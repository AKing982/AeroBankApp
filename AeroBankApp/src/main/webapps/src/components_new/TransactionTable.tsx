import React, {FunctionComponent, useEffect, useState} from "react";
import axios from "axios";
import {
    Alert, Button,
    CircularProgress,
    IconButton,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import {Box} from "@mui/system";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import ArrowForwardIcon from "@mui/icons-material/ArrowForward";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import TransactionDetails from "../components/TransactionDetails";
import DialogActions from "@mui/material/DialogActions";

interface TransactionTableProps {
    accountId?: string;
}

interface Statement {
    description: string;
    debit: number;
    credit: number;
    balance: number;
    transactionDate: string | Date
}

interface TransactionDetailsProps{
    transaction: any;
    open: boolean;
    close: () => void;
}

const TransactionTable: FunctionComponent = ({accountId}: TransactionTableProps) => {
    const [transactionStatements, setTransactionStatements] = useState<Statement[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    const [errorMessage, setErrorMessage] = useState<string>('');
    const [currentDate, setCurrentDate] = useState<Date>(new Date());
    const [selectedTransaction, setSelectedTransaction] = useState<any>(null);
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);

    const handlePrevDate = () : void => {
        const newDate = new Date(currentDate);
        newDate.setMonth(currentDate.getMonth() - 1);
        setCurrentDate(newDate);
    };

    const handleNextDate = () : void => {
        const newDate = new Date(currentDate);
        newDate.setMonth(currentDate.getMonth() + 1);
        setCurrentDate(newDate);
    }

    useEffect(() => {
        const fetchAccountAndTransactions = async () => {

            setIsLoading(true);
            await new Promise(resolve => setTimeout(resolve, 4000)); // Simulating network delay
            const userID = sessionStorage.getItem('userID');
            let acctID: string | null = sessionStorage.getItem('accountID');

            if (!acctID) {
                try {
                    const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/maxTransactions/${userID}`);
                    acctID = response.data;
                    console.log('Fetching AccountID: ', acctID);
                    if (typeof acctID === "string") {
                        sessionStorage.setItem('accountID', acctID);
                    }
                } catch (error) {
                    console.error('Error fetching AccountID:', error);
                    setErrorMessage('Unable to fetch transactions. Please Try again later.');
                    setIsLoading(false);
                    return;
                }
            }

            try {
                if(acctID !== '0'){
                    setErrorMessage('');
                }
                console.log('AccountID Before call: ', acctID);
                const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/${acctID}`);
                if (response.data.length > 0) {
                    setTransactionStatements(response.data);
                } else {
                    console.log('No Statements found for this account.');
                }
            } catch (error) {
                console.error('There was an error fetching the transaction statements:', error);
                setErrorMessage('Unable to fetch transactions. Please Try again later.');
            } finally {
                setIsLoading(false);

            }
        };

        fetchAccountAndTransactions();
    }, []);

    // Group transactions by date for the actual data rendering
    const transactionsGroupedByDate = transactionStatements.reduce((acc: {[key: string]: Statement[]}, statement : Statement) => {
        const date = String(statement.transactionDate); // Assuming transactionDate is the correct field
        if (!acc[date]) {
            acc[date] = [];
        }
        acc[date].push(statement);
        return acc;
    }, {});

    const handleRowClick = (statement: any) => {
        setSelectedTransaction(statement);
        setIsDialogOpen(true);
    };

    const handleCloseDialog = () => {
        setIsDialogOpen(false);
        //setSelectedTransaction(null);
    };

    return (
        <>
            <TableContainer component={Paper} sx={{ maxHeight: 1080 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: 2 }}>
                    <IconButton onClick={handlePrevDate} aria-label="previous month">
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h6">
                        Transactions for {currentDate.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
                    </Typography>
                    <IconButton onClick={handleNextDate} aria-label="next month">
                        <ArrowForwardIcon />
                    </IconButton>
                </Box>
                <Table sx={{ minWidth: 650 }} aria-label="transaction table">
                    <TableHead>
                        <TableRow>
                            <TableCell>Description</TableCell>
                            <TableCell align="right">Debit</TableCell>
                            <TableCell align="right">Credit</TableCell>
                            <TableCell align="right">Balance</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {isLoading ? (
                            <TableRow><TableCell colSpan={4}><CircularProgress /></TableCell></TableRow>
                        ) : errorMessage ? (
                            <TableRow>
                                <TableCell colSpan={4}>
                                    <Alert severity="error">{errorMessage}</Alert>
                                </TableCell>
                            </TableRow>
                        ) : (
                            Object.entries(transactionsGroupedByDate).map(([date, statements]) => (
                                <React.Fragment key={date}>
                                    <TableRow>
                                        <TableCell colSpan={4} sx={{ backgroundColor: '#0E0F52', color: 'white' }}>
                                            <Typography variant="subtitle1" gutterBottom>
                                                {date}
                                            </Typography>
                                        </TableCell>
                                    </TableRow>
                                    {(statements as Statement[]).map((statement, stmtIndex) => (
                                        <TableRow key={`${date}-${stmtIndex}`}
                                                  onClick={() => handleRowClick(statement)}
                                                  sx={{
                                                      cursor: 'pointer',
                                                      '&:last-child td, &:last-child th': { border: 0 },
                                                      '& td, & th': {
                                                          borderBottom: '1px solid rgba(224, 224, 224, 1)',
                                                          borderRight: 'none',
                                                      }
                                                  }}>
                                            <TableCell>{statement.description}</TableCell>
                                            <TableCell align="right" sx={{ color: statement.debit > 0 ? 'red' : 'inherit' }}>{statement.debit}</TableCell>
                                            <TableCell align="right" sx={{ color: statement.credit > 0 ? 'green' : 'inherit' }}>{statement.credit}</TableCell>
                                            <TableCell align="right">{statement.balance}</TableCell>
                                        </TableRow>
                                    ))}
                                </React.Fragment>
                            ))
                        )}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={isDialogOpen} onClose={handleCloseDialog}>
                <DialogTitle>Transaction Details</DialogTitle>
                <DialogContent>
                    {selectedTransaction && <TransactionDetails transaction={selectedTransaction} open={isDialogOpen} close={handleCloseDialog} />}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>
        </>
    );
}