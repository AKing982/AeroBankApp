import React, {useEffect, useState} from 'react';
import {
    Container,
    Box,
    Grid,
    Paper,
    Typography,
    Button,
    TextField,
    Accordion,
    AccordionSummary,
    AccordionDetails,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    Select,
    FormControlLabel,
    MenuItem,
    InputAdornment,
    Card,
    CardContent,
    Switch,
    Autocomplete,
    CssBaseline,
    FormGroup,
    Collapse, Radio, FormControl, FormLabel, RadioGroup
} from '@mui/material';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';
import BillPaySchedule from "./BillPaySchedule";
import Home from "./Home";
import GradientSeparator from "./GradientSeparator";
import MenuAppBar from "./MenuAppBar";
import BillScheduler from "./BillScheduler";
import PaymentGraph from "./PaymentGraph";
import InfoIcon from "@mui/icons-material/Info";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import PaymentCalendar from "./PaymentCalendar";
import SavePaymentTemplate from "./SavePaymentTemplate";
import UsePaymentTemplate from "./UsePaymentTemplate";
import axios from "axios";
import PaymentNotification from "./PaymentNotification";
import PaymentHistory from "./PaymentHistory";
import { toast } from 'react-toastify';
import NewPaymentDialog from "./NewPaymentDialog";



