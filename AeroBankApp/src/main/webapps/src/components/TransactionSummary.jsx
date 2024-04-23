import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Button,
    Card, Checkbox, FormControl, FormControlLabel,
    Grid, InputLabel, MenuItem, Select, Table, TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField,
    Typography
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {DatePicker, LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import {useState} from "react";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";

function TransactionSummary(){
    const [filters, setFilters] = useState({
        useDates: false,
        startDate: null,
        endDate: null,
        useTransactionType: false,
        transactionType: '',
        useStatus: false,
        status: '',
        useScheduledDate: false,
        scheduledDate: null,
        useScheduledTime: false,
        scheduledTime: null,
        useUsername: false,
        username: '',
        useAccountID: false,
        accountID: '',
        useDescription: false,
        description: '',
        useAmountRange: false,
        minAmount: '',
        maxAmount: ''
    });

    const [pendingTransactions, setPendingTransactions] = useState([]);
    const [pastTransactions, setPastTransactions] = useState([]);

    const handleCheckboxChange = (event) => {
        const { name, checked } = event.target;
        setFilters(prevFilters => ({
            ...prevFilters,
            [name]: checked
        }));
    };

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setFilters({
            ...filters,
            [name]: value
        });
    };


    // Dummy function to load transactions based on filters (to be implemented)
    const loadTransactions = () => {
        // Implementation to load transactions based on current filters
    };

    const handleChange = (event) => {
        setFilters({ ...filters, [event.target.name]: event.target.value });
    };

    const handleDateChange = (name, value) => {
        setFilters({ ...filters, [name]: value });
    };

    const toggleFilters = () => {
        setFilters(prev => ({ ...prev, showFilters: !prev.showFilters }));
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <div>
                <Typography variant="h4" align="center" gutterBottom>Transaction Summary</Typography>
                <Grid container spacing={2} justifyContent="center">
                    <Grid item xs={12} md={10}>
                        <Accordion expanded={filters.showFilters} onChange={toggleFilters}>
                            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                <Typography>Toggle Filters</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                {/* Place your filter components here */}
                                <Grid container spacing={2} justifyContent="center">
                                    <Grid item xs={12} md={8}>
                                        <Accordion expanded={filters.showFilters} onChange={toggleFilters}>
                                            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                                <Typography>Toggle Filters</Typography>
                                            </AccordionSummary>
                                            <AccordionDetails>
                                                <Grid container spacing={2}>
                                                    {/* Date Range Filters */}
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useStartDate} onChange={handleCheckboxChange} name="useStartDate" />}
                                                            label="Start Date"
                                                        />
                                                        {filters.useStartDate && (
                                                            <DatePicker
                                                                label="Start Date"
                                                                value={filters.startDate}
                                                                onChange={(newValue) => handleDateChange('startDate', newValue)}
                                                                renderInput={(params) => <TextField fullWidth {...params} />}
                                                            />
                                                        )}
                                                    </Grid>
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useEndDate} onChange={handleCheckboxChange} name="useEndDate" />}
                                                            label="End Date"
                                                        />
                                                        {filters.useEndDate && (
                                                            <DatePicker
                                                                label="End Date"
                                                                value={filters.endDate}
                                                                onChange={(newValue) => handleDateChange('endDate', newValue)}
                                                                renderInput={(params) => <TextField fullWidth {...params} />}
                                                            />
                                                        )}
                                                    </Grid>
                                                    {/* Transaction Type and Status Filters */}
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControl fullWidth>
                                                            <InputLabel>Transaction Type</InputLabel>
                                                            <Select
                                                                value={filters.transactionType}
                                                                onChange={handleInputChange}
                                                                name="transactionType"
                                                            >
                                                                <MenuItem value="withdraw">Withdraw</MenuItem>
                                                                <MenuItem value="transfer">Transfer</MenuItem>
                                                                <MenuItem value="deposit">Deposit</MenuItem>
                                                            </Select>
                                                        </FormControl>
                                                    </Grid>
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControl fullWidth>
                                                            <InputLabel>Status</InputLabel>
                                                            <Select
                                                                value={filters.status}
                                                                onChange={handleInputChange}
                                                                name="status"
                                                            >
                                                                <MenuItem value="pending">Pending</MenuItem>
                                                                <MenuItem value="completed">Completed</MenuItem>
                                                                <MenuItem value="active">Active</MenuItem>
                                                            </Select>
                                                        </FormControl>
                                                    </Grid>
                                                    {/* Scheduled Date and Time Filters */}
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useScheduledDate} onChange={handleCheckboxChange} name="useScheduledDate" />}
                                                            label="Scheduled Date"
                                                        />
                                                        {filters.useScheduledDate && (
                                                            <DatePicker
                                                                label="Scheduled Date"
                                                                value={filters.scheduledDate}
                                                                onChange={(newValue) => handleDateChange('scheduledDate', newValue)}
                                                                renderInput={(params) => <TextField fullWidth {...params} />}
                                                            />
                                                        )}
                                                    </Grid>
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useScheduledTime} onChange={handleCheckboxChange} name="useScheduledTime" />}
                                                            label="Scheduled Time"
                                                        />
                                                        {filters.useScheduledTime && (
                                                            <TimePicker
                                                                label="Scheduled Time"
                                                                value={filters.scheduledTime}
                                                                onChange={(newValue) => handleDateChange('scheduledTime', newValue)}
                                                                renderInput={(params) => <TextField fullWidth {...params} />}
                                                            />
                                                        )}
                                                    </Grid>
                                                    {/* Username, Account ID, Description Filters */}
                                                    <Grid item xs={12} sm={4}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useUsername} onChange={handleCheckboxChange} name="useUsername" />}
                                                            label="Username"
                                                        />
                                                        {filters.useUsername && (
                                                            <TextField
                                                                fullWidth
                                                                label="Username"
                                                                name="username"
                                                                value={filters.username}
                                                                onChange={handleInputChange}
                                                            />
                                                        )}
                                                    </Grid>
                                                    <Grid item xs={12} sm={4}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useAccountID} onChange={handleCheckboxChange} name="useAccountID" />}
                                                            label="Account ID"
                                                        />
                                                        {filters.useAccountID && (
                                                            <TextField
                                                                fullWidth
                                                                label="Account ID"
                                                                name="accountID"
                                                                value={filters.accountID}
                                                                onChange={handleInputChange}
                                                            />
                                                        )}
                                                    </Grid>
                                                    <Grid item xs={12} sm={4}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useDescription} onChange={handleCheckboxChange} name="useDescription" />}
                                                            label="Description"
                                                        />
                                                        {filters.useDescription && (
                                                            <TextField
                                                                fullWidth
                                                                label="Description"
                                                                name="description"
                                                                value={filters.description}
                                                                onChange={handleInputChange}
                                                            />
                                                        )}
                                                    </Grid>
                                                    {/* Amount Range Filters */}
                                                    <Grid item xs={12} sm={6}>
                                                        <FormControlLabel
                                                            control={<Checkbox checked={filters.useAmountRange} onChange={handleCheckboxChange} name="useAmountRange" />}
                                                            label="Amount Range"
                                                        />
                                                        {filters.useAmountRange && (
                                                            <>
                                                                <TextField
                                                                    fullWidth
                                                                    label="Minimum Amount"
                                                                    name="minAmount"
                                                                    type="number"
                                                                    value={filters.minAmount}
                                                                    onChange={handleInputChange}
                                                                />
                                                                <TextField
                                                                    fullWidth
                                                                    label="Maximum Amount"
                                                                    name="maxAmount"
                                                                    type="number"
                                                                    value={filters.maxAmount}
                                                                    onChange={handleInputChange}
                                                                />
                                                            </>
                                                        )}
                                                    </Grid>
                                                    {/* Apply Filters Button */}
                                                    <Grid item xs={12}>
                                                        <Button variant="contained" color="primary">
                                                            Apply Filters
                                                        </Button>
                                                    </Grid>
                                                </Grid>
                                            </AccordionDetails>
                                        </Accordion>
                                    </Grid>
                                </Grid>
                            </AccordionDetails>
                        </Accordion>
                    </Grid>
                    {/* Accordion for Pending Transactions */}
                    <Grid item xs={12} md={10}>
                        <Accordion>
                            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                <Typography>Pending Transactions</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Date</TableCell>
                                            <TableCell>Type</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Status</TableCell>
                                            <TableCell>Description</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {filters.pendingTransactions && filters.pendingTransactions.length > 0 &&
                                            filters.pendingTransactions.map((transaction, index) => (
                                                <TableRow key={index}>
                                                    <TableCell>{transaction.date}</TableCell>
                                                    <TableCell>{transaction.type}</TableCell>
                                                    <TableCell>{transaction.amount}</TableCell>
                                                    <TableCell>{transaction.status}</TableCell>
                                                    <TableCell>{transaction.description}</TableCell>
                                                </TableRow>
                                            ))
                                        }
                                    </TableBody>
                                </Table>
                            </AccordionDetails>
                        </Accordion>
                    </Grid>
                    {/* Accordion for Past Transactions */}
                    <Grid item xs={12} md={10}>
                        <Accordion>
                            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                <Typography>Past Transactions</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>Date</TableCell>
                                            <TableCell>Type</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Status</TableCell>
                                            <TableCell>Description</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        {filters.pastTransactions && filters.pastTransactions.length > 0 &&
                                            filters.pastTransactions.map((transaction, index) => (
                                                <TableRow key={index}>
                                                    <TableCell>{transaction.date}</TableCell>
                                                    <TableCell>{transaction.type}</TableCell>
                                                    <TableCell>{transaction.amount}</TableCell>
                                                    <TableCell>{transaction.status}</TableCell>
                                                    <TableCell>{transaction.description}</TableCell>
                                                </TableRow>
                                            ))
                                        }
                                    </TableBody>
                                </Table>
                            </AccordionDetails>
                        </Accordion>
                    </Grid>
                </Grid>
            </div>
        </LocalizationProvider>
    );

}

export default TransactionSummary;

