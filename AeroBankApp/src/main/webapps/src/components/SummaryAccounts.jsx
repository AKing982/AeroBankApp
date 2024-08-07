import {Avatar, IconButton, List, ListItem, ListItemAvatar, ListItemText, Paper, Typography} from "@mui/material";
import {Box} from "@mui/system";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import AccountSummaryBox from "./AccountSummaryBox";
import MenuAppBar from "./MenuAppBar";
import backgroundImage from "./images/pexels-pixabay-210307.jpg";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";

function SummaryAccounts()
{
    const navigate = useNavigate();
    const accounts = [
        {id: 2, name: 'SAVING SHARES', available: 1249.26, color: 'blue', image: '/images/pexels-pixabay-417173.jpg' },
        {id: 3, name: 'RENT', available: 9.16, color: 'purple', image: '/images/pexels-james-wheeler-417074.jpg'},
        {id: 1, name: 'CHECKING', available: 898.44, color: 'teal', image: '/images/pexels-julius-silver-753325.jpg'}
    ];
    const [accountSummaries, setAccountSummaries] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const username = sessionStorage.getItem('username');

    const fetchAccountSummaryData = async() => {
        setIsLoading(true);
        try
        {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data`, {
                params:
                    {
                    user: username
                }
            });
            const summaries = response.data;
            setAccountSummaries(summaries);
            console.log('Account Summaries: ', summaries);

        }catch(error)
        {
            console.error("There was an error fetching account summary data: ", error);
        }
        finally
        {
            setIsLoading(false);
        }
    }

    useEffect(() => {
      fetchAccountSummaryData();
    }, [])

    const totalBalance = 2202.41;
    const totalAvailable = 2156.86;

    const handleAccountSelect = (accountId) => {
        const currentDate = new Date();
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();
        navigate(`/accounts/${accountId}/${year}/${month}`)
    }

    return (
        <div style={{
            background: `url(${backgroundImage}) no-repeat center bottom`,
            backgroundSize: 'cover',
            minHeight: 'calc(120vh - 64px)',
            width: '100%',
            position: 'relative',
        }}>
        <MenuAppBar />
        <Paper elevation={3} sx={{ p: 2, borderRadius: 2 }}>
            <Typography variant="h4" gutterBottom>
                Shares
            </Typography>
            <Box display="flex" flexDirection="column" alignItems="flex-start" mb={2}>
                <Typography variant="body2">Total Balance ${totalBalance}</Typography>
                <Typography variant="body2">Total Available ${totalAvailable}</Typography>
            </Box>
            <List disablePadding>
                {accountSummaries.map((account) => (
                    <AccountSummaryBox
                        key={account.acctID}
                        id={account.shortSegment}
                        name={account.accountName}
                        balance={account.balance}
                        color={account.acctColor}
                        image={account.acctImg}
                        onSelect={() => handleAccountSelect(account.acctID)}
                    />
                ))}
            </List>
        </Paper>
        </div>
    );

}

export default SummaryAccounts;