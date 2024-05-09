import AccountBox from "./AccountBox";
import '../AccountBox.css';
import './TransactionView.css';
import ListView from "./AccountListView";
import {Divider, Grid, Paper, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import TransactionTable from "./TransactionTable";
import PendingTransactionsTable from "./PendingTransactionsTable";
import {Box} from "@mui/system";
import Home from "./Home";
import MenuAppBar from "./MenuAppBar";
import GradientSeparator from "./GradientSeparator";
import backgroundImage from './images/pexels-pixabay-210307.jpg';


export default function TransactionView()
{
    const [accountID, setAccountID] = useState(0);
    const [fullName, setFullName] = useState('');
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');

    useEffect(() => {
        const fetchUsersName = () => {
            const userID = sessionStorage.getItem('userID');
            axios.get(`http://localhost:8080/AeroBankApp/api/users/name/${userID}`)
                .then(response => {
                    console.log('Full Name Response: ', response.data);
                    setFirstName(response.data.firstName);
                    setLastName(response.data.lastName);
                })
                .catch(error => {
                    console.error('An Error occurred fetching the users full name: ', error);
                });
        }
        fetchUsersName();

    }, [])

    function TimeGreeting({name}) {
        const [greeting, setGreeting] = useState('');

        useEffect(() => {
            const updateGreeting = () => {
                const now = new Date();
                const hours = now.getHours();
                let currentGreeting = '';

                if(hours >= 5 && hours < 12)
                {
                    currentGreeting = 'Good Morning';
                }
                else if(hours >= 12 && hours < 17)
                {
                    currentGreeting = 'Good Afternoon';
                }
                else if(hours >= 17 || hours < 5)
                {
                    currentGreeting = 'Good Evening';
                }

                setGreeting(currentGreeting);
            };

            updateGreeting();

            const timerID = setInterval(updateGreeting, 3600000);

            return function cleanup()
            {
                clearInterval(timerID);
            }
        }, []);


        return (
            <span style={{
                color: '#555', // Slightly darker text for the greeting
                textShadow: '1px 1px 1px #eee' // Subtle shadow for depth
            }}>
            {greeting},
            <span style={{ color: '#000', fontWeight: 'bold' }}>{name}</span>
        </span>
        );

    }

    return (
        <div>
            <MenuAppBar />
            <GradientSeparator />
            <Box sx={{ flexGrow: 1,
                padding: 3,
                backgroundColor: '#f5f5f5',
                backgroundImage: `url(${backgroundImage})`,
                backgroundSize: 'cover',
                backgroundRepeat: 'no-repeat',
                marginTop: '-66px',
                paddingTop: '63px'}}>
                <Grid container spacing={2}>
                    <Grid item xs={12} md={6}>
                        <Paper elevation={3} sx={{
                            margin: 2,
                            padding: 2,
                            backgroundColor: 'rgba(255, 255, 255, 0.95)', // Slightly transparent background to blend with the backdrop
                            backdropFilter: 'blur(10px)', // Softening the background image underneath for better readability
                            border: '1px solid #ccc', // Adding a subtle border to define the Paper's boundaries
                            borderRadius: '8px', // Soft rounded corners for a smoother look
                            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)' // Enhancing the shadow for better depth perception
                        }}>
                            <Typography variant="h6" component="div" sx={{
                                color: '#333', // Subdued, professional text color
                                fontWeight: 'medium', // Medium weight for slight emphasis
                                padding: '8px 0', // Padding above and below the text for better spacing
                                borderBottom: '2px solid #ccc', // A subtle underline to denote section separation
                                marginBottom: '12px', // Margin below to separate from other content
                                textAlign: 'center' // Center alignment to make it stand out
                            }}>
                                <TimeGreeting name={firstName} />
                            </Typography>
                            <ListView updateAccountID={setAccountID} />
                        </Paper>
                        <Divider orientation="vertical" flexItem />
                    </Grid>
                    <Grid item xs={12} md={6}>
                        <Paper elevation={3}
                               sx={{
                                   margin: 2,
                                   padding: 2,
                                   backdropFilter: 'blur(10px)',
                                   border: '1px solid #ccc',
                                   borderRadius: '8px',
                                   height: 'auto',
                                   width: { xs: '100%', sm: '90%', md: '100%' }, // Responsive widths
                                   maxWidth: '1200px', // Maximum width to ensure it doesn't get too wide on large screens
                                   mx: 'auto' // Centers the Paper component within its container
                               }}>
                            <PendingTransactionsTable accountID={accountID} />
                            <TransactionTable accountID={accountID} />
                        </Paper>
                    </Grid>
                </Grid>
                <Box sx={{ pt: 2, textAlign: 'center' }}>
                    {/* Footer content can go here */}
                </Box>
            </Box>
        </div>

    );
}