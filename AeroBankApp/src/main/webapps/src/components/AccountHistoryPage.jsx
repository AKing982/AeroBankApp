import {
    Avatar,
    Divider, Grid,
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
import Account from "./Account";
import axios from "axios";
import {Skeleton} from "@mui/lab";
import {styled} from "@mui/material/styles";

function AccountHistoryPage({balance, available})
{
    const {id: initialId, month: initialMonth, year: initialYear} = useParams();
    const [selectedAccount, setSelectedAccount] = useState(null);
    const [otherAccounts, setOtherAccounts] = useState([]);
    const navigate = useNavigate();
    const [accountData, setAccountData] = useState([]);
    const username = sessionStorage.getItem('username');
    const [notificationsByAccount, setNotificationsByAccount] = useState([]);

    const [isLoading, setIsLoading] = useState(true);

    const monthName = new Date(initialYear, initialMonth-1).toLocaleString('default', {month: 'long'});

    // Mock accounts data - replace with actual data fetching
    const allAccounts = [
        { id: 1, name: 'CHECKING', balance: 1249.26, available: 1249.26, color: 'red' },
        { id: 2, name: 'SAVINGS', balance: 9.16, available: 9.16, color: 'purple' },
        { id: 3, name: 'RENT', balance: 898.44, available: 898.44, color: 'teal' },
    ];


    useEffect(() => {
        // Set a timeout to delay the fetch operation
        const timeout = setTimeout(() => {
            const fetchAccountsAndNotifications = async () => {
                setIsLoading(true);
                try {
                    // Fetch account data
                    const accountsResponse = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data`, {
                        params: {
                            user: username
                        }
                    });
                    const accounts = accountsResponse.data;
                    setAccountData(accounts);
                    console.log('Accounts Response: ', accountsResponse.data);
                    console.log('AccountID List: ', accountsResponse.data.acctID);
                    const selected = accounts.find(account => account.acctID === parseInt(initialId));
                    setSelectedAccount(selected);


                    // Prepare and execute all notifications fetch operations
                    const notificationPromises = accounts.map(account =>
                        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/notifications/${account.acctID}`)
                            .catch(error => {
                                console.error(`Failed to fetch notifications for account ID ${account.acctID}:`, error);
                                return { error };  // Return an error object to handle this case gracefully
                            })
                    );

                    // Resolve all notification promises
                    const notificationResponses = await Promise.allSettled(notificationPromises);
                    console.log('Notification Response: ', notificationResponses);

                    // Initialize notifications map from resolved promises
                    const newNotificationsByAccount = {};
                    notificationResponses.forEach((result, index) => {
                        if (result.status === 'fulfilled' && Array.isArray(result.value.data)) {
                            newNotificationsByAccount[accounts[index].acctID] = result.value.data;
                        } else {
                            newNotificationsByAccount[accounts[index].acctID] = []; // Handle errors or no notifications case
                        }
                    });

                    console.log('New Notifications By Account: ', newNotificationsByAccount);
                    setNotificationsByAccount(newNotificationsByAccount);

                    console.log('NotificationsByAccount: ', notificationsByAccount);
                } catch (error) {
                    console.error('Error fetching account data or notifications:', error);
                } finally {
                    setIsLoading(false); // Ensure the loading state is updated correctly
                }
            };

            fetchAccountsAndNotifications();
        }, 2000); // Delay fetching by 2000 ms

        // Clear the timeout when the component unmounts or the dependency changes
        return () => clearTimeout(timeout);
    }, [username]); // Depend on 'username' to refetch when it changes


    const handleAccountSelect = (accountId) => {
        const newSelectedAccount = accountData.find(account => account.acctID === accountId);
        setSelectedAccount(newSelectedAccount);
        navigate(`/accounts/${accountId}/${initialYear}/${initialMonth}`);
    }

    const AccountSkeleton = () => (
        <Box sx={{ mb: 2 }}>
            <Skeleton variant="rectangular" width={300} height={200} />
        </Box>
    );

    const WiderAccountBox = styled(Box)(({ theme }) => ({
        width: '100%',
        marginBottom: theme.spacing(2),
    }));


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
                <Box sx={{ width: 600, mr: 2 }}>
                    {/* Selected account */}
                    {isLoading ? (
                        <Skeleton variant="rectangular" width={300} height={200} sx={{ mb: 2 }} />
                    ) : selectedAccount ? (
                        <Box sx={{ mb: 2 }}>
                            <Account
                                color={selectedAccount.acctColor}
                                accountCode={selectedAccount.shortSegment}
                                acctID={selectedAccount.acctID}
                                accountName={selectedAccount.accountName}
                                balance={selectedAccount.balance}
                                pending="0.00"
                                available={selectedAccount.available}
                                notifications={notificationsByAccount[selectedAccount.acctID] || []}
                                onNotificationClick={() => {}}
                                notificationCount={notificationsByAccount[selectedAccount.acctID]?.length || 0}
                                onAccountClick={() => {}}
                                isSelected={true}
                                backgroundImageUrl={selectedAccount.acctImage}
                            />
                        </Box>
                    ) : null}

                    <Grid container spacing={2}>
                        {isLoading ? (
                            [...Array(3)].map((_, index) => (
                                <Grid item xs={16} key={index}>
                                    <Skeleton variant="rectangular" height={100} />
                                </Grid>
                            ))
                        ) : (
                            accountData
                                .filter(account => account.acctID !== selectedAccount?.acctID)
                                .map((account) => (
                                    <Grid item xs={12} key={account.acctID}>
                                        <Account
                                            color={account.acctColor}
                                            accountCode={account.shortSegment}
                                            acctID={account.acctID}
                                            accountName={account.accountName}
                                            balance={account.balance}
                                            pending="0.00"
                                            available={account.available}
                                            notifications={notificationsByAccount[account.acctID] || []}
                                            onNotificationClick={() => {}}
                                            notificationCount={notificationsByAccount[account.acctID]?.length || 0}
                                            onAccountClick={handleAccountSelect}
                                            isSelected={false}
                                            backgroundImageUrl={account.acctImage}
                                        />
                                    </Grid>
                                ))
                        )}
                    </Grid>
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
                        {isLoading ? (
                            <Skeleton variant="rectangular" width="100%" height={400} />
                        ) : (
                            selectedAccount && <TransactionTable accountID={selectedAccount.acctID} />
                        )}
                        {/*{selectedAccount && <TransactionTable accountID={selectedAccount.id} />}*/}
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