import React, {useEffect, useState} from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Typography
} from '@mui/material';
import axios from "axios";


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
    const [randomAcctID, setRandomAcctID] = useState(0);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchAccountAndTransactions = async () => {
            setIsLoading(true);
            const userID = sessionStorage.getItem('userID');
            let acctID = sessionStorage.getItem('accountID');

            if (!acctID) {
                try {
                    const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/rand/${userID}`);
                    acctID = response.data;
                    console.log('Randomly selected accountID: ',  acctID);
                    sessionStorage.setItem('accountID', acctID);
                } catch (error) {
                    console.error('Error fetching a random acctID:', error);
                    setIsLoading(false);
                    return;
                }
            }

            try {
                const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/${acctID}`);
                if (response.data.length > 0) {
                    setTransactionStatements(response.data);
                } else {
                    console.log('No Statements found for this account.');
                }
            } catch (error) {
                console.error('There was an error fetching the transaction statements:', error);
            } finally {
                setIsLoading(false);
            }
        };

        fetchAccountAndTransactions();
    }, []);

    // Group transactions by date
    const transactionsGroupedByDate = transactionStatements.reduce((acc, statement) => {
        const date = statement.transactionDate;
        if (!acc[date]) {
            acc[date] = [];
        }
        acc[date].push(statement);
        return acc;
    }, {});

    return (
        <TableContainer component={Paper} sx={{maxHeight: 500}}>
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
                    {Object.entries(transactionsGroupedByDate).map(([date, statements], index) => (
                        <React.Fragment key={date}>
                            <TableRow>
                                <TableCell colSpan={4} sx={{ backgroundColor: '#0E0F52', color: 'white' }}>
                                    <Typography variant="subtitle1" gutterBottom>
                                        {date}
                                    </Typography>
                                </TableCell>
                            </TableRow>
                            {statements.map((statement, stmtIndex) => (
                                <TableRow
                                    key={`${date}-${stmtIndex}`}
                                    sx={{
                                        '&:last-child td, &:last-child th': { border: 0 },
                                        '& td, & th': {
                                            borderBottom: '1px solid rgba(224, 224, 224, 1)',
                                            borderRight: 'none',
                                        }
                                    }}
                                >
                                    <TableCell component="th" scope="row">
                                        {statement.description}
                                    </TableCell>
                                    <TableCell align="right" sx={{ color: statement.debit ? 'red' : 'inherit' }}>
                                        {statement.debit}
                                    </TableCell>
                                    <TableCell align="right" sx={{ color: statement.credit ? 'green' : 'inherit' }}>
                                        {statement.credit}
                                    </TableCell>
                                    <TableCell align="right">
                                        {statement.balance}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </React.Fragment>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}