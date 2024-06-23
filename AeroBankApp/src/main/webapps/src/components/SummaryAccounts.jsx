import {Avatar, IconButton, List, ListItem, ListItemAvatar, ListItemText, Paper, Typography} from "@mui/material";
import {Box} from "@mui/system";
import MoreVertIcon from '@mui/icons-material/MoreVert';
import AccountSummaryBox from "./AccountSummaryBox";
import MenuAppBar from "./MenuAppBar";
import backgroundImage from "./images/pexels-pixabay-210307.jpg";
import {useNavigate} from "react-router-dom";

function SummaryAccounts()
{
    const navigate = useNavigate();
    const accounts = [
        {id: 1, name: 'SAVING SHARES', available: 1249.26, color: 'blue', image: '/images/pexels-pixabay-417173.jpg' },
        {id: 2, name: 'RENT', available: 9.16, color: 'purple', image: '/images/pexels-james-wheeler-417074.jpg'},
        {id: 3, name: 'CHECKING', available: 898.44, color: 'teal', image: '/images/pexels-julius-silver-753325.jpg'}
    ];

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
                <Typography variant="body2">Total Balance ${totalBalance.toFixed(2)}</Typography>
                <Typography variant="body2">Total Available ${totalAvailable.toFixed(2)}</Typography>
            </Box>
            <List disablePadding>
                {accounts.map((account) => (
                    <AccountSummaryBox
                        key={account.id}
                        id={account.id}
                        name={account.name}
                        available={account.available}
                        color={account.color}
                        image={account.image}
                        onSelect={() => handleAccountSelect(account.id)}
                    />
                ))}
            </List>
        </Paper>
        </div>
    );

}

export default SummaryAccounts;