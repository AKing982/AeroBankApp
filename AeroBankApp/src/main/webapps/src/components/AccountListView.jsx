import {useEffect, useState} from "react";
import axios from "axios";
import Account from "./Account";
import {CircularProgress, Grid, Typography} from "@mui/material";
import {Box} from "@mui/system";

const NotificationCategory = {
    TRANSACTION_ALERT: "TransactionAlert",
    BALANCE_UPDATE: "BalanceUpdate",
    ACCOUNT_SECURITY: "AccountSecurity",
    PAYMENT_REMINDER: "PaymentReminder",
    SCHEDULED_MAINTENANCE: "ScheduledMaintenance",
    ACCOUNT_UPDATE: "AccountUpdate"
};


const testNotifications = [
    {
        id: 1,
        title: "Payment Received",
        message: "You have received a payment of $150 from John Doe.",
        category: NotificationCategory.TRANSACTION_ALERT
    },
    {
        id: 2,
        title: "Account Alert",
        message: "Your account balance is lower than $100.",
        category: NotificationCategory.BALANCE_UPDATE
    },
    {
        id: 3,
        title: "Scheduled Maintenance",
        message: "Our banking services will be unavailable this Sunday from 2 AM to 5 AM due to scheduled maintenance.",
        category: NotificationCategory.SCHEDULED_MAINTENANCE
    },
    {
        id: 4,
        title: "New Offer",
        message: "A new savings account with an attractive interest rate is available now. Check it out!",
        category: NotificationCategory.ACCOUNT_UPDATE
    }
];


export default function AccountListView({updateAccountID})
{
    const [accountData, setAccountData] = useState([]);
    const [accountProperties, setAccountProperties] = useState([]);
    const [selectedAccount, setSelectedAccount] = useState(0);
    const [isLoading, setIsLoading] = useState(true);
    const [fullName, setFullName] = useState('');
    const [notifications, setNotifications] = useState([]);
    const [notificationsByAccount, setNotificationsByAccount] = useState([]);
    const [popOverOpen, setPopOverOpen] = useState(false);
    const [currentNotification, setCurrentNotification] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);

    const username = sessionStorage.getItem('username');

    const storeAccountCode = (accountCode) => {
        sessionStorage.setItem('AccountCode', accountCode);
    }

    useEffect(() => {
        console.log('Selected Account: ', selectedAccount);
        if(selectedAccount){
            fetchAccountID();
        }
        if(selectedAccount === ''){
            console.log('Going inside fetchAccountIDByUserID');
            fetchAccountIDByUserID();
        }
    }, [selectedAccount]);

    const fetchAccountIDByUserID = () => {
        const userID = sessionStorage.getItem('userID');
        console.log('Generating Random AccountID');
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/rand/${userID}`)
            .then(response => {
                console.log('Random AccountID Response: ', response.data);
                const randomAcctID = response.data;
                updateAccountID(randomAcctID);
            })
            .catch(error => {
                console.error('There was an error fetching a random acctID: ', error);
            });
    }


    const fetchAccountNotifications = async (accountID) => {
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/notifications/${accountID}`);
            if(Array.isArray(response.data)){
                console.log('Notification Response: ', response.data);
                setNotificationsByAccount(response.data);
            }else{
                console.error('Expected an array of notifications, but received: ', response.data);
                setNotifications([]);
            }

        }catch(error)
        {
            console.error('Error fetching Account Notifications: ', error);
        }
    }

    useEffect(() => {

    })

    const fetchAccountID = () => {
        const accountCodeID = selectedAccount;

        const currentUserID = sessionStorage.getItem('userID');
        console.log('Selected Account Code from Fetch: ', accountCodeID);
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/${currentUserID}/${accountCodeID}`)
            .then(response => {
                    const accountID = response.data.accountID;
                    console.log('AccountID Response: ', accountID);
                    updateAccountID(accountID)
                    sessionStorage.setItem('AccountID', accountID);
                    console.log('Fetching Account ID: ', accountID);
            })
            .catch(error => {
                console.error('There was an error fetching the accountID: ', error);
            });
    }


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



    useEffect(() => {
        const fetchUsersFullName = async () => {
            let userID = sessionStorage.getItem('userID');
            console.log('UserID fetched: ', userID);
            axios.get(`http://localhost:8080/AeroBankApp/api/users/name/${userID}`)
                .then(response => {
                    setFullName(getFullName(response.data.firstName, response.data.lastName));
                })
                .catch(error => {
                    console.error('There was an error fetching the full name: ', error);
                });
        }
        fetchUsersFullName();
    }, []);

    const getFullName = (first, last) => {
        return first + " " + last;
    }

    const handleNotificationClick = (event) => {
        setAnchorEl(event.currentTarget);

    }


    const handleAccountButtonClick = (accountCodeID) => {
       setSelectedAccount(accountCodeID);
       storeAccountCode(accountCodeID);
       console.log("Selected AccountCode: ", accountCodeID);
    };

    return (
        <Box sx={{ flexGrow: 1, p: 3 }}>
            <Typography variant="h4" gutterBottom>
                {fullName ? `Your Accounts, ${fullName}` : 'Your Accounts'}
            </Typography>
            {isLoading ? (
                <CircularProgress />
            ) : (
                <Grid container spacing={3} direction="column">
                    {accountData.map((account) => {
                        const accountNotifications = notificationsByAccount[account.acctID] || [];
                        return (
                            <Grid item xs={12} key={account.id}>
                                <Account
                                    acctID={account.acctID}
                                    accountCode={account.shortSegment}
                                    acctCodeID={account.acctCodeID}
                                    balance={account.balance}
                                    pending={account.pendingAmount}
                                    accountName={account.accountName}
                                    available={account.availableAmount}
                                    backgroundImageUrl={account.acctImage}
                                    onAccountClick={() => handleAccountButtonClick(account.acctID)}
                                    notificationCount={accountNotifications.length}
                                    notifications={accountNotifications}
                                    onNotificationClick={(event) => handleNotificationClick(event, account.acctID)}
                                    color={account.acctColor}
                                    isSelected={selectedAccount === account.acctID}
                                />
                            </Grid>
                        );
                    })}
                </Grid>
            )}
        </Box>
    );
}