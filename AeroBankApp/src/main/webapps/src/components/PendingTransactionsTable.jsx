import React, {useEffect, useState} from 'react';
import {
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper, Alert, Chip, Avatar
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import axios from "axios";
import {Skeleton} from "@mui/lab";
import {HourglassEmpty} from "@mui/icons-material";


function createData(description, debit, credit, balance, status) {
    return { description, debit, credit, balance, status };
}

const pendingTransactions = [
    createData('PURCHASE 933334 FIREHOUSE SUBS 1178 ECOSOUTH ', '-$10.24', '', '', 'pending'),
    // ... add more pending transactions here
];

export default function PendingTransactionsTable({accountID}) {
    const [expanded, setExpanded] = useState(false);
    const [totalPending, setTotalPending]= useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const [pendingTransactions, setPendingTransactions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (panel) => (event, isExpanded) => {
        setExpanded(isExpanded ? panel : false);
    };

    useEffect(() => {
        const fetchPendingTransactions = async () => {
            setIsLoading(true);
            await new Promise(resolve => setTimeout(resolve, 4000));
            let acctID = sessionStorage.getItem('accountID');
            try{
                if(accountID !== 0){
                    setErrorMessage('');
                }
                console.log('AccountID before pending call: ', accountID);
                    const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/pending/${accountID}`);
                    if(response.data.length > 0){
                        console.log('Pending Transactions: ', response.data);
                        setPendingTransactions(response.data);
                    }else{
                        console.log('No Pending Transactions found');
                    }

                }catch(error){
                    console.error('Error fetching pending transactions: ', error);
                    setErrorMessage('Unable to fetch pending transactions. Please try again later.');
                    setIsLoading(false);
                    return;
                }

                try{
                    const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/pending/size/${accountID}`);
                    if(response.data && response.data.pendingAmount){
                        console.log('Pending Size: ', response.data.pendingAmount);
                        setTotalPending(Number(response.data.pendingAmount));
                    }

                }catch(error){
                    console.error('Unable to retrieve a count of pending transactions: ', error);

                }finally {
                    setIsLoading(false);
                }
        };

        fetchPendingTransactions();

    }, [accountID]);

    if(Number(totalPending) === 0) return null;


    return (
        <Accordion expanded={expanded === 'panel1'} onChange={handleChange('panel1')}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1bh-content"
                id="panel1bh-header"
            >
                {
                    Number(totalPending) > 0 && (
                        <Chip
                            avatar={
                                <Avatar sx={{ fontSize: '2.3rem', fontWeight: 'bold' }}>
                                    {totalPending}
                                </Avatar>
                            }
                            label={<Typography sx={{ fontWeight: 'bold' }}>{`Pending Transactions`}</Typography>}
                            color="info"
                        />
                )}
            </AccordionSummary>
            <AccordionDetails>
                <TableContainer component={Paper}>
                        <Table sx={{ minWidth: 650 }} aria-label="pending transactions table">
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
                                    // Render Skeletons when data is loading
                                    Array.from(new Array(5)).map((_, index) => (
                                        <TableRow key={index}>
                                            <TableCell>
                                                <Skeleton animation="wave" height={30} width="100%" />
                                            </TableCell>
                                            <TableCell align="right">
                                                <Skeleton animation="wave" height={30} width="60%" />
                                            </TableCell>
                                            <TableCell align="right">
                                                <Skeleton animation="wave" height={30} width="60%" />
                                            </TableCell>
                                            <TableCell align="right">
                                                <Skeleton animation="wave" height={30} width="40%" />
                                            </TableCell>
                                        </TableRow>
                                    ))
                                ) : errorMessage ? (
                                    // Render Alert in a TableRow if there's an error message
                                    <TableRow>
                                        <TableCell colSpan={4}>
                                            <Alert severity="error" sx={{ width: '99%' }}>
                                                {errorMessage}
                                            </Alert>
                                        </TableCell>
                                    </TableRow>
                                ) : (
                                    // Render actual data when it's not loading and no error message
                                    pendingTransactions.map((row, index) => (
                                        <TableRow key={index}>
                                            <TableCell component="th" scope="row">
                                                {row.description}
                                            </TableCell>
                                            <TableCell align="right" sx={{color: row.debit ? 'red' : 'inherit'}}>
                                                {row.debit}
                                            </TableCell>
                                            <TableCell align="right" sx={{color: row.credit ? 'green' : 'inherit'}}>
                                                {row.credit}
                                            </TableCell>
                                            <TableCell align="right">
                                                {row.status === 'pending' ? <em>{row.status}</em> : row.balance}
                                            </TableCell>
                                        </TableRow>
                                    ))
                                )}
                            </TableBody>
                        </Table>
                </TableContainer>
            </AccordionDetails>
        </Accordion>
    );
}