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

export default function TransactionTable() {

    const [transactionDate, setTransactionDate] = useState('');
    const [description, setDescription] = useState('');
    const [debitAmount, setDebitAmount] = useState(0);
    const [creditAmount, setCreditAmount] = useState(0);
    const [balance, setBalance] = useState(0);
    const [transactionStatements, setTransactionStatements] = useState([]);

    useEffect(() => {
        fetchTransactionStatements();
    }, []);

    const fetchTransactionStatements = () => {
        let userID = sessionStorage.getItem('userID');
        axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/${1}`)
            .then(response => {
                if(Array.isArray(response.data) && response.data.length > 0){
                    console.log('Transaction Statements: ', response.data);
                    console.log('Transaction Description: ', response.data.description);
                    setTransactionStatements(response.data);

                }else{
                    console.log('No Statements found for this user.');
                }
            })
            .catch(error => {
                console.error('There was an error fetching the transaction statements: ', error);
            });
    }

    return (
        <TableContainer component={Paper}>
            <Table sx={{ minWidth: 650 }} aria-label="transaction table">
                <TableHead>
                    <TableRow>
                        <TableCell>Date</TableCell>
                        <TableCell>Description</TableCell>
                        <TableCell align="right">Debit</TableCell>
                        <TableCell align="right">Credit</TableCell>
                        <TableCell align="right">Balance</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {transactionStatements.map((statements, index) => (
                        <TableRow key={index}
                                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                        >
                            <TableCell>{statements.transactionDate}</TableCell>
                            <TableCell>{statements.description}</TableCell>
                            <TableCell align="right" sx={{color: statements.debit ? 'red' : 'inherit'}}>{statements.debit}</TableCell>
                            <TableCell align="right" sx={{color: statements.credit ? 'green' : 'inherit'}}>{statements.credit}</TableCell>
                            <TableCell align="right">{statements.balance}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}