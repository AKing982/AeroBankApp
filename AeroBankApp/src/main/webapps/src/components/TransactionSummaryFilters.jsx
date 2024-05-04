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
import axios from "axios";

const transactionFilterType = {
    description: 'DESCRIPTION',
    dates: 'DATES',
    transactionType: 'TRANSACTION_TYPE',
    status: 'STATUS',
    scheduledTime: 'SCHEDULED_TIME',
    username: 'USERNAME',
    accountID: 'ACCOUNT_ID',
    amountRange: 'AMOUNT_RANGE'
};

function TransactionSummaryFilters({ applyFilters}){
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
        enabledFilters: Object.keys(transactionFilterType).reduce((acc, key)=> {
            acc[transactionFilterType[key]] = false;
            return acc;
        }, {})
        // enabledFilters: {
        //     description: false,
        //     dates: false,
        //     transactionType: false,
        //     status: false,
        //     scheduledTime: false,
        //     username: false,
        //     accountID: false,
        //     amountRange: false
        // }
    });

    const [loading, setIsLoading] = useState(false);
    const [error, setError] = useState('');

    const transactionTypes = [
        { value: 'DEPOSIT', label: 'Deposit' },
        { value: 'WITHDRAW', label: 'Withdraw' },
        { value: 'PURCHASE', label: 'Purchase' },
        { value: 'TRANSFER', label: 'Transfer' }
    ];

    const transactionStatusTypes = [
        {value: 'PENDING', label: 'Pending'},
        {value: 'COMPLETED', label: 'Completed'},
        {value: 'FAILED', label: 'Failed'},
        {value: 'CANCELLED', label: 'Cancelled'}
    ];

    const buildDescriptionAndStartDateRequest = (description, startDate) => {

    };

    const buildDescriptionAndStatusRequest = (description, status) => {

    };

    const buildDescriptionAndUserNameRequest = (description, username) => {

    };




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

    const enableAllFilters = () => {
        setFilters(prev => ({
            ...prev,
            enabledFilters: Object.keys(prev.enabledFilters).reduce((acc, key) => {
                acc[key] = true;
                return acc;
            }, {})
        }));
    };

    const disableAllFilters = () => {
        setFilters(prev => ({
            ...prev,
            enabledFilters: Object.keys(prev.enabledFilters).reduce((acc, key) => {
                acc[key] = false;
                return acc;
            }, {})
        }));
    };

    const handleDateChange = (field, value) => {
        setFilters(prev => ({ ...prev, [field]: value }));
    };

    const handleSubmit = () => {

        const enabledTypes = Object.entries(filters.enabledFilters).reduce((acc, [key, enabled]) => {
            if (enabled) {
                acc.push(transactionFilterType[key]); // Ensure this maps to string values correctly
            }
            return acc;
        }, []);

        const request = buildRequest(enabledTypes);
        sendFilterRequestToServer(request);
       // applyFilters(filters);
    };

    const buildRequest = (enabledTypes) => {
        console.log("Enabled Types:", enabledTypes); // Debug: Check the content of enabledTypes

        return {
            filterType: enabledTypes,
            description: filters.description || null,
            startDate: filters.startDate ? filters.startDate.toISOString().split('T')[0] : null,
            endDate: filters.endDate ? filters.endDate.toISOString().split('T')[0] : null,
            transactionType: filters.transactionType || null, // Set null if empty
            status: filters.status || null,
            scheduledTime: filters.scheduledTime ? filters.scheduledTime.toISOString() : null,
            username: filters.username || null,
            accountID: filters.accountID || null,
            minAmount: filters.minAmount || null,
            maxAmount: filters.maxAmount || null
        };
    }

    const buildFilterRequest = () => {
        return {
            description: filters.description,
            minAmount: filters.minAmount,
            maxAmount: filters.maxAmount,
            transactionType: filters.transactionType,
            status: filters.status,
            accountID: filters.accountID,
            username: filters.username,
            startDate: filters.startDate,
            endDate: filters.endDate,
            scheduledTime: filters.scheduledTime
        };
    };


    const sendFilterRequestToServer = async(request) => {

        console.log('Request: ', request);
        try{
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/transactionHistory/save`, request, {
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            if(response.status === 200 || response.status === 201){
                console.log('Response from server was successful');
                console.log('Filters: ', filters);
                console.log('Filter Response: ', response.data);
                applyFilters(response.data);
            }else{
                console.error('Failed to save filters, response status: ', response.status);
                setError('Failed to save filters.');
            }

        }catch(error){
            console.error('Error occurred while saving filters:', error.message); // Error: log error message
            if (error.response) {
                console.error('Error details:', {
                    data: error.response.data,
                    status: error.response.status,
                    headers: error.response.headers
                }); // Error: log detailed error response from server
            }
            setError('An error occurred while saving filters.');
        }finally{
            console.log('Filter request processing complete.');
            setIsLoading(false);
        }
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
                                {transactionTypes.map((type) => (
                                    <MenuItem key={type.value} value={type.value}>
                                        {type.label}
                                    </MenuItem>
                                ))}
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
                                {transactionStatusTypes.map((type) => (
                                    <MenuItem key={type.value} value={type.value}>
                                        {type.label}
                                    </MenuItem>
                                ))}
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