export default function BillPayPage({templates = []}) {
    const [selectedPayee, setSelectedPayee] = useState(null);
    const [selectedDate, setSelectedDate] = useState(new Date().toISOString().substring(0, 10));
    const [currentPaymentDetails, setCurrentPaymentDetails] = useState({});
    const [showAddPaymentForm, setShowAddPaymentForm] = useState(false);
    const [newPaymentDetails, setNewPaymentDetails] = useState({
        payeeName: '',
        amount: '',
        cardNumber: '',
        fromAccount: '',
        cardExpiry: '',
        cardCVV: '',
        dueDate: new Date().toISOString().substring(0, 10) ,
        accountFrom: ''
    });
    const [isLoading, setIsLoading] = useState(false);
    const [payeeList, setPayeeList] = useState({});
    const [scheduledPayments, setScheduledPayments] = useState([]);
    const [isAutoPayEnabled, setIsAutoPayEnabled] = useState(false);
    const [paymentHistory, setPaymentHistory] = useState([]);
    const [isPaymentHistoryOpen, setIsPaymentHistoryOpen] = useState(false);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [content, setContent] = useState('');
    const [accountCodeList, setAccountCodeList] = useState([]);
    const [paymentMethod, setPaymentMethod] = useState('account'); // Default to account


    let userID = sessionStorage.getItem('userID');
   // const [paymentTemplates, setPaymentTemplates] = useState(templates);  // Assuming templates is passed as a prop

    const handleSaveTemplate = (paymentDetails) => {
        console.log("Saving template:", paymentDetails);
        // Assuming saving logic here, for example:
        // setPaymentTemplates([...paymentTemplates, paymentDetails]);
    };

    const handleAutoPayChange = (event) => {
        setIsAutoPayEnabled(event.target.checked);
    };

    const payees = [
        { name: "Utility Company", id: 1 },
        { name: "Credit Card Company", id: 2 },
        { name: "Mortgage Bank", id: 3 }
    ];

    // const scheduledPayments = [
    //     { id: 1, payeeName: "Utility Company", amount: 120.75, nextPaymentDue: new Date(2024, 4, 15) },
    //     { id: 2, payeeName: "Credit Card Company", amount: 250.00, nextPaymentDue: new Date(2024, 4, 22) },
    //     { id: 3, payeeName: "Mortgage Bank", amount: 1500.00, nextPaymentDue: new Date(2024, 4, 5) }
    // ];

    const paymentTemplates = [
        { id: 1, templateName: "Monthly Utility Payment", payeeName: "Utility Company", accountFrom: "Checking Account 2242", amount: 120.75, recurring: true },
        { id: 2, templateName: "Credit Card Payment", payeeName: "Credit Card Company", accountFrom: "Checking Account 2242", amount: 250.00, recurring: true }
    ];


    const applyTemplate = (template) => {
        console.log("Applying template:", template);
        setSelectedPayee(template.payeeName);
        setCurrentPaymentDetails(template);
    };

    const toggleAddPaymentForm = () => setShowAddPaymentForm(!showAddPaymentForm);

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setNewPaymentDetails(prevDetails => ({
            ...prevDetails,
            [name]: value  // Directly set the string value for the date
        }));
    };

    const handleDateChange = (event) => {
        const { value } = event.target;
        console.log('Selected date string:', value);  // Debugging
        setSelectedDate(value);  // Directly set the string value
    };

    const buildPaymentRequest = () => {
         return{
            payeeName: newPaymentDetails.payeeName,
            amount: newPaymentDetails.amount,
            dueDate: selectedDate,
            autoPayEnabled: isAutoPayEnabled
         }
     }

    const sendNewPaymentRequest = async (request) => {
         console.log('Request: ', request);
        try{
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/bills/save`, request);
            if(response.status === 200 || response.status === 201){
                toast.success('Payment successfully scheduled.');
            }
        }catch(error){
            console.error('There was an error sending the request to the server: ', error);
        }
    }

    useEffect(() => {
        setIsLoading(true);
        console.log('Fetching Bill Payee List');

        axios.get(`http://localhost:8080/AeroBankApp/api/bills/${userID}/list`)
            .then(response => {
                if(Array.isArray(response.data) && response.data.length > 0){
                    console.log('Bill Payee List: ', response.data);
                    setPayeeList(response.data);
                }
            })
            .catch(error => {
                console.error('There was an error fetching the list of Bill Payees: ', error);
            })
            .finally(() => {
                setIsLoading(false);
        })
    }, []);

    useEffect(() => {
        setIsLoading(true);

        axios.get(`http://localhost:8080/AeroBankApp/api/bills/${userID}/schedules`)
            .then(response => {
                if(Array.isArray(response.data) && response.data.length > 0){
                    console.log('Scheduled Payments: ', response.data);
                    setScheduledPayments(response.data);
                }

            })
            .catch(error => {
                console.error('An error occurred fetching scheduled payments: ', error);
            })
            .finally(() => {
                setIsLoading(false);
            })
    }, []);


    useEffect(() => {
        setIsLoading(true);
        axios.get(`http://localhost:8080/AeroBankApp/api/bills/${userID}/history`)
            .then(response => {
                 console.log('Payment History: ', response.data);
                 if(Array.isArray(response.data) && response.data.length > 0){
                     setPaymentHistory(response.data)
                 }
            })
            .catch(error => {
                console.error('There was an error fetching user payment history: ', error);
            })
            .finally(() => {
                setIsLoading(false);
            })

    }, []);

    const submitNewPayment = () => {
        const paymentRequest = buildPaymentRequest();
        if(validateNewPaymentCriteria()){
            sendNewPaymentRequest(paymentRequest);
            // Add logic to handle submitting the new payment
            toggleAddPaymentForm();
        }
    };

    const handleDialogClose = () => {
        setIsDialogOpen(false);
    };

    const validateNewPaymentCriteria = () => {

        if(!newPaymentDetails.payeeName){
            setIsDialogOpen(true);
            setContent('Please enter a Payee Name.');
            return false;
        }
        if(!newPaymentDetails.amount){
            setIsDialogOpen(true);
            setContent('Please specify a payment amount...');
            return false;
        }
        if(!newPaymentDetails.dueDate){
            setIsDialogOpen(true);
            setContent('Please specify a due date for the payment...');
            return false;
        }
        if (paymentMethod === 'account' && !newPaymentDetails.accountFrom) {
            setContent('Please select an account to pull funds from.');
            setIsDialogOpen(true);
            return false;
        }
        if (paymentMethod === 'card' && (!newPaymentDetails.cardNumber || !newPaymentDetails.cardExpiry || !newPaymentDetails.cardCVV)) {
            setContent('Please enter your card details.');
            setIsDialogOpen(true);
            return false;
        }
        if (paymentMethod === 'wallet' && !newPaymentDetails.walletProvider) {
            setContent('Please select a digital wallet.');
            setIsDialogOpen(true);
            return false;
        }
        return true;
    }

    const paymentData = [
        {
            id: 1,
            name: 'Utility Company',
            lastPaid: '04/15/2024',
            nextPaymentDue: '05/15/2024',
            amount: 120.50
        },
        {
            id: 2,
            name: 'Credit Card Company',
            lastPaid: '04/12/2024',
            nextPaymentDue: '05/12/2024',
            amount: 200.00
        },
        {
            id: 3,
            name: 'Mortgage Bank',
            lastPaid: '04/05/2024',
            nextPaymentDue: '05/05/2024',
            amount: 1500.00
        },
        {
            id: 4,
            name: 'Cable Provider',
            lastPaid: '04/20/2024',
            nextPaymentDue: '05/20/2024',
            amount: 99.99
        },
        {
            id: 5,
            name: 'Mobile Phone Service',
            lastPaid: '04/22/2024',
            nextPaymentDue: '05/22/2024',
            amount: 75.00
        }
    ];

    const togglePaymentHistory = () => {
        setIsPaymentHistoryOpen(!isPaymentHistoryOpen);
    };

    return (
        <div style={{
            background: `url(${backgroundImage}) no-repeat center bottom`,
            backgroundSize: 'cover',
             minHeight: 'calc(200vh - 64px)',
            width: '100%',
            position: 'relative',
        }}>
            <CssBaseline />
            <MenuAppBar />
            <Container maxWidth="lg">
                <Box my={4} sx={{ background: 'rgba(255, 255, 255, 0.8)', padding: 2, borderRadius: '8px' }}>
                    <Grid container spacing={3}>
                        <Grid item xs={12} sm={8} md={8} lg={9} >
                            <Card raised>
                                <CardContent>
                                    <Typography variant="h6" gutterBottom>
                                        Payment Schedule
                                    </Typography>
                                    <IconButton aria-label="info" size="small" sx={{ float: 'right' }}>
                                        <InfoIcon fontSize="inherit" />
                                    </IconButton>
                                    <Typography variant="body2" gutterBottom>
                                        Our goal is to deliver your payment securely and quickly.
                                    </Typography>
                                    <Typography variant="body2" color="textSecondary">
                                        Payments may be processed using a single-use card.
                                    </Typography>
                                    <Button onClick={toggleAddPaymentForm} variant="outlined" sx={{ mb: 2 }}>
                                        {showAddPaymentForm ? 'Close Add Payment' : 'Add New Payment'}
                                    </Button>
                                    {showAddPaymentForm ? (
                                        <FormGroup>
                                            <TextField
                                                fullWidth
                                                label="Payee Name"
                                                name="payeeName"
                                                multiline
                                                rows={1}
                                                value={newPaymentDetails.payeeName}
                                                onChange={handleInputChange}
                                                margin="normal"
                                            />
                                            <FormControl component="fieldset" margin="normal">
                                                <FormLabel component="legend">Payment Method</FormLabel>
                                                <RadioGroup
                                                    row
                                                    aria-label="payment-method"
                                                    name="paymentMethod"
                                                    value={paymentMethod}
                                                    onChange={(e) => setPaymentMethod(e.target.value)}
                                                >
                                                    <FormControlLabel value="account" control={<Radio />} label="Bank Account" />
                                                    <FormControlLabel value="card" control={<Radio />} label="Credit/Debit Card" />
                                                    <FormControlLabel value="wallet" control={<Radio />} label="Digital Wallet" />
                                                </RadioGroup>
                                            </FormControl>

                                            {paymentMethod === 'account' && (
                                                <Autocomplete
                                                    options={accountCodeList}
                                                    getOptionLabel={(option) => option.name}
                                                    style={{ width: '100%' }}
                                                    renderInput={(params) => <TextField {...params} label="Pay from" variant="outlined" margin="normal" />}
                                                    onChange={(event, newValue) => setNewPaymentDetails({ ...newPaymentDetails, accountFrom: newValue.id })}
                                                />
                                            )}

                                            {paymentMethod === 'card' && (
                                                <>
                                                    <TextField
                                                        fullWidth
                                                        label="Card Number"
                                                        name="cardNumber"
                                                        value={newPaymentDetails.cardNumber}
                                                        onChange={handleInputChange}
                                                        margin="normal"
                                                    />
                                                    <TextField
                                                        fullWidth
                                                        label="Expiry Date"
                                                        name="cardExpiry"
                                                        placeholder="MM/YY"
                                                        value={newPaymentDetails.cardExpiry}
                                                        onChange={handleInputChange}
                                                        margin="normal"
                                                    />
                                                    <TextField
                                                        fullWidth
                                                        label="CVV"
                                                        name="cardCVV"
                                                        value={newPaymentDetails.cardCVV}
                                                        onChange={handleInputChange}
                                                        margin="normal"
                                                    />
                                                </>
                                            )}

                                            {paymentMethod === 'wallet' && (
                                                <Select
                                                    fullWidth
                                                    label="Select Wallet"
                                                    name="walletProvider"
                                                    value={newPaymentDetails.walletProvider}
                                                    onChange={handleInputChange}
                                                    margin="normal"
                                                >
                                                    <MenuItem value="paypal">PayPal</MenuItem>
                                                    <MenuItem value="googlepay">Google Pay</MenuItem>
                                                    <MenuItem value="applepay">Apple Pay</MenuItem>
                                                </Select>
                                            )}
                                            <TextField
                                                fullWidth
                                                label="Amount"
                                                name="amount"
                                                multiline
                                                rows={1}
                                                value={newPaymentDetails.amount}
                                                onChange={handleInputChange}
                                                margin="normal"
                                            />
                                            <TextField
                                                fullWidth
                                                type="date"
                                                label="Due Date"
                                                name="dueDate"
                                                InputLabelProps={{ shrink: true }}
                                                value={selectedDate}
                                                onChange={handleDateChange}
                                                margin="normal"
                                            />
                                            <FormControlLabel
                                                control={<Switch name="recurring"
                                                                 checked={isAutoPayEnabled}
                                                                 onChange={handleAutoPayChange}
                                                />}
                                                label="Enable AutoPay"
                                            />
                                            <Select
                                                fullWidth
                                                label="Pay from"
                                                name="accountFrom"
                                                value={newPaymentDetails.accountFrom}
                                                onChange={handleInputChange}
                                                margin="normal"
                                            >
                                                {payees.map((payee) => (
                                                    <MenuItem key={payee.id} value={payee.id}>{payee.name}</MenuItem>
                                                ))}
                                            </Select>
                                            <Button onClick={submitNewPayment} variant="contained" color="primary">
                                                Submit Payment
                                            </Button>
                                        </FormGroup>
                                    ) : (
                                        scheduledPayments.length > 0 && (
                                            <>
                                                <Autocomplete
                                                    options={payeeList}
                                                    getOptionLabel={(option) => option.name}
                                                    style={{ width: 300 }}
                                                    renderInput={(params) => <TextField {...params} label="Payee" variant="outlined" />}
                                                    onChange={(event, newValue) => setSelectedPayee(newValue)}
                                                />
                                                <FormControlLabel
                                                    control={<Switch name="recurring" />}
                                                    label="Enable AutoPay"
                                                />
                                                <Select
                                                    labelId="select-account-label"
                                                    id="select-account"
                                                    value={selectedPayee ? selectedPayee.id : ''}
                                                    label="Pay from"
                                                    fullWidth
                                                    margin="normal"
                                                    onChange={(e) => setNewPaymentDetails({ ...newPaymentDetails, account: e.target.value })}
                                                >
                                                    {payeeList.map(payee => (
                                                        <MenuItem key={payee.id} value={payee.id}>{payee.name}</MenuItem>
                                                    ))}
                                                </Select>
                                                <SavePaymentTemplate currentPaymentDetails={newPaymentDetails} onSaveTemplate={() => console.log('Template Saved')} />
                                                <UsePaymentTemplate templates={templates} onSelectTemplate={(template) => console.log('Template Applied', template)} />
                                            </>
                                        )
                                    )}
                                </CardContent>
                            </Card>
                            <Box mt={2}>
                                <PaymentGraph data={scheduledPayments} />
                            </Box>
                            <Box mt={2}>
                                <Button onClick={togglePaymentHistory} variant="outlined">
                                    {isPaymentHistoryOpen ? 'Hide Payment History' : 'Show Payment History'}
                                </Button>
                                <Collapse in={isPaymentHistoryOpen}>
                                    <PaymentHistory paymentHistory={paymentHistory} />
                                </Collapse>
                            </Box>
                        </Grid>
                        <Grid item xs={12} sm={4} md={4} lg={3}>
                            <PaymentCalendar scheduledPayments={scheduledPayments} />
                            <BillScheduler payees={scheduledPayments} />
                            {/*<NotificationAlerts notifications={notifications} />*/}
                        </Grid>
                    </Grid>
                </Box>
            </Container>
            <NewPaymentDialog open={isDialogOpen} handleClose={handleDialogClose} content={content} />
        </div>
    );
    // return (
    //     <div style={{
    //         background: `url(${backgroundImage}) no-repeat center bottom`,
    //         backgroundSize: 'cover',
    //         minHeight: 'calc(150vh - 64px)',
    //         width: '100%',
    //         position: 'relative',
    //     }}>
    //         <CssBaseline />
    //         <MenuAppBar />
    //         <Container maxWidth="lg">
    //             <Box my={4} sx={{ background: 'rgba(255, 255, 255, 0.8)', padding: 2, borderRadius: '8px' }}>
    //                 <Grid container spacing={3}>
    //                     <Grid item xs={12} sm={8} md={8} lg={9} >
    //                         <Card raised>
    //                             <CardContent>
    //                                 <Typography variant="h6" gutterBottom>
    //                                     Payment Schedule
    //                                 </Typography>
    //                                 <IconButton aria-label="info" size="small" sx={{ float: 'right' }}>
    //                                     <InfoIcon fontSize="inherit" />
    //                                 </IconButton>
    //                                 <Typography variant="body2" gutterBottom>
    //                                     Our goal is to deliver your payment securely and quickly.
    //                                 </Typography>
    //                                 <Typography variant="body2" color="textSecondary">
    //                                     Payments may be processed using a single-use card.
    //                                 </Typography>
    //                                 <Button onClick={toggleAddPaymentForm} variant="outlined" sx={{ mb: 2 }}>
    //                                     {showAddPaymentForm ? 'Close Add Payment' : 'Add New Payment'}
    //                                 </Button>
    //                                 {showAddPaymentForm ? (
    //                                     <FormGroup>
    //                                         <TextField
    //                                             fullWidth
    //                                             label="Payee Name"
    //                                             name="payeeName"
    //                                             multiline
    //                                             rows={1}
    //                                             value={newPaymentDetails.payeeName}
    //                                             onChange={handleInputChange}
    //                                             margin="normal"
    //                                         />
    //                                         <TextField
    //                                             fullWidth
    //                                             label="Amount"
    //                                             name="amount"
    //                                             multiline
    //                                             rows={1}
    //                                             value={newPaymentDetails.amount}
    //                                             onChange={handleInputChange}
    //                                             margin="normal"
    //                                         />
    //                                         <TextField
    //                                             fullWidth
    //                                             type="date"
    //                                             label="Due Date"
    //                                             name="dueDate"
    //                                             InputLabelProps={{ shrink: true }}
    //                                             value={selectedDate}
    //                                             onChange={handleDateChange}
    //                                             margin="normal"
    //                                         />
    //                                         <FormControlLabel
    //                                             control={<Switch name="recurring"
    //                                                              checked={isAutoPayEnabled}
    //                                                              onChange={handleAutoPayChange}
    //                                             />}
    //                                             label="Enable AutoPay"
    //                                         />
    //                                         <Select
    //                                             fullWidth
    //                                             label="Pay from"
    //                                             name="accountFrom"
    //                                             value={newPaymentDetails.accountFrom}
    //                                             onChange={handleInputChange}
    //                                             margin="normal"
    //                                         >
    //                                             {payees.map((payee) => (
    //                                                 <MenuItem key={payee.id} value={payee.id}>{payee.name}</MenuItem>
    //                                             ))}
    //                                         </Select>
    //                                         <Button onClick={submitNewPayment} variant="contained" color="primary">
    //                                             Submit Payment
    //                                         </Button>
    //                                     </FormGroup>
    //                                 ) : (
    //                                     scheduledPayments.length > 0 && (
    //                                         <>
    //                                             <Autocomplete
    //                                                 options={payeeList}
    //                                                 getOptionLabel={(option) => option.name}
    //                                                 style={{ width: 300 }}
    //                                                 renderInput={(params) => <TextField {...params} label="Payee" variant="outlined" />}
    //                                                 onChange={(event, newValue) => setSelectedPayee(newValue)}
    //                                             />
    //                                             <FormControlLabel
    //                                                 control={<Switch name="recurring" />}
    //                                                 label="Enable AutoPay"
    //                                             />
    //                                             <Select
    //                                                 labelId="select-account-label"
    //                                                 id="select-account"
    //                                                 value={selectedPayee ? selectedPayee.id : ''}
    //                                                 label="Pay from"
    //                                                 fullWidth
    //                                                 margin="normal"
    //                                                 onChange={(e) => setNewPaymentDetails({ ...newPaymentDetails, account: e.target.value })}
    //                                             >
    //                                                 {payeeList.map(payee => (
    //                                                     <MenuItem key={payee.id} value={payee.id}>{payee.name}</MenuItem>
    //                                                 ))}
    //                                             </Select>
    //                                             <SavePaymentTemplate currentPaymentDetails={newPaymentDetails} onSaveTemplate={() => console.log('Template Saved')} />
    //                                             <UsePaymentTemplate templates={templates} onSelectTemplate={(template) => console.log('Template Applied', template)} />
    //                                         </>
    //                                     )
    //                                 )}
    //                             </CardContent>
    //                         </Card>
    //                         <Box mt={2}>
    //                             <PaymentGraph data={scheduledPayments} />
    //                         </Box>
    //                         <Box mt={2}>
    //                             <PaymentHistory paymentHistory={paymentHistory} />
    //                         </Box>
    //                     </Grid>
    //                     <Grid item xs={12} sm={4} md={4} lg={3}>
    //                         <PaymentCalendar scheduledPayments={scheduledPayments} />
    //                         <BillScheduler payees={scheduledPayments} />
    //                     </Grid>
    //                 </Grid>
    //             </Box>
    //         </Container>
    //     </div>
    // );
    //
    //

}