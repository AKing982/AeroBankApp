// @ts-ignore
import React, {useEffect, useState} from "react";
import axios from "axios";
import {Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from "@mui/material";

interface TransactionStatement {
    date: string;
    description: string;
    debit: string;
    credit: string;
    balance: string;
}

const TransactionStatementTable: React.FC = () => {
    const [transactionStatements, setTransactionStatements] = useState<TransactionStatement[]>([]);

    useEffect(() => {
        fetchTransactionStatements();
    } ,[]);

    const fetchTransactionStatements = () => {
        let userID = sessionStorage.getItem('userID');
        axios.get<TransactionStatement[]>(`http://localhost:8080/AeroBankApp/api/statements/${userID}`)
            .then(response => {
                if(Array.isArray(response.data) && response.data.length > 0){
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
                            <TableCell>{statements.date}</TableCell>
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
};

export default TransactionStatementTable;