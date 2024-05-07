import {Accordion, AccordionDetails, AccordionSummary, Grid, IconButton, Typography} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {LocalizationProvider} from "@mui/x-date-pickers";
import {useEffect, useState} from "react";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import TransactionSummaryTable from "./TransactionSummaryTable";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import TransactionSummaryStats from "./TransactionSummaryStats";
import FilterListIcon from "@mui/icons-material/FilterList";
import GridOnIcon from '@mui/icons-material/GridOn';
import ViewComfyIcon from '@mui/icons-material/ViewComfy';
import axios from "axios";
import Home from "./Home";

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

    const [transactionStatistics, setTransactionStatistics] = useState({
        lastTransaction: '',
        totalTransactionCount: 0,
        totalTransferredAmount: 0,
        averageTransactionValue: 0,
        totalTransactionAmountByMonth: 0,
        totalTransactionsThisWeek: 0,
        totalWeeklyTransactions: 0,
        pendingTransactionCount: 0,
        totalMonthlyTransfers: 0,
    })

    const [totalMonthlyTransactions, setTotalMonthlyTransactions] = useState('');


    
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

    useEffect(() => {
        const fetchTransactionStatistics = async() => {
            let userID = sessionStorage.getItem('userID');
            try{
                const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionHistory/stats/${userID}`)
                if(response.status === 200 || response.status === 201){
                    setTransactionStatistics({
                        totalTransactionAmountByMonth: response.data.totalTransactionAmountByMonth,
                        pendingTransactionCount: response.data.pendingTransactionCount,
                        averageTransactionValue: response.data.averageTransactionValue,
                        totalTransferredAmount: response.data.totalTransferredAmount,
                        totalTransactionsThisWeek: response.data.totalTransactionAmountByWeek,
                        totalTransactionCount: response.data.totalTransactionCount
                    });
                }else{
                    console.error('Failed to fetch transaction statistics: ', response.status);
                }

            }catch(error){
                console.error('Error fetching transaction statistics: ', error);
            }
        };
        fetchTransactionStatistics();
    }, [])

    useEffect(() => {
        const fetchMonthlyTransactionStatsByUserID = async () => {
            let userID = sessionStorage.getItem('userID');
            try{
                const response = await axios.get(`http://localhost:8080/AeroBankApp/api/transactionHistory/amount/month/${userID}`)
                if(response.status === 200 || response.status === 201){
                    setTotalMonthlyTransactions(response.data);
                }

            }catch(error){
                console.error()
            }
        }
        fetchMonthlyTransactionStatsByUserID();
    }, []);

    const getStartOfWeek = () => {
        const today = new Date();

        const dayOfWeek = (today.getDay() + 6) % 7;

        return new Date(today.setDate(today.getDate() - dayOfWeek));
    }

    const getEndOfWeek = () => {
        const today = new Date();

        const startOfWeek = getStartOfWeek();

        return new Date(today.setDate(startOfWeek.getDate() + 6));
    }

    const getWeekString = () => {

    }

    const getCurrentMonthAsString = () => {
        const today = new Date();

        const currentMonth = today.getMonth();
        console.log('Current Month: ', currentMonth);
        return getMonthString(currentMonth);
    }

    const getMonthString = (currentMonth) => {
        let month = "";
        switch(currentMonth){
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
        }
        return month;
    }

    const formatDate = (date) => {
        let month = (date.getMonth() + 1).toString().padStart(2, '0');  // Months are 0-based, add 1
        let day = date.getDate().toString().padStart(2, '0');
        let year = date.getFullYear();
        return `${month}/${day}/${year}`;
    };

    return (
        <div>
            <Home />
            <LocalizationProvider dateAdapter={AdapterDateFns}>
                <div style={{
                    backgroundImage: `url(${backgroundImage})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    minHeight: '100vh',
                    padding: '20px'
                }}>
                    <Typography variant="h4" align="center" gutterBottom>Transaction Analytics</Typography>
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
                                            <TransactionSummaryStats title="Transaction Count" value={transactionStatistics.totalTransactionCount} />
                                        </Grid>
                                        <Grid item xs={4}>
                                            <TransactionSummaryStats title="Total Amount Transferred" value={transactionStatistics.totalTransferredAmount} />
                                        </Grid>
                                        {/* Row 2 */}
                                        <Grid item xs={4}>
                                            <TransactionSummaryStats title="Average Transaction Value" value={transactionStatistics.averageTransactionValue} />
                                        </Grid>
                                        <Grid item xs={4}>
                                            {/* You can add more stats here */}
                                            <TransactionSummaryStats title="Total Transactions Amount This Month" value={transactionStatistics.totalTransactionAmountByMonth} />
                                        </Grid>
                                        <Grid item xs={4}>
                                            {/* Additional stat */}
                                            <TransactionSummaryStats title={`Total Transactions for ${formatDate(getStartOfWeek())} to ${formatDate(getEndOfWeek())}`} value={transactionStatistics.totalTransactionsThisWeek} />
                                        </Grid>
                                        {/* Row 3 */}
                                        <Grid item xs={4}>
                                            {/* Additional stat */}
                                            <TransactionSummaryStats title="Currently Pending" value={transactionStatistics.pendingTransactionCount} />
                                        </Grid>
                                        <Grid item xs={4}>
                                            {/* Placeholder or another stat */}
                                            <TransactionSummaryStats title="Failed Transactions" value="0" />
                                        </Grid>
                                        <Grid item xs={4}>
                                            {/* Placeholder or another stat */}
                                            <TransactionSummaryStats title={`Total Transfers for ${getCurrentMonthAsString()}`} value="15" />
                                        </Grid>
                                    </Grid>
                                </AccordionDetails>
                            </Accordion>
                        </Grid>
                        <Grid item xs={12} md={10}>
                            <TransactionSummaryTable  />
                        </Grid>
                    </Grid>
                </div>
            </LocalizationProvider>
        </div>

    );

}

export default TransactionSummary;

