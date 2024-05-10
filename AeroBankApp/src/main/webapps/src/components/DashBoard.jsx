import {
    Accordion,
    AccordionDetails, AccordionSummary, Autocomplete,
    Card,
    Grid,
    List,
    ListItem,
    ListItemText,
    Paper,
    Table, TableBody,
    TableCell,
    TableHead,
    TableRow, TextField,
    Typography
} from "@mui/material";
import axios from "axios";
import {useEffect, useState} from "react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {Skeleton} from "@mui/lab";
import Home from "./Home";
import {Box} from "@mui/system";
import backgroundImage from './images/pexels-eberhard-grossgasteiger-1366907.jpg';
import MenuAppBar from "./MenuAppBar";
import ScheduledPayments from "./ScheduledPayments";
import BIllsDue from "./BillsDue";
import BillsDue from "./BillsDue";

export default function DashBoard()
{
    const user = sessionStorage.getItem('username');
    const [isLoading, setIsLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');
    const [filteredTransactions, setFilteredTransactions] = useState([]);

    const paperStyle = {
        padding: '16px',
        backgroundColor: 'rgba(255, 255, 255, 0.9)', // Semi-transparent white
        boxShadow: '0px 4px 8px rgba(0,0,0,0.1)',
        borderRadius: '8px'
    };

    const headingStyle = {
        margin: '0 0 16px 0',
        color: '#205375', // Dark blue color for headings
    };

    // Dummy data for the dashboard
    const dummyAccounts = [
        {
            id: 1,
            name: 'Checking Account',
            balance: '$5,320.50',
            availableBalance: '$5,000.00',
            transactions: [
                { date: '2023-05-01', description: 'Grocery Store', amount: '-$76.43' },
                { date: '2023-04-30', description: 'ATM Withdrawal', amount: '-$200.00' },
            ]
        },
        {
            id: 2,
            name: 'Savings Account',
            balance: '$10,764.32',
            availableBalance: '$10,764.32',
            transactions: [
                { date: '2023-04-29', description: 'Deposit', amount: '+$1,000.00' },
                { date: '2023-04-15', description: 'Transfer to Checking', amount: '-$500.00' },
            ]
        }
    ];

    const allTransactions = [
        { date: '2023-05-01', description: 'Grocery Store', amount: '-$76.43', category: 'Groceries' },
        { date: '2023-04-29', description: 'Salary Deposit', amount: '+$2,000.00', category: 'Income' },
        { date: '2023-04-28', description: 'Electric Bill', amount: '-$120.15', category: 'Utilities' },
        { date: '2024-05-08', description: 'Grocery Store', amount: '-25.46', category: 'Groceries'},
        { date: '2024-05-08', description: 'Gas Bill', amount: '-44.56', category: 'Utilities'},
        { date: '2024-05-09', description: 'Hair Cut', amount: '-21.00', category: 'Expense'}
        // More transactions...
    ];

    const uniqueDescriptions = Array.from(new Set(allTransactions.map(tx => tx.description)));
    const uniqueCategories = Array.from(new Set(allTransactions.map(tx => tx.category)));


    const dummyScheduledPayments = [
        { dueDate: '2023-05-15', payee: 'Internet Provider', amount: '$59.99', confirmationNumber: 232323, status: 'COMPLETE' },
        { dueDate: '2023-05-20', payee: 'Mortgage', amount: '$1,500.00', confirmationNumber: 4343434, status: 'PENDING' }
    ];

    const dummyBillsDue = [
        {
            id: 1,
            billDate: '2023-05-10',
            billType: 'Credit Card',
            amountDue: '$423.78',
            paymentMethod: 'Online Transfer',
            status: 'upcoming' // Could be 'upcoming' or 'overdue'
        },
        {
            id: 2,
            billDate: '2023-05-15',
            billType: 'Utility Bill',
            amountDue: '$88.45',
            paymentMethod: 'Direct Debit',
            status: 'overdue'
        },
        {
            id: 3,
            billDate: '2023-05-20',
            billType: 'Mortgage',
            amountDue: '$1,500.00',
            paymentMethod: 'Check',
            status: 'upcoming'
        },
        {
            id: 4,
            billDate: '2023-05-25',
            billType: 'Mobile Phone',
            amountDue: '$59.99',
            paymentMethod: 'Online Transfer',
            status: 'upcoming'
        },
        {
            id: 5,
            billDate: '2023-05-30',
            billType: 'Insurance',
            amountDue: '$230.00',
            paymentMethod: 'Credit Card',
            status: 'overdue'
        }
    ];



    const dashboardStyle = {
        position: 'relative',
        flexGrow: 1,
        minHeight: '100vh', // Ensures the dashboard extends full height of the viewport
        backgroundImage: '#e3f2fd', // General background color
    };

    const backgroundStyle = {
        backgroundImage: `url(${backgroundImage})`,
        position: 'absolute',
        top: '64px', // Position below the AppBar height
        left: 0,
        right: 0,
        bottom: 0,
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        zIndex: -1
    };

    const gridContainerStyle = {
        position: 'relative',
        zIndex: 1,
        padding: '20px',
        marginTop: '64px', // Ensure there's space for the AppBar above
    };

    const sectionStyle = (image) => ({
        backgroundImage: image ? `url(${image})` : 'none',
        backgroundSize: 'cover',
        backgroundRepeat: 'no-repeat',
        backgroundPosition: 'center',
        padding: '16px',
        borderRadius: '8px',
        backgroundColor: 'rgba(255, 255, 255, 0.9)', // Semi-transparent background for readability
        boxShadow: '0px 4px 8px rgba(0,0,0,0.1)' // Soft shadow for depth
    });


    useEffect(() => {
        if (!searchTerm) {
            setFilteredTransactions(allTransactions);
            return;
        }
        const lowercasedFilter = searchTerm.toLowerCase();
        const filteredData = allTransactions.filter(transaction => {
            return (
                transaction.description.toLowerCase().includes(lowercasedFilter) ||
                transaction.category.toLowerCase().includes(lowercasedFilter)
            );
        });
        setFilteredTransactions(filteredData);
    }, [searchTerm]);

    // Simulate data fetching on component mount
    useEffect(() => {
        setTimeout(() => {
            setIsLoading(false);
        }, 1000);
    }, []);

    return (
        <Box sx={dashboardStyle}>
            <MenuAppBar />
            <Box sx={backgroundStyle}/>
            <Grid container spacing={2} sx={gridContainerStyle}>
                <Grid item xs={12} md={6}>
                    <Paper sx={paperStyle}>
                        <Typography variant="h6" sx={headingStyle}>Account Summaries</Typography>
                        {isLoading ? (
                            <Skeleton variant="rectangular" height={300} />
                        ) : (
                            dummyAccounts.map(account => (
                                <Accordion key={account.id}>
                                    <AccordionSummary
                                        expandIcon={<ExpandMoreIcon />}
                                        aria-controls="panel-content"
                                        id="panel-header"
                                    >
                                        <Typography>{account.name}</Typography>
                                    </AccordionSummary>
                                    <AccordionDetails>
                                        <Typography sx={{ mb: 2 }}>Balance: {account.balance}</Typography>
                                        <Typography sx={{ mb: 2 }}>Available: {account.availableBalance}</Typography>
                                        <Table size="small">
                                            <TableHead>
                                                <TableRow>
                                                    <TableCell>Date</TableCell>
                                                    <TableCell>Description</TableCell>
                                                    <TableCell>Amount</TableCell>
                                                </TableRow>
                                            </TableHead>
                                            <TableBody>
                                                {account.transactions.map((transaction, index) => (
                                                    <TableRow key={index}>
                                                        <TableCell>{transaction.date}</TableCell>
                                                        <TableCell>{transaction.description}</TableCell>
                                                        <TableCell>{transaction.amount}</TableCell>
                                                    </TableRow>
                                                ))}
                                            </TableBody>
                                        </Table>
                                    </AccordionDetails>
                                </Accordion>
                            ))
                        )}
                    </Paper>
                </Grid>

                <Grid item xs={12} md={6}>
                    <Paper sx={paperStyle}>
                        <Typography variant="h6" sx={headingStyle}>Recent Transactions</Typography>
                        <Autocomplete
                            freeSolo
                            options={uniqueDescriptions}
                            renderInput={(params) => (
                                <TextField
                                    {...params}
                                    label="Search Transactions"
                                    variant="outlined"
                                    fullWidth
                                    sx={{ mb: 2 }} // Margin bottom for spacing
                                />
                            )}
                            onChange={(event, newValue) => {
                                setSearchTerm(newValue);
                            }}
                        />
                        {isLoading ? (
                            <Skeleton variant="rectangular" height={300} />
                        ) : (
                            <Table>
                                <TableHead>
                                    <TableRow>
                                        <TableCell>Date</TableCell>
                                        <TableCell>Description</TableCell>
                                        <TableCell>Amount</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {filteredTransactions.map((transaction, index) => (
                                        <TableRow key={index}>
                                            <TableCell>{transaction.date}</TableCell>
                                            <TableCell>{transaction.description}</TableCell>
                                            <TableCell>{transaction.amount}</TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        )}
                    </Paper>
                </Grid>
                <ScheduledPayments payments={dummyScheduledPayments}/>
                <BillsDue bills={dummyBillsDue} />
            </Grid>
        </Box>
    );
}