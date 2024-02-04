import React, { useState } from 'react';
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
    Paper
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';

function createData(description, debit, credit, balance, status) {
    return { description, debit, credit, balance, status };
}

const pendingTransactions = [
    createData('PURCHASE 933334 FIREHOUSE SUBS 1178 ECOSOUTH JORDAN UTUS', '-$10.24', '', '', 'pending'),
    // ... add more pending transactions here
];

export default function PendingTransactionsTable() {
    const [expanded, setExpanded] = useState(false);

    const handleChange = (panel) => (event, isExpanded) => {
        setExpanded(isExpanded ? panel : false);
    };

    return (
        <Accordion expanded={expanded === 'panel1'} onChange={handleChange('panel1')}>
            <AccordionSummary
                expandIcon={<ExpandMoreIcon />}
                aria-controls="panel1bh-content"
                id="panel1bh-header"
            >
                <Typography sx={{ flexShrink: 0 }}>
                    2 pending transactions
                </Typography>
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
                            {pendingTransactions.map((row, index) => (
                                <TableRow key={index}>
                                    <TableCell component="th" scope="row">
                                        {row.description}
                                    </TableCell>
                                    <TableCell align="right"sx={{color: row.debit ? 'red' : 'inherit'}}>{row.debit}</TableCell>
                                    <TableCell align="right">{row.credit}</TableCell>
                                    <TableCell align="right">
                                        {row.status === 'pending' ? <em>{row.status}</em> : row.balance}
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            </AccordionDetails>
        </Accordion>
    );
}