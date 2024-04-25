import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Button,
    Card, Checkbox, FormControl, FormControlLabel,
    Grid, IconButton, InputLabel, MenuItem, Paper, Select, Table, TableBody,
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
import TransactionSummaryTable from "./TransactionSummaryTable";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import TransactionSummaryStats from "./TransactionSummaryStats";
import {InfoOutlined, ViewComfy} from "@mui/icons-material";
import FilterListIcon from "@mui/icons-material/FilterList";
import GridOnIcon from '@mui/icons-material/GridOn';
import ViewComfyIcon from '@mui/icons-material/ViewComfy';

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

    const transactionStats = {
        lastTransaction: 'April 20, 2024, $500 to Account 123456',
        transactionCount: 320,
        totalAmountTransferred: '$15,000',
        averageTransaction: '$150'
    };

    const handleIconClick = (event) => {

    }

    const handleGridIconClick = (event) => {

    }

    const handleToggleIconClick = (event) => {

    }


    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <div style={{
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                minHeight: '100vh',
                padding: '20px'
            }}>
                <Typography variant="h4" align="center" gutterBottom>Transaction Summary</Typography>
                <Grid container spacing={2} justifyContent="center">
                    <Grid item xs={12} md={10}>
                        <Accordion>
                            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                                <Typography variant="h6" fontFamily="Montserrat" fontWeight="bold">Transaction Statistics</Typography>
                                <IconButton onClick={handleIconClick} style={{fontSize: 36}}>
                                    <FilterListIcon />
                                </IconButton>
                                <IconButton onClick={handleGridIconClick}>
                                    <GridOnIcon />
                                </IconButton>
                                <IconButton onClick={handleToggleIconClick}>
                                    <ViewComfyIcon/>
                                </IconButton>
                            </AccordionSummary>
                            <AccordionDetails>
                                <Grid container spacing={2} justifyContent="center">
                                    {/* Row 1 */}
                                    <Grid item xs={4}>
                                        <TransactionSummaryStats title="Last Transaction Submitted" value={transactionStats.lastTransaction} />
                                    </Grid>
                                    <Grid item xs={4}>
                                        <TransactionSummaryStats title="Transaction Count" value={transactionStats.transactionCount} />
                                    </Grid>
                                    <Grid item xs={4}>
                                        <TransactionSummaryStats title="Total Amount Transferred" value={transactionStats.totalAmountTransferred} />
                                    </Grid>
                                    {/* Row 2 */}
                                    <Grid item xs={4}>
                                        <TransactionSummaryStats title="Average Transaction Value" value={transactionStats.averageTransaction} />
                                    </Grid>
                                    <Grid item xs={4}>
                                        {/* You can add more stats here */}
                                        <TransactionSummaryStats title="Average Transactions by Month" value="140" />
                                    </Grid>
                                    <Grid item xs={4}>
                                        {/* Additional stat */}
                                        <TransactionSummaryStats title="Total Weekly Transactions" value="50" />
                                    </Grid>
                                    {/* Row 3 */}
                                    <Grid item xs={4}>
                                        {/* Additional stat */}
                                        <TransactionSummaryStats title="Currently Pending" value="3" />
                                    </Grid>
                                    <Grid item xs={4}>
                                        {/* Placeholder or another stat */}
                                        <TransactionSummaryStats title="Failed Transactions" value="0" />
                                    </Grid>
                                    <Grid item xs={4}>
                                        {/* Placeholder or another stat */}
                                        <TransactionSummaryStats title="Total Transfers by month" value="15" />
                                    </Grid>
                                </Grid>
                            </AccordionDetails>
                        </Accordion>
                    </Grid>
                    <Grid item xs={12} md={10}>
                        <TransactionSummaryTable />
                    </Grid>
                </Grid>
            </div>
        </LocalizationProvider>
    );

}

export default TransactionSummary;

