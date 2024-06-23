import {
    Avatar,
    Divider,
    IconButton,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    Paper,
    Typography
} from "@mui/material";
import {Box} from "@mui/system";
import {
    ChevronLeft,
    ChevronRight,
    Download,
    FilterList,
    FormatListBulleted,
    MoreVert,
    Print
} from "@mui/icons-material";
import TransactionTable from "./TransactionTable";
import backgroundImage from "./images/pexels-pixabay-210307.jpg";
import MenuAppBar from "./MenuAppBar";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";

function AccountHistoryPage({balance, available})
{
    const {id: initialId, month: initialMonth, year: initialYear} = useParams();
    const [selectedAccount, setSelectedAccount] = useState(null);
    const [otherAccounts, setOtherAccounts] = useState([]);
    const navigate = useNavigate();
    const monthName = new Date(initialYear, initialMonth-1).toLocaleString('default', {month: 'long'});

    // Mock accounts data - replace with actual data fetching
    const allAccounts = [
        { id: 1, name: 'SAVINGS SHARES', balance: 1249.26, available: 1249.26, color: 'red' },
        { id: 2, name: 'RENT', balance: 9.16, available: 9.16, color: 'purple' },
        { id: 3, name: 'CHECKING', balance: 898.44, available: 898.44, color: 'teal' },
    ];

    useEffect(() => {
        const selected = allAccounts.find(account => account.id === parseInt(initialId));
        setSelectedAccount(selected);
        setOtherAccounts(allAccounts.filter(account => account.id !== parseInt(initialId)));
    }, [initialId]);

    const handleAccountSelect = (accountId) => {
        navigate(`/accounts/${accountId}/${initialYear}/${initialMonth}`);
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
            <Box sx={{ display: 'flex', p: 2 }}>
                {/* Left sidebar */}
                <Box sx={{ width: 300, mr: 2 }}>
                    {selectedAccount && (
                        <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                                <Avatar sx={{ bgcolor: selectedAccount.color, mr: 2 }}>{selectedAccount.id}</Avatar>
                                <Typography variant="h6">{selectedAccount.name}</Typography>
                            </Box>
                            <Typography variant="body2">Balance</Typography>
                            <Typography variant="h6">${selectedAccount.balance.toFixed(2)}</Typography>
                            <Typography variant="body2">Available</Typography>
                            <Typography variant="h6">${selectedAccount.available.toFixed(2)}</Typography>
                            <Typography color="primary" sx={{ mt: 2 }}>QUICK TRANSFER</Typography>
                        </Paper>
                    )}

                    {/* Other accounts */}
                    <Paper elevation={3} sx={{ p: 2 }}>
                        <List>
                            {otherAccounts.map((account) => (
                                <ListItem key={account.id} button onClick={() => handleAccountSelect(account.id)}>
                                    <ListItemIcon>
                                        <Avatar sx={{ bgcolor: account.color }}>{account.id}</Avatar>
                                    </ListItemIcon>
                                    <ListItemText primary={account.name} secondary={`$${account.available.toFixed(2)}`} />
                                    <IconButton size="small">
                                        <MoreVert />
                                    </IconButton>
                                </ListItem>
                            ))}
                        </List>
                    </Paper>
                </Box>

                {/* Main content */}
                <Box sx={{ flexGrow: 1 }}>
                    <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
                        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                                <IconButton>
                                    <ChevronLeft />
                                </IconButton>
                                <Typography variant="h6">{monthName.toUpperCase()} {initialYear}</Typography>
                                <IconButton>
                                    <ChevronRight />
                                </IconButton>
                            </Box>
                            <Box>
                                <IconButton>
                                    <FilterList />
                                </IconButton>
                                <IconButton>
                                    <FormatListBulleted />
                                </IconButton>
                                <IconButton>
                                    <Download />
                                </IconButton>
                                <IconButton>
                                    <Print />
                                </IconButton>
                            </Box>
                        </Box>
                        <Divider />
                        {selectedAccount && <TransactionTable accountID={selectedAccount.id} />}
                    </Paper>
                </Box>
            </Box>
        </div>
    );

    // return (
    //     <div style={{
    //         background: `url(${backgroundImage}) no-repeat center bottom`,
    //         backgroundSize: 'cover',
    //         minHeight: 'calc(120vh - 64px)',
    //         width: '100%',
    //         position: 'relative',
    //     }}>
    //         <MenuAppBar/>
    //     <Box sx={{ display: 'flex', p: 2 }}>
    //         {/* Left sidebar */}
    //         <Box sx={{ width: 300, mr: 2 }}>
    //             <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
    //                 <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
    //                     <Avatar sx={{ bgcolor: 'primary.main', mr: 2 }}>{id}</Avatar>
    //                     <Typography variant="h6">CHECKING</Typography>
    //                 </Box>
    //                 <Typography variant="body2">Balance</Typography>
    //                 <Typography variant="h6">${balance}</Typography>
    //                 <Typography variant="body2">Available</Typography>
    //                 <Typography variant="h6">${available}</Typography>
    //                 <Typography color="primary" sx={{ mt: 2 }}>QUICK TRANSFER</Typography>
    //             </Paper>
    //
    //             {/* Other accounts */}
    //             <Paper elevation={3} sx={{ p: 2 }}>
    //                 <List>
    //                     <ListItem>
    //                         <ListItemIcon>
    //                             <Avatar sx={{ bgcolor: 'red' }}>1</Avatar>
    //                         </ListItemIcon>
    //                         <ListItemText primary="SAVINGS SHARES" secondary="$1,249.26" />
    //                         <IconButton size="small">
    //                             <MoreVert />
    //                         </IconButton>
    //                     </ListItem>
    //                     <ListItem>
    //                         <ListItemIcon>
    //                             <Avatar sx={{ bgcolor: 'purple' }}>2</Avatar>
    //                         </ListItemIcon>
    //                         <ListItemText primary="RENT" secondary="$9.16" />
    //                         <IconButton size="small">
    //                             <MoreVert />
    //                         </IconButton>
    //                     </ListItem>
    //                 </List>
    //             </Paper>
    //         </Box>
    //
    //         {/* Main content */}
    //         <Box sx={{ flexGrow: 1 }}>
    //             <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
    //                 <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
    //                     <Box sx={{ display: 'flex', alignItems: 'center' }}>
    //                         <IconButton>
    //                             <ChevronLeft />
    //                         </IconButton>
    //                         <Typography variant="h6">{monthName.toUpperCase()} {year}</Typography>
    //                         <IconButton>
    //                             <ChevronRight />
    //                         </IconButton>
    //                     </Box>
    //                     <Box>
    //                         <IconButton>
    //                             <FilterList />
    //                         </IconButton>
    //                         <IconButton>
    //                             <FormatListBulleted />
    //                         </IconButton>
    //                         <IconButton>
    //                             <Download />
    //                         </IconButton>
    //                         <IconButton>
    //                             <Print />
    //                         </IconButton>
    //                     </Box>
    //                 </Box>
    //                 <Divider />
    //                 <TransactionTable accountID={id}/>
    //             </Paper>
    //         </Box>
    //     </Box>
    //     </div>
    // );

}

export default AccountHistoryPage;