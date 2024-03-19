import {useEffect, useState} from "react";
import axios from "axios";
import {Paper, TableCell, TableRow, Typography} from "@mui/material";
import {TableVirtuoso} from "react-virtuoso";
import React from "react";

 export default function StatementTable(){
    const [transactionStatements, setTransactionStatements] = useState([]);
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
                     sessionStorage.setItem('accountID', acctID);
                 } catch (error) {
                     console.error('Error fetching a random acctID:', error);
                     setIsLoading(false);
                     return;
                 }
             }

             try {
                 const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionStatements/${1}`);
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

     // Prepare data for rendering
     const transactionsGroupedByDate = transactionStatements.reduce((acc, statement) => {
         const date = statement.transactionDate;
         if (!acc[date]) {
             acc[date] = [];
         }
         acc[date].push(statement);
         return acc;
     }, {});

     const flattenedData = Object.entries(transactionsGroupedByDate).flatMap(([date, statements]) => [
         { isDateRow: true, date },
         ...statements.map(statement => ({ ...statement, date }))
     ]);

     const rowRenderer = (index, item) => {
         if (item.isDateRow) {
             return (
                 <TableRow>
                     <TableCell colSpan={4} sx={{ backgroundColor: '#0E0F52', color: 'white' }}>
                         <Typography variant="subtitle1">{item.date}</Typography>
                     </TableCell>
                 </TableRow>
             );
         } else {
             return (
                 <TableRow key={`${item.date}-${index}`}>
                     <TableCell>{item.description}</TableCell>
                     <TableCell align="right" sx={{ color: item.debit ? 'red' : 'inherit' }}>{item.debit}</TableCell>
                     <TableCell align="right" sx={{ color: item.credit ? 'green' : 'inherit' }}>{item.credit}</TableCell>
                     <TableCell align="right">{item.balance}</TableCell>
                 </TableRow>
             );
         }
     };

     if (isLoading) {
         return <Typography>Loading...</Typography>;
     }

     return (
         <Paper sx={{ height: 400, width: '100%', overflow: 'hidden' }}>
             <TableVirtuoso
                 data={flattenedData}
                 itemContent={(index, item) => rowRenderer(index, item)}
                 style={{ height: '100%' }}
                 components={{
                     Table: React.forwardRef((props, ref) => (
                         <table {...props} ref={ref} style={{ width: '100%' }} />
                     )),
                     TableRow: React.forwardRef(({ item, ...props }, ref) => (
                         <TableRow {...props} ref={ref} />
                     )),
                     TableCell: React.forwardRef((props, ref) => (
                         <TableCell {...props} ref={ref} />
                     )),
                 }}
             />
         </Paper>
     );
}