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
import MenuAppBar from "./MenuAppBar";
import {ClockIcon} from "@mui/x-date-pickers";
import backgroundImg from "./images/pexels-pixabay-210307.jpg";

function BillPaymentHistoryPage()
{
    const navigate = useNavigate();

    const userID = sessionStorage.getItem('userID');

    const handleReturnToPayments = () => {
        navigate(`/billPay/Payments/userID=${userID}`);
    }

    const handlePendingPayments = () => {
        navigate('/billPay/Payments/Pending');
    }

    return (
        <div
            style={{
                background: `url(${backgroundImg}) no-repeat center bottom`,
                backgroundSize: 'cover',
                minHeight: 'calc(120vh - 64px)',
                width: '100%',
                position: 'relative',
            }}>
            <MenuAppBar/>
            <Container maxWidth="lg" sx={{ py: 4}}>
                <Paper elevation={3} sx={{ p: 3 }}>
                    <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3}}>
                        <Typography variant="h4">Payment History</Typography>
                        <Box sx={{display: 'flex', alignItems: 'center', gap: 2}}>
                            <TextField size="small" placeholder="Search"/>
                            <Button variant="contained" color="primary">
                                Filter
                            </Button>
                        </Box>
                    </Box>

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

                    <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 3}}>
                        <Button variant="outlined" color="primary" startIcon={<ClockIcon />} onClick={handlePendingPayments}>
                            View Pending
                        </Button>
                        <Button variant="outlined" color="primary" sx={{ml: 2}} onClick={handleReturnToPayments}>
                            Return to Payments
                        </Button>
                    </Box>
                </Paper>
            </Container>
        </div>

    );
}

export default BillPaymentHistoryPage;