import {
    Button, Checkbox,
    FormControl,
    FormControlLabel, FormGroup,
    InputLabel,
    MenuItem,
    Select,
    TextField,
    Typography
} from "@mui/material";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {Box} from "@mui/system";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {useState} from "react";

function TransactionSummaryFilters({ applyFilters }){
    const [filters, setFilters] = useState({
        description: '',
        startDate: null,
        endDate: null,
        transactionType: '',
        status: '',
        scheduledTime: '',
        username: '',
        accountID: '',
        minAmount: '',
        maxAmount: '',
        enabledFilters: {
            description: false,
            dates: false,
            transactionType: false,
            status: false,
            scheduledTime: false,
            username: false,
            accountID: false,
            amountRange: false
        }
    });

    const handleInputChange = (event) => {
        const { name, value, checked, type } = event.target;
        if (type === 'checkbox') {
            setFilters(prev => ({
                ...prev,
                enabledFilters: {
                    ...prev.enabledFilters,
                    [name]: checked
                }
            }));
        } else {
            setFilters(prev => ({ ...prev, [name]: value }));
        }
    };

    const handleDateChange = (field, value) => {
        setFilters(prev => ({ ...prev, [field]: value }));
    };

    const handleSubmit = () => {
        applyFilters(filters);
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <Box sx={{ p: 2, bgcolor: 'background.paper', boxShadow: 24 }}>
                <FormGroup>
                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.description} onChange={handleInputChange} name="description" />}
                        label="Description"
                    />
                    {filters.enabledFilters.description && (
                        <TextField
                            fullWidth
                            label="Description"
                            name="description"
                            multiline
                            rows={1}
                            value={filters.description}
                            onChange={handleInputChange}
                        />
                    )}

                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.dates} onChange={handleInputChange} name="dates" />}
                        label="Start and End Date"
                    />
                    {filters.enabledFilters.dates && (
                        <>
                            <DatePicker
                                label="Start Date"
                                value={filters.startDate}
                                onChange={(newValue) => handleDateChange('startDate', newValue)}
                                renderInput={(params) => <TextField {...params} fullWidth />}
                            />
                            <DatePicker
                                label="End Date"
                                value={filters.endDate}
                                onChange={(newValue) => handleDateChange('endDate', newValue)}
                                renderInput={(params) => <TextField {...params} fullWidth />}
                            />
                        </>
                    )}

                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.transactionType} onChange={handleInputChange} name="transactionType" />}
                        label="Transaction Type"
                    />
                    {filters.enabledFilters.transactionType && (
                        <FormControl fullWidth>
                            <InputLabel>Transaction Type</InputLabel>
                            <Select
                                value={filters.transactionType}
                                onChange={handleInputChange}
                                name="transactionType"
                            >
                                <MenuItem value="withdraw">Withdraw</MenuItem>
                                <MenuItem value="deposit">Deposit</MenuItem>
                                <MenuItem value="transfer">Transfer</MenuItem>
                            </Select>
                        </FormControl>
                    )}

                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.status} onChange={handleInputChange} name="status" />}
                        label="Status"
                    />
                    {filters.enabledFilters.status && (
                        <FormControl fullWidth>
                            <InputLabel>Status</InputLabel>
                            <Select
                                value={filters.status}
                                onChange={handleInputChange}
                                name="status"
                            >
                                <MenuItem value="pending">Pending</MenuItem>
                                <MenuItem value="completed">Completed</MenuItem>
                                <MenuItem value="cancelled">Cancelled</MenuItem>
                            </Select>
                        </FormControl>
                    )}

                    {/* Include checkboxes and fields for username, accountID, and amount range */}
                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.username} onChange={handleInputChange} name="username" />}
                        label="Username"
                    />
                    {filters.enabledFilters.username && (
                        <TextField
                            fullWidth
                            label="Username"
                            name="username"
                            multiline
                            rows={1}
                            value={filters.username}
                            onChange={handleInputChange}
                        />
                    )}

                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.accountID} onChange={handleInputChange} name="accountID" />}
                        label="Account ID"
                    />
                    {filters.enabledFilters.accountID && (
                        <TextField
                            fullWidth
                            label="Account ID"
                            name="accountID"
                            multiline
                            rows={1}
                            value={filters.accountID}
                            onChange={handleInputChange}
                        />
                    )}

                    <FormControlLabel
                        control={<Checkbox checked={filters.enabledFilters.amountRange} onChange={handleInputChange} name="amountRange" />}
                        label="Amount Range"
                    />
                    {filters.enabledFilters.amountRange && (
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

                </FormGroup>
                <Button variant="contained" color="primary" onClick={handleSubmit} sx={{ mt: 2 }}>
                    Apply Filters
                </Button>
            </Box>
        </LocalizationProvider>
    );
}

export default TransactionSummaryFilters;

