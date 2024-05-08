import {
    Accordion,
    AccordionDetails, AccordionSummary,
    Card,
    Grid,
    List,
    ListItem,
    ListItemText,
    Paper,
    Table, TableBody,
    TableCell,
    TableHead,
    TableRow,
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

export default function DashBoard()
{
    const user = sessionStorage.getItem('username');
    const [isLoading, setIsLoading] = useState(true);

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
    // const gridContainerStyle = {
    //     position: 'relative',
    //     zIndex: 1,
    //     margin: '20px 0' // Adjust margins as needed
    // };

    // const gridContainerStyle = {
    //     backgroundImage: `url(${backgroundImage})`, // Set the background image here
    //     backgroundSize: 'cover',
    //     backgroundRepeat: 'no-repeat',
    //     backgroundPosition: 'center',
    //     margin: '15px', // Outer margin to ensure the background is visible around the edges
    //     padding: '20px', // Padding to create space between the grid cells and the container edges
    //     position: 'relative', // Ensures z-index stacking context starts here
    //     zIndex: 0 // Underlying layer
    // };

    // Dummy data for the dashboard
    const dummyAccounts = [
        { id: 1, name: 'Checking Account', balance: '$5,320.50' },
        { id: 2, name: 'Savings Account', balance: '$10,764.32' }
    ];
    const dummyTransactions = [
        { date: '2023-05-01', description: 'Grocery Store', amount: '-$76.43' },
        { date: '2023-04-29', description: 'Salary Deposit', amount: '+$2,000.00' },
        { date: '2023-04-28', description: 'Electric Bill', amount: '-$120.15' }
    ];
    const dummyScheduledPayments = [
        { dueDate: '2023-05-15', payee: 'Internet Provider', amount: '$59.99' },
        { dueDate: '2023-05-20', payee: 'Mortgage', amount: '$1,500.00' }
    ];
    const dummyBillsDue = [
        { billDate: '2023-05-10', billType: 'Credit Card', amountDue: '$423.78' },
        { billDate: '2023-05-15', billType: 'Utility Bill', amountDue: '$88.45' }
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


    // const dashboardStyle = {
    //     position: 'relative',
    //     flexGrow: 0,
    //     padding: '24px',
    //     minHeight: '50vh', // Ensures the dashboard extends to the full height of the viewport
    //     backgroundColor: '#e3f2fd',
    //     overflow: 'auto'
    // };
    //
    // const backgroundStyle = {
    //     backgroundImage: `url(${backgroundImage})`,
    //     position: 'absolute',
    //     top: '64px', // Start the background image below the AppBar height
    //     left: 0,
    //     right: 0,
    //     bottom: 0,
    //     backgroundSize: 'cover',
    //     backgroundRepeat: 'no-repeat',
    //     backgroundPosition: 'center',
    //     zIndex: -1
    // };
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
                    <Paper sx={sectionStyle()}>
                        <Typography variant="h6" sx={headingStyle}>Account Summaries</Typography>
                        {isLoading ? (
                            <Skeleton variant="rectangular" height={100} />
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
                                        <List>
                                            <ListItem>{`Balance: ${account.balance}`}</ListItem>
                                        </List>
                                    </AccordionDetails>
                                </Accordion>
                            ))
                        )}
                    </Paper>
                </Grid>

                <Grid item xs={12} md={6}>
                    <Paper sx={sectionStyle()}>
                        <Typography variant="h6" sx={headingStyle}>Recent Transactions</Typography>
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
                                    {dummyTransactions.map((transaction, index) => (
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

                <Grid item xs={12} md={6}>
                    <Paper style={paperStyle}>
                        <Typography variant="h6" sx={headingStyle}>Scheduled Payments</Typography>
                        {isLoading ? (
                            <Skeleton variant="rectangular" height={200} />
                        ) : (
                            <List>
                                {dummyScheduledPayments.map((payment, index) => (
                                    <ListItem key={index}>
                                        {`${payment.dueDate}: Pay ${payment.payee} ${payment.amount}`}
                                    </ListItem>
                                ))}
                            </List>
                        )}
                    </Paper>
                </Grid>

                <Grid item xs={12} md={6}>
                    <Paper style={paperStyle}>
                        <Typography variant="h6" sx={headingStyle}>Bills Due</Typography>
                        {isLoading ? (
                            <Skeleton variant="rectangular" height={200} />
                        ) : (
                            <List>
                                {dummyBillsDue.map((bill, index) => (
                                    <ListItem key={index}>
                                        {`${bill.billDate} - ${bill.billType}: ${bill.amountDue}`}
                                    </ListItem>
                                ))}
                            </List>
                        )}
                    </Paper>
                </Grid>
            </Grid>
        </Box>
    );
}