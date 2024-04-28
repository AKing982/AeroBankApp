import {useEffect, useState} from "react";
import '../TransferView.css';
import {
    Alert,
    Button, Checkbox,
    CircularProgress,
    FormControl,
    FormControlLabel,
    FormLabel,
    InputLabel,
    MenuItem,
    Radio,
    RadioGroup,
    Select,
    Snackbar,
    TextField,
    Typography
} from "@mui/material";
import {Container} from "@mui/system";
import axios from "axios";

export default function TransferView()
{
    let userID = sessionStorage.getItem('userID');
    let currentUser = sessionStorage.getItem('username');
    const [transferType, setTransferType] = useState('ownAccount');
    const [fromAccount, setFromAccount] = useState('');
    const [toAccount, setToAccount] = useState('');
    const [recipientAccount, setRecipientAccount] = useState('');
    const [recipientAccountCode, setRecipientAccountCode] = useState('');
    const [amount, setAmount] = useState('');
    const [transferDate, setTransferDate] = useState('');
    const [transferTime, setTransferTime] = useState('');
    const [description, setDescription] = useState('');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [accountNamesList, setAccountNamesList] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [searchType, setSearchType] = useState('accountNumber');
    const [toUserNames, setToUserNames] = useState([]);
    const [selectedToUserName, setSelectedToUserName] = useState('');
    const [selectedAccountNumber, setSelectedAccountNumber] = useState('');
    const [accountNumberExists, setAccountNumberExists] = useState(false);
    const [selectedToAccountCodeID, setSelectedToAccountCodeID] = useState(0);
    const [accountCodeList, setAccountCodeList] = useState([]);
    const [isLoadingAccountCodes, setIsLoadingAccountCodes] = useState(false);
    const [accountCodeUserList, setAccountCodeUserList] = useState([]);
    const [enableNotifications, setEnableNotifications] = useState(false);

    const TransferType = Object.freeze({
        USER_TO_USER: "USER_TO_USER",
        SAME_USER: "SAME_USER"
    });

    useEffect(() => {
        setIsLoading(true);
        const timeoutId = setTimeout(() => {
            axios.get(`http://localhost:8080/AeroBankApp/api/accounts/list/${userID}`)
                .then(response => {
                    console.log('Account Names List: ', response.data);
                    setAccountNamesList(response.data);
                })
                .catch(error => {
                    console.error('Unable to fetch List of Account Names due to the following: ', error);
                })
                .finally(() => {
                    setIsLoading(false);
                })
        }, 2000);

        return () => clearTimeout(timeoutId);
    }, [userID]);

    useEffect(() => {
        setIsLoading(true);
        const timeoutId = setTimeout(() => {
            axios.get(`http://localhost:8080/AeroBankApp/api/users/user-names-list`)
                .then(response => {
                    console.log('To UserName List: ', response.data);
                    setToUserNames(response.data);
                    if(response.data.length < 0){
                        console.log('No Such Data found in the response.');
                    }
                })
                .catch(error => {
                    console.error('There was an error fetching the list of usernames: ', error);
                })
                .finally(() => {
                    setIsLoading(false);
                })
        }, 2000);
        return () => clearTimeout(timeoutId);
    }, []);

    useEffect(() => {
        if(accountNumberExists){
            fetchToAccountCodesList(selectedAccountNumber);
        }
    }, [accountNumberExists, selectedAccountNumber]);

    const handleTransfer = async () => {
        // Handle the transfer logic

        console.log('Search Type: ', searchType);
        if(searchType === 'accountNumber'){
            validateAccountNumber(selectedAccountNumber);
            validateTransferCriteria(amount, transferDate, transferTime, description);
        }
        if(searchType === 'username'){
            validateSelectedToUserName(selectedToUserName);
            validateTransferCriteria(amount, transferDate, transferTime, description);
        }

        const isUserToUser = transferType === 'otherUser';
        buildAndSendRequestToServer(isUserToUser);
    };

    const parseSelectedAccountCodeToAccountID = (selectedAccountCode) => {
        let pattern =  /XXXX(\d+)$/;
        let matchResult = selectedAccountCode.match(pattern);
        let selectedAcctID = 0;
        if(matchResult){
            let digits = matchResult[1];
            selectedAcctID = digits[digits.length - 1];
            console.log('selectedAcctID: ', selectedAcctID);
            return selectedAcctID;
        }
        return selectedAcctID;
    }

    const buildAndSendRequestToServer = async (type) => {
        let selectedToAccountID = parseSelectedAccountCodeToAccountID(selectedToAccountCodeID);
        if(type){
            if(searchType === 'username'){
                console.log('SelectedToAccountCodeID: ', selectedToAccountCodeID);

                const userToUserWithUserIDRequest = await createUserToUserUserIDTransferRequest(fromAccount, amount, selectedToUserName, selectedToAccountID, description, transferDate, transferTime, TransferType.USER_TO_USER);
                sendTransferRequestToServer(userToUserWithUserIDRequest);
            }
            if(searchType === 'accountNumber'){
                console.log('SelectedToAccountCodeID: ', selectedToAccountCodeID);
                const userToUserRequest =  await createUserToUserTransferRequest(fromAccount, amount, selectedAccountNumber, selectedToAccountID, description, transferDate, transferTime, TransferType.USER_TO_USER);
                sendTransferRequestToServer(userToUserRequest);
            }
        }else{
            const sameUserRequest = await createSameUserTransferRequest(fromAccount, toAccount, amount, description, transferDate, transferTime, TransferType.SAME_USER);
            sendTransferRequestToServer(sameUserRequest);
        }
    };

    const validateSelectedToUserName = (user) => {
        if(!user){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please select a recipient username.');
        }
    };

    const createSameUserTransferRequest = async (fromAccount, toAccount, transferAmount, description, transferDate, transferTime, isUserToUser) => {
        const fromAcctID = await fetchFromAccountID(fromAccount);
        const toAcctID = await fetchFromAccountID(toAccount);

        return {
            fromAccountID: fromAcctID,
            toAccountID: toAcctID,
            fromUserID: userID,
            toUserID: userID,
         //   toUserAccountNumber: '',
        //    toUserAccountCode: '',
            transferAmount: transferAmount,
            transferDate: transferDate,
            transferTime: transferTime,
            transferDescription: description,
            transferType: isUserToUser,
            notificationEnabled: enableNotifications
        }
    };

    const createUserToUserUserIDTransferRequest = async (fromAccount, transferAmount, username, accountCodeID, description, transferDate, transferTime, isUserToUser) => {
        const toUserID = await fetchToUserID(username);
        const fromAcctID = await fetchFromAccountID(fromAccount);
        const toAcctID = await fetchAccountIDByAccountCodeAndUserID(accountCodeID, toUserID);

        return {
            fromAccountID: fromAcctID,
            toAccountID: toAcctID,
            fromUserID: userID,
            toUserID: toUserID,
           // toUserAccountNumber: '',
           // toUserAccountCode: accountCode,
            transferAmount: transferAmount,
            transferDescription: description,
            transferDate: transferDate,
            transferTime: transferTime,
            transferType: isUserToUser,
            notificationEnabled: enableNotifications
        }
    };

    const createUserToUserTransferRequest = async (fromAccount, transferAmount, accountNumber, accountCodeID, description, transferDate, transferTime, isUserToUser) => {

        const accountID = await fetchFromAccountID(fromAccount);
        const toUserID = await fetchToUserIDByAccountNumber(accountNumber);
        const toAcctID = await fetchAccountIDByAccountCodeAndUserID(accountCodeID, toUserID);

        return {
            fromAccountID: accountID,
            toAccountID: toAcctID,
            fromUserID: userID,
            toUserID: toUserID,
            transferAmount: transferAmount,
         //   toUserAccountNumber: accountNumber,
           // toUserAccountCode: accountCode,
            transferDescription: description,
            transferDate: transferDate,
            transferTime: transferTime,
            transferType: isUserToUser,
            notificationEnabled: enableNotifications
        };
    };

    const validateAccountNumber = (accountNumber) => {
        if(!accountNumber){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please select a recipient account number');
        }
    };

    const validateTransferCriteria = (amount, transferDate, transferTime, description) => {
        if(!amount){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please enter an amount.');
        }

        if(!transferDate){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please choose a transfer date.');
        }
        if(!transferTime){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please choose a transfer time.');
        }
        if(!description){
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('Please enter a valid description.');
        }
    };

    const fetchAccountIDByAccountCodeAndUserID = async (acctCodeID, userID) => {
        if(!acctCodeID || !userID){
            return;
        }
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/${userID}/${acctCodeID}`)
            if(response.status === 200){
                return response.data.accountID;
            }
        }catch(error){
            console.error('There was an error fetching the accountID: ', error);
        }
    };

    const fetchToUserIDByAccountNumber = async (accountNumber) => {
        if(!accountNumber){
            return;
        }
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/id-num/${accountNumber}`)
            if(response.status === 200){
                return response.data;
            }
        }catch(error){
            console.error('There was an error fetching the userID: ', error);
        }
    }

    const fetchToUserID = async (username) => {
        if(!username){
            return;
        }
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/id/${username}`)
            if(response.status === 200 || response.status === 201){
                return response.data;
            }
        }catch(error){
            console.error('There was an error fetching the userID: ', error);
        }
    };

    const fetchAccountCodesUserNameList = async (username) => {
        if(!username){
            return;
        }
        setIsLoadingAccountCodes(true);
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/codes/${username}`, {
                timeout: 4000
            });
            console.log('Account Codes UserName List: ', response.data);
            if(Array.isArray(response.data) && response.data.length > 0){
                setAccountCodeUserList(response.data);
            }
        }catch(error){
            console.error('There was an error fetching Account Codes by username: ', error);
        }finally{
            setIsLoadingAccountCodes(false);
        }
    };

    const fetchFromAccountID = async (accountName) => {
        console.log('Fetching Account ID for account Name: ', accountName);
        if(!accountName){
            return;
        }
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/id/${accountName}`)
            if(response.status === 200 || response.status === 201){
                console.log('FromAccount response: ', response.data);
                return response.data.accountID;
            }
        }catch(error){
            console.error('Unable to fetch accountID for accountName due to the following error: ', error);
        }
    }

    const fetchToAccountCodesList = async (accountNumber) => {
        if(!accountNumber){
            return;
        }
        setIsLoadingAccountCodes(true);
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/codes/${accountNumber}`, {
                timeout: 4000
            });
            console.log('ToAccountCodeID List: ', response.data);
            if(Array.isArray(response.data) && response.data.length > 0){
                setAccountCodeList(response.data);
            }

        }catch(error){
            console.error('There was an error fetching Account Codes: ', error);
        }finally{
            setIsLoadingAccountCodes(false);
        }
    }

    const fetchAccountNumberExists = async (accountNumber) => {
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/exists/${accountNumber}`)
            console.log('Does Account Number Exist: ', response.data);
            if(response.data.exists)
            {
                setAccountNumberExists(true);
                setOpenSnackbar(true);
                setSnackBarSeverity('success');
                setSnackBarMessage('An Account Number Exists');
            }else{
                setAccountNumberExists(false);
                setOpenSnackbar(true);
                setSnackBarSeverity('error');
                setSnackBarMessage('Account Number not found.');
            }

        }catch(error){
            console.error('There was an error determining if account number exists: ', error);
        }
    }

    const sendTransferRequestToServer = async (request) => {
        console.log('Saving Request: ', request);
        try {
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/transfers/save`, request)
            if(response.status === 200 || response.status === 201){
                console.log('Transfer Successful: ', response.data);
                setOpenSnackbar(true);
                setSnackBarSeverity('success');
                setSnackBarMessage('Transfer was successfully saved.');
            }else{
                throw new Error(`Unexpected status response: ${response.status}`);
            }
        }catch(error){
            console.error('There was an error saving your Transfer: ', error);
            setOpenSnackbar(true);
            setSnackBarSeverity('error');
            setSnackBarMessage('There was an error saving the Transfer. Please try again...');
            if (error.response) {
                // Server responded with a status code that falls out of the range of 2xx
                console.log(error.response.data);
                console.log(error.response.status);
                console.log(error.response.headers);
            } else if (error.request) {
                // Request was made but no response was received
                console.log(error.request);
            } else {
                // An error occurred in setting up the request
                console.log('Error', error.message);
            }
        }
    }

    useEffect(() => {
        if(searchType === 'accountNumber' && selectedAccountNumber.trim()){
            fetchAccountNumberExists(selectedAccountNumber);
        }
    }, [selectedAccountNumber, searchType]);

    useEffect(() => {
        if(searchType === 'username' && selectedToUserName.trim()){
            fetchAccountCodesUserNameList(selectedToUserName);
        }
    }, [selectedToUserName, searchType]);


    function snackBar(isOpen, errorMessage, message){
        setOpenSnackbar(isOpen);
        setSnackBarSeverity(errorMessage);
        setSnackBarMessage(message);
    }

    const handleCloseSnackbar = () => {
        setOpenSnackbar(false);
    };

    const handleKeyPressOnAccountNumber = (event) => {
        if (event.key === 'Enter') {
            event.preventDefault(); // Prevent form submission if form logic is tied to Enter key
            console.log('Enter clicked');
            fetchAccountNumberExists(selectedAccountNumber); // Trigger your function to check account existence
        }
    };

    return (
        <Container style={{ marginTop: '20px' }}>
            <Typography variant="h4">Make a Transfer</Typography>
            <form style={{ marginTop: '20px' }}>
                <FormControl component="fieldset" style={{ marginBottom: '20px' }}>
                    <FormLabel component="legend">Transfer Type</FormLabel>
                    <RadioGroup
                        row
                        aria-label="transferType"
                        name="row-radio-buttons-group"
                        value={transferType}
                        onChange={(e) => setTransferType(e.target.value)}
                    >
                        <FormControlLabel value="ownAccount" control={<Radio />} label="To Own Account" />
                        <FormControlLabel value="otherUser" control={<Radio />} label="To Another User" />
                    </RadioGroup>
                </FormControl>

                <FormControl fullWidth style={{ marginBottom: '20px' }}>
                    <InputLabel>From Account</InputLabel>
                    <Select
                        value={fromAccount}
                        onChange={(e) => setFromAccount(e.target.value)}
                        displayEmpty
                        renderValue={selected => {
                            if (selected.length === 0) {
                                return <em>Please wait while we load your accounts...</em>;
                            }
                            return selected;
                        }}
                    >
                        {isLoading ? (
                            <MenuItem disabled value="">
                                <CircularProgress size={24} />
                            </MenuItem>
                        ) : (
                            accountNamesList.map((account) => (
                                <MenuItem key={account.id} value={account.accountName}>
                                    {account.accountName}
                                </MenuItem>
                            ))
                        )}
                    </Select>
                </FormControl>

                {transferType === 'ownAccount' ? (
                    <FormControl fullWidth style={{ marginBottom: '20px' }}>
                        <InputLabel>To Account</InputLabel>
                        <Select
                            value={toAccount}
                            onChange={(e) => setToAccount(e.target.value)}
                            displayEmpty
                            renderValue={selected => {
                                if (selected.length === 0) {
                                    return <em>Please wait while we load your accounts...</em>;
                                }
                                return selected;
                            }}
                        >
                            {isLoading ? (
                                <MenuItem disabled value="">
                                    <CircularProgress size={24} />
                                </MenuItem>
                            ) : (
                                accountNamesList.filter(account => account.accountName !== fromAccount).map((account) => (
                                    <MenuItem key={account.id} value={account.accountName}>
                                        {account.accountName}
                                    </MenuItem>
                                ))
                            )}
                        </Select>
                    </FormControl>
                ) : (transferType === 'otherUser' && (
                    <>
                        <FormControl component="fieldset" style={{ marginBottom: '20px' }}>
                            <FormLabel component="legend">Search Recipient By</FormLabel>
                            <RadioGroup
                                row
                                aria-label="searchType"
                                name="searchTypeRadioGroup"
                                value={searchType}
                                onChange={(e) => {setSearchType(e.target.value);
                                    if(e.target.value === 'username'){
                                        setSelectedAccountNumber('');
                                        setSelectedToAccountCodeID(0);
                                }else{
                                        setSelectedToUserName('');
                                        setAccountCodeList([]);
                                }
                            }}
                            >
                                <FormControlLabel value="accountNumber" control={<Radio />} label="Account Number" />
                                <FormControlLabel value="username" control={<Radio />} label="Username" />
                            </RadioGroup>
                        </FormControl>

                        {searchType === 'username' ? (
                            <>
                                <FormControl fullWidth style={{ marginBottom: '20px' }}>
                                    <InputLabel>Recipient's Username</InputLabel>
                                    <Select
                                        value={selectedToUserName}
                                        onChange={(e) => setSelectedToUserName(e.target.value)}
                                        displayEmpty
                                    >
                                        {toUserNames.filter(user => user.username !== currentUser).map((user) => (
                                            <MenuItem key={user.id} value={user.username}>
                                                {user.username}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                                {selectedToUserName && (
                                    <FormControl fullWidth style={{ marginBottom: '20px' }}>
                                        <InputLabel>Recipient's Account Codes</InputLabel>
                                        <Select
                                            value={selectedToAccountCodeID}
                                            onChange={(e) => setSelectedToAccountCodeID(e.target.value)}
                                            displayEmpty
                                        >
                                            {isLoadingAccountCodes ? (
                                                <MenuItem value="" disabled>
                                                    <CircularProgress size={24} />
                                                </MenuItem>
                                            ) : (
                                                accountCodeUserList.map((code) => (
                                                    <MenuItem key={code.id} value={code.accountCode}>
                                                        {code.accountCode}
                                                    </MenuItem>
                                                ))
                                            )}
                                        </Select>
                                    </FormControl>
                                )}
                            </>
                        ) : (
                            <>
                                <TextField
                                    fullWidth
                                    multiline
                                    rows={1}
                                    label="Recipient's Account Number"
                                    type="text"
                                    value={selectedAccountNumber}
                                    onChange={(e) => setSelectedAccountNumber(e.target.value)}
                                    onKeyDown={handleKeyPressOnAccountNumber}
                                    style={{ marginBottom: '20px' }}
                                />
                                {accountNumberExists && selectedAccountNumber ? (
                                    <FormControl fullWidth style={{ marginBottom: '20px' }}>
                                        <InputLabel>Recipient's Account Codes</InputLabel>
                                        <Select
                                            value={selectedToAccountCodeID}
                                            onChange={(e) => setSelectedToAccountCodeID(e.target.value)}
                                            displayEmpty
                                        >
                                            {isLoadingAccountCodes ? (
                                                <MenuItem value="" disabled>
                                                    <CircularProgress size={24} />
                                                </MenuItem>
                                            ) : (
                                                accountCodeList.map((code) => (
                                                    <MenuItem key={code.id} value={code.accountCode}>
                                                        {code.accountCode}
                                                    </MenuItem>
                                                ))
                                            )}
                                        </Select>
                                    </FormControl>
                                ) : null}
                            </>
                        )}
                    </>
                ))}
                <TextField
                    fullWidth
                    label="Amount"
                    type="number"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <TextField
                    fullWidth
                    label="Transfer Date"
                    type="date"
                    InputLabelProps={{ shrink: true }}
                    value={transferDate}
                    onChange={(e) => setTransferDate(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <TextField
                    fullWidth
                    type="time"
                    label="Transfer Time"
                    value={transferTime}
                    onChange={(e) => setTransferTime(e.target.value)}
                    style={{ marginBottom: '20px' }}
                />

                <TextField
                    fullWidth
                    label="Description"
                    multiline
                    rows={4}
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    style={{ marginBottom: '20px' }}
                        />

                <FormControlLabel
                    control={
                        <Checkbox
                            checked={enableNotifications}
                            onChange={(e) => setEnableNotifications(e.target.checked)}
                            color="primary"
                        />
                    }
                    label="Notify me when this transfer is completed"
                    style={{ marginBottom: '20px' }}
                />
                <Button variant="contained" color="primary" onClick={handleTransfer}>
                    Submit Transfer
                </Button>
            </form>

            <Snackbar
                open={openSnackbar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="Transfer successful"
            >
                <Alert onClose={handleCloseSnackbar} severity={snackBarSeverity} variant="filled" sx={{width: '100%'}}>
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </Container>
    );
}