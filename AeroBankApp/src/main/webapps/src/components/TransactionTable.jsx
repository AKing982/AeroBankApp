import React from 'react';
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

function createData(date, description, debit, credit, balance) {
    return { date, description, debit, credit, balance };
}

function fetchTransactionData()
{

}

const rows = [
    createData('February 4, 2024', 'PIN Purchase HARMONS - DIST 11453 S. PARKWAY South', '-$20.84', '', '$931.02'),
    createData('February 4, 2024', 'PIN Purchase WALMART', '-$15.84', '', '$915.02'),
    createData('February 5, 2024', 'PIN Purchase WINCO', '-$10.00', '', '$905.02'),
    createData('February 6, 2024', 'Deposit Transfer', '', '$10.00', '$915.02')
    // ... add more row data here
];

export default function TransactionTable() {
    return (
        <TableContainer component={Paper}>
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
                    {rows.map((row, index) => (
                        <React.Fragment key={index}>
                            {index === 0 || rows[index-1].date !== row.date ? (
                                <TableRow>
                                    <TableCell colSpan={4} sx={{backgroundColor: '#0E0F52', color: 'white'}}>
                                        <Typography variant="subtitle1" gutterBottom>
                                            {row.date}
                                        </Typography>
                                    </TableCell>
                                </TableRow>
                            ) : null}
                            <TableRow
                                sx={{
                                    '&:last-child td, &:last-child th': { border: 0 },
                                    '& td, & th': {
                                        borderBottom: '1px solid rgba(224, 224, 224, 1)', // Apply horizontal line
                                        borderRight: 'none', // Remove vertical lines
                                    } }}
                            >
                                <TableCell component="th" scope="row">
                                    {row.description}
                                </TableCell>
                                <TableCell align="right" sx={{color: row.debit ? 'red' : 'inherit'}}>{row.debit}</TableCell>
                                <TableCell align="right" sx={{color: row.credit ? 'green' : 'inherit'}}>{row.credit}</TableCell>
                                <TableCell align="right">{row.balance}</TableCell>
                            </TableRow>
                        </React.Fragment>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}