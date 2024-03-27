import AccountBox from "./AccountBox";
import '../AccountBox.css';
import './TransactionView.css';
import TableView from "./TableView";
import ListView from "./AccountListView";
import CollapsiblePanel from "./CollapsiblePanel";
import {Divider, Grid, Typography} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import TransactionTable from "./TransactionTable";
import PendingTransactionsTable from "./PendingTransactionsTable";
import {Box} from "@mui/system";


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


        return <span>{greeting}, {name}</span>;

    }

    return (
        <Box className="transaction-view-container" sx={{ flexGrow: 100}}>
            <Grid container spacing={2}>
                <Grid item xs={12} md={6} className="account-list-body">
                    <Box sx={{margin: 2}}>
                        <Typography variant="h6"><TimeGreeting name={firstName}/> </Typography>
                    </Box>
                    <ListView updateAccountID={setAccountID} />
                    <Divider orientation="vertical" flexItem />
                </Grid>
                <Grid item xs={12} md={6} className="transaction-view-right">
                    <Box sx={{ maxWidth: 'auto', margin: 'auto', width: '100%' }}>
                        <PendingTransactionsTable accountID={accountID} />
                        <TransactionTable accountID={accountID} />
                    </Box>
                </Grid>
            </Grid>
            <Box className="transaction-view-footer" sx={{ pt: 2 }}>
                {/* Footer content can go here */}
            </Box>
        </Box>
    );
}