import React from 'react';
import {
    Container,
    Box,
    Grid,
    Paper,
    Typography,
    Button,
    TextField,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    Select, FormControlLabel, MenuItem, InputAdornment, Card, CardContent, Switch
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';
import BillPaySchedule from "./BillPaySchedule";
import Home from "./Home";
import GradientSeparator from "./GradientSeparator";
import MenuAppBar from "./MenuAppBar";
import BillScheduler from "./BillScheduler";
import PaymentGraph from "./PaymentGraph";
import InfoIcon from "@mui/icons-material/Info";

export default function BillPayPage() {
    // Sample data for payees, this should be fetched from your back end or state management.

    const payees = [
        { name: 'STATE FARM INS AUTO LIFE FIRE HEALTH PAYMENTS ONLY', lastPaid: '78.96 on 05/13/2021', nextPaymentDue: '02/05/2024', amount: '0.00' },
        // other payee data
    ];

    return (
        <div>
            <MenuAppBar />
            <GradientSeparator />
            <Container maxWidth="lg">
                <Box my={4}>
                    <Grid container spacing={3}>
                        <Grid item xs={12} md={8}>
                            <Card raised>
                                <CardContent>
                                    <Typography variant="h6" gutterBottom>
                                        Payment Schedule
                                    </Typography>
                                    <IconButton aria-label="info" size="small" sx={{ float: 'right' }}>
                                        <InfoIcon fontSize="inherit" />
                                    </IconButton>
                                    <Typography variant="body2" gutterBottom>
                                        Our goal is to deliver your payment securely and quickly.
                                    </Typography>
                                    <Typography variant="body2" color="textSecondary">
                                        Payments may be processed using a single-use card.
                                    </Typography>
                                    <Button
                                        variant="contained"
                                        color="primary"
                                        startIcon={<AddIcon />}
                                        sx={{ marginTop: 2 }}
                                    >
                                        Add Payee
                                    </Button>
                                    <TextField
                                        fullWidth
                                        margin="normal"
                                        id="search-payee"
                                        label="Payee name or nickname"
                                        InputProps={{
                                            endAdornment: (
                                                <InputAdornment position="end">
                                                    <IconButton>
                                                        <SearchIcon />
                                                    </IconButton>
                                                </InputAdornment>
                                            ),
                                        }}
                                    />
                                    <FormControlLabel
                                        control={<Switch name="recurring" />}
                                        label="Make it recurring"
                                    />
                                    <Select
                                        labelId="select-account-label"
                                        id="select-account"
                                        value="Primary Acco..2242"
                                        label="Pay from"
                                        fullWidth
                                        margin="normal"
                                    >
                                        <MenuItem value="Primary Acco..2242">Primary Account 2242</MenuItem>
                                        {/* More accounts */}
                                    </Select>
                                    <div>
                                        <Button variant="outlined" sx={{ marginTop: 2 }}>
                                            View pending transactions
                                        </Button>
                                        <Button variant="outlined" sx={{ marginTop: 2 }}>
                                            View history
                                        </Button>
                                    </div>
                                    <div>
                                        <Button variant="contained" color="primary" sx={{ marginTop: 2 }}>
                                            Review All
                                        </Button>
                                        <Button variant="contained" color="secondary" sx={{ marginTop: 2 }}>
                                            Pay All
                                        </Button>
                                    </div>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={12} md={4}>
                            <BillScheduler payees={payees} />
                            <PaymentGraph data={payees} />
                        </Grid>
                    </Grid>
                </Box>
            </Container>
        </div>
    );

}