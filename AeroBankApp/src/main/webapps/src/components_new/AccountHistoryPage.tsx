import {FunctionComponent, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import axios from "axios";
import {Box} from "@mui/system";
import {Skeleton} from "@mui/lab";
import {styled} from "@mui/material/styles";
// @ts-ignore
import backgroundImage from "../components/images/pexels-pixabay-210307.jpg";
import MenuAppBar from "../components/MenuAppBar";
import Account from "../components/Account";
import {Divider, Grid, IconButton, Paper, Typography} from "@mui/material";
import {ChevronLeft, ChevronRight, Download, FilterList, FormatListBulleted, Print} from "@mui/icons-material";
import TransactionTable from "../components/TransactionTable";
import {fetchAccountsAndNotifications} from '../api/AccountHistoryApiService'

interface AccountHistoryPageProps {
    balance?: number
    available?: number,
}

type AccountType = {
    acctID: number;
    [key: string]: any;
};

type NotificationType = {
    [acctID: string] : any[];
};

const AccountHistoryPage: FunctionComponent<AccountHistoryPageProps> = ({balance, available} : AccountHistoryPageProps) => {
    const {id: initialId, month: initialMonth, year: initialYear} = useParams<{id: string, month: string, year: string}>();
    const [selectedAccount, setSelectedAccount] = useState<any>(null);
    const [otherAccounts, setOtherAccounts] = useState<any>([]);
    const navigate = useNavigate();
    const [accountData, setAccountData] = useState<any[]>([]);
    const username: string | null = sessionStorage.getItem('username');
    const [notificationsByAccount, setNotificationsByAccount] = useState<NotificationType[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(true);
    // const monthName: string | null = new Date(initialYear, initialMonth-1).toLocaleString('default', {month: 'long'});

    const allAccounts = [
        { id: 1, name: 'CHECKING', balance: 1249.26, available: 1249.26, color: 'red' },
        { id: 2, name: 'SAVINGS', balance: 9.16, available: 9.16, color: 'purple' },
        { id: 3, name: 'RENT', balance: 898.44, available: 898.44, color: 'teal' },
    ];

    // useEffect(() => {
    //     // Set a timeout to delay the fetch operation
    //     let user : string = String(username);
    //     let id : string = String(initialId);
    //     const timeout = setTimeout(() => {
    //         fetchAccountsAndNotifications(user, id)
    //             .then(({accounts, selectedAccount, newNotificationsByAccount}) => {
    //                 setAccountData(accounts);
    //                 setSelectedAccount(selectedAccount);
    //                 setNotificationsByAccount(newNotificationsByAccount);
    //                 setIsLoading(false);
    //             })
    //             .catch((error) => {
    //                 console.error('Error occurred:', error);
    //             });
    //     }, 2000); // Delay fetching by 2000 ms
    //
    //     // Clear the timeout when the component unmounts or the dependency changes
    //     return () => clearTimeout(timeout);
    // }, [username]); // Depend on 'username' to refetch when it changes

    const handleAccountSelect = (accountId: number) : void => {
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

    // return (
    //     <div style={{
    //         background: `url(${backgroundImage}) no-repeat center bottom`,
    //         backgroundSize: 'cover',
    //         minHeight: 'calc(120vh - 64px)',
    //         width: '100%',
    //         position: 'relative',
    //     }}>
    //         <MenuAppBar />
    //         <Box sx={{ display: 'flex', p: 2 }}>
    //             {/* Left sidebar */}
    //             <Box sx={{ width: 600, mr: 2 }}>
    //                 {/* Selected account */}
    //                 {isLoading ? (
    //                     <Skeleton variant="rectangular" width={300} height={200} sx={{ mb: 2 }} />
    //                 ) : selectedAccount ? (
    //                     <Box sx={{ mb: 2 }}>
    //                         <Account
    //                             color={selectedAccount.acctColor}
    //                             accountCode={selectedAccount.shortSegment}
    //                             acctID={selectedAccount.acctID}
    //                             accountName={selectedAccount.accountName}
    //                             balance={selectedAccount.balance}
    //                             pending="0.00"
    //                             available={selectedAccount.available}
    //                             notifications={notificationsByAccount[selectedAccount.acctID] || []}
    //                             onNotificationClick={() => {}}
    //                             notificationCount={notificationsByAccount[selectedAccount.acctID]?.length || 0}
    //                             onAccountClick={() => {}}
    //                             isSelected={true}
    //                             backgroundImageUrl={selectedAccount.acctImage}
    //                         />
    //                     </Box>
    //                 ) : null}
    //
    //                 <Grid container spacing={2}>
    //                     {isLoading ? (
    //                         [...Array(3)].map((_, index) => (
    //                             <Grid item xs={16} key={index}>
    //                                 <Skeleton variant="rectangular" height={100} />
    //                             </Grid>
    //                         ))
    //                     ) : (
    //                         accountData
    //                             .filter(account => account.acctID !== selectedAccount?.acctID)
    //                             .map((account) => (
    //                                 <Grid item xs={12} key={account.acctID}>
    //                                     <Account
    //                                         color={account.acctColor}
    //                                         accountCode={account.shortSegment}
    //                                         acctID={account.acctID}
    //                                         accountName={account.accountName}
    //                                         balance={account.balance}
    //                                         pending="0.00"
    //                                         available={account.available}
    //                                         notifications={notificationsByAccount[account.acctID] || []}
    //                                         onNotificationClick={() => {}}
    //                                         notificationCount={notificationsByAccount[account.acctID]?.length || 0}
    //                                         onAccountClick={handleAccountSelect}
    //                                         isSelected={false}
    //                                         backgroundImageUrl={account.acctImage}
    //                                     />
    //                                 </Grid>
    //                             ))
    //                     )}
    //                 </Grid>
    //             </Box>
    //             {/* Main content */}
    //             <Box sx={{ flexGrow: 1 }}>
    //                 <Paper elevation={3} sx={{ p: 2, mb: 2 }}>
    //                     <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
    //                         <Box sx={{ display: 'flex', alignItems: 'center' }}>
    //                             <IconButton>
    //                                 <ChevronLeft />
    //                             </IconButton>
    //                             <Typography variant="h6">{monthName.toUpperCase()} {initialYear}</Typography>
    //                             <IconButton>
    //                                 <ChevronRight />
    //                             </IconButton>
    //                         </Box>
    //                         <Box>
    //                             <IconButton>
    //                                 <FilterList />
    //                             </IconButton>
    //                             <IconButton>
    //                                 <FormatListBulleted />
    //                             </IconButton>
    //                             <IconButton>
    //                                 <Download />
    //                             </IconButton>
    //                             <IconButton>
    //                                 <Print />
    //                             </IconButton>
    //                         </Box>
    //                     </Box>
    //                     <Divider />
    //                     {isLoading ? (
    //                         <Skeleton variant="rectangular" width="100%" height={400} />
    //                     ) : (
    //                         selectedAccount && <TransactionTable accountID={selectedAccount.acctID} />
    //                     )}
    //                     {/*{selectedAccount && <TransactionTable accountID={selectedAccount.id} />}*/}
    //                 </Paper>
    //             </Box>
    //         </Box>
    //     </div>
    // );
    return null;
}
