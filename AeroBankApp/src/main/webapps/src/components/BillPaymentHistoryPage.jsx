import {Box, Container} from "@mui/system";
import {
    Button,
    Paper, Tab,
    Table, TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow, Tabs,
    TextField,
    Typography
} from "@mui/material";
import {useLocation, useNavigate} from "react-router-dom";
import {useEffect, useState} from "react";
import PayeesContentPage from "./PayeesContentPage";

function BillPaymentHistoryPage()
{
    const navigate = useNavigate();

    return (
        <Container maxWidth="lg" sx={{ py: 4}}>
            <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
                <Typography variant="h4">Payment History</Typography>
                <Box sx={{display: 'flex', alignItems: 'center', gap: 2}}>
                    <TextField size="small" placeholder="Search"/>
                    <Button variant="contained" color="primary">
                        Filter
                    </Button>
                </Box>
            </Box>


            <Paper elevation={3}>
                <TableContainer>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Pay To</TableCell>
                                <TableCell>Pay From</TableCell>
                                <TableCell>Amount</TableCell>
                                <TableCell>Process Date</TableCell>
                                <TableCell>Deliver By</TableCell>
                                <TableCell>Additional Items</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            <TableRow>
                                <TableCell>Utility Company</TableCell>
                                <TableCell>Checking Account</TableCell>
                                <TableCell>$50.00</TableCell>
                                <TableCell>June 15, 2024</TableCell>
                                <TableCell>June 20, 2024</TableCell>
                                <TableCell>-</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableCell>Rent</TableCell>
                                <TableCell>Savings Account</TableCell>
                                <TableCell>$1200</TableCell>
                                <TableCell>June 1, 2024</TableCell>
                                <TableCell>June 5, 2024</TableCell>
                                <TableCell>-</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </TableContainer>
            </Paper>

            <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 3}}>
                <Button variant="outlined" color="primary">
                    View Pending
                </Button>
                <Button variant="outlined" color="primary" sx={{ml: 2}}>
                    Return to Payments
                </Button>
            </Box>
        </Container>
    );
}

export default BillPaymentHistoryPage;