import React, {useEffect, useState} from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography, Alert, CircularProgress
} from '@mui/material';
import axios from "axios";
import {Skeleton} from "@mui/lab";
import { IconButton } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';   // Simpler back arrow
import ArrowForwardIcon from '@mui/icons-material/ArrowForward'; // Simpler forward arrow
import {Box} from "@mui/system";

function createData(date, description, debit, credit, balance) {
    return { date, description, debit, credit, balance };
}


const rows = [
    createData('February 4, 2024', 'PIN Purchase HARMONS - DIST 11453 S. PARKWAY South', '-$20.84', '', '$931.02'),
    createData('February 4, 2024', 'PIN Purchase WALMART', '-$15.84', '', '$915.02'),
    createData('February 5, 2024', 'PIN Purchase WINCO', '-$10.00', '', '$905.02'),
    createData('February 6, 2024', 'Deposit Transfer', '', '$10.00', '$915.02')
    // ... add more row data here
];

export default function TransactionTable({accountID}) {

    const [transactionStatements, setTransactionStatements] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [currentDate, setCurrentDate] = useState(new Date());

    const handlePrevDate = () => {
        const newDate = new Date(currentDate);
        newDate.setMonth(currentDate.getMonth() - 1);
        setCurrentDate(newDate);
    };

    const handleNextDate = () => {
        const newDate = new Date(currentDate);
        newDate.setMonth(currentDate.getMonth() + 1);
        setCurrentDate(newDate);
    }

    useEffect(() => {
        const fetchAccountAndTransactions = async () => {

            setIsLoading(true);
            await new Promise(resolve => setTimeout(resolve, 4000)); // Simulating network delay
            const userID = sessionStorage.getItem('userID');
            let acctID = sessionStorage.getItem('accountID');

            if (!acctID) {
                try {
                    const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/maxTransactions/${userID}`);
                    acctID = response.data;
                    console.log('Fetching AccountID: ', acctID);
                    sessionStorage.setItem('accountID', acctID);
                } catch (error) {
                    console.error('Error fetching AccountID:', error);
                    setErrorMessage('Unable to fetch transactions. Please Try again later.');
                    setIsLoading(false);
                    return;
                }
            }

            try {
                if(accountID !== 0){
                    setErrorMessage('');
                }
                console.log('AccountID Before call: ', accountID);
                const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/${accountID}`);
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
    }, [accountID]);

    // Group transactions by date for the actual data rendering
    const transactionsGroupedByDate = transactionStatements.reduce((acc, statement) => {
        const date = statement.transactionDate; // Assuming transactionDate is the correct field
        if (!acc[date]) {
            acc[date] = [];
        }
        acc[date].push(statement);
        return acc;
    }, {});

    return (
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
                                {statements.map((statement, stmtIndex) => (
                                    <TableRow key={`${date}-${stmtIndex}`}
                                              sx={{
                                                  '&:last-child td, &:last-child th': { border: 0 },
                                                  '& td, & th': {
                                                      borderBottom: '1px solid rgba(224, 224, 224, 1)',
                                                      borderRight: 'none',
                                                  }
                                              }}>
                                        <TableCell>{statement.description}</TableCell>
                                        <TableCell align="right" sx={{ color: statement.debit > 0 ? 'red' : 'inherit' }}>{statement.debit}</TableCell>
                                        <TableCell align="right" sx={{ color: statement.credit > 0 ? 'green' : 'inherit'}}>{statement.credit}</TableCell>
                                        <TableCell align="right">{statement.balance}</TableCell>
                                    </TableRow>
                                ))}
                            </React.Fragment>
                        ))
                    )}
                </TableBody>
            </Table>
        </TableContainer>
    );
}