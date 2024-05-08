import React from 'react';
import { Container, Box, Grid, Paper, Typography, Button, TextField, Accordion, AccordionSummary, AccordionDetails, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, IconButton } from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';
import BillPaySchedule from "./BillPaySchedule";
import Home from "./Home";
import GradientSeparator from "./GradientSeparator";
import MenuAppBar from "./MenuAppBar";

export default function BillPayPage() {
    // Sample data for payees, this should be fetched from your back end or state management.
    const payees = [
        { name: 'STATE FARM INS AUTO LIFE FIRE HEALTH PAYMENTS ONLY', lastPaid: '78.96 on 05/13/2021', nextPaymentDue: '02/05/2024', amount: '0.00' },
        // ... other payee data
    ];

    return (
        <div>
            <MenuAppBar />
            <GradientSeparator />
            <Container maxWidth="lg">
                <Box my={4}>
                    <Grid container spacing={3}>
                        <Grid item xs={12} md={8}>
                            <BillPaySchedule />

                        </Grid>
                        <Grid item xs={12} md={4}>
                            {/* Pending and History sections can be Accordions */}
                            <Accordion>
                                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                    <Typography>Pending</Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    {/* ... Content for Pending section */}
                                </AccordionDetails>
                            </Accordion>
                            <Accordion>
                                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                    <Typography>History</Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    {/* ... Content for History section */}
                                </AccordionDetails>
                            </Accordion>
                        </Grid>
                    </Grid>
                </Box>
            </Container>
        </div>

    );
}