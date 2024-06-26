import {Box} from "@mui/system";
import {
    Button,
    Divider,
    Grid,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import MenuAppBar from "./MenuAppBar";
import {ArrowLeft} from "@mui/icons-material";
import {useNavigate} from "react-router-dom";
import backgroundImage from "./images/pexels-pixabay-210307.jpg";

function PendingPaymentsPage()
{
    const navigate = useNavigate();

    const handleHistoryPage = () => {
        navigate('/billPay/Payments/PaymentHistory');
    }

    return(
        <div
            style={{
                background: `url(${backgroundImage}) no-repeat center bottom`,
                backgroundSize: 'cover',
                minHeight: 'calc(120vh - 64px)',
                width: '100%',
                position: 'relative',
            }}>
            <MenuAppBar />
            <Box sx={{flexGrow: 1, p: 3 }}>
                <Typography variant="h4" gutterBottom>
                    Pending Payments
                </Typography>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={8}>
                        <Paper sx={{p: 2, mb: 2}}>
                            <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2}}>
                                <Typography variant="h6">Pending</Typography>
                                <Divider orientation="horizontal" />
                            </Box>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Pay to</TableCell>
                                            <TableCell>Pay from</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Process Date</TableCell>
                                            <TableCell>Deliver Date</TableCell>
                                            <TableCell>Additional Items</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>Utility Company</TableCell>
                                            <TableCell>Checking Account</TableCell>
                                            <TableCell>$75.00</TableCell>
                                            <TableCell>June 26, 2024</TableCell>
                                            <TableCell>June 29, 2024</TableCell>
                                            <TableCell>-</TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 3}}>
                                <Typography variant="h6">Payment Total: $0.00</Typography>
                            </Box>
                            <Divider />
                            <Box sx={{display: 'flex', justifyContent: 'flex-start', mt: 3}}>
                                <Button variant="contained" startIcon={<ArrowLeft />} onClick={handleHistoryPage}>
                                    Return to History
                                </Button>
                            </Box>
                        </Paper>
                    </Grid>
                </Grid>
            </Box>
        </div>
    );

}

export default PendingPaymentsPage;