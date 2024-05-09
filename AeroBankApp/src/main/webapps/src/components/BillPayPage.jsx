import React, {useState} from 'react';
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
    Select, FormControlLabel, MenuItem, InputAdornment, Card, CardContent, Switch, Autocomplete, CssBaseline, FormGroup
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



export default function BillPayPage({templates = []}) {
    const [selectedPayee, setSelectedPayee] = useState(null);
    const [selectedDate, setSelectedDate] = useState(new Date());
    const [currentPaymentDetails, setCurrentPaymentDetails] = useState({});
    const [showAddPaymentForm, setShowAddPaymentForm] = useState(false);
    const [newPaymentDetails, setNewPaymentDetails] = useState({
        payeeName: '',
        amount: '',
        dueDate: new Date(),
        accountFrom: ''
    });
   // const [paymentTemplates, setPaymentTemplates] = useState(templates);  // Assuming templates is passed as a prop

    const handleSaveTemplate = (paymentDetails) => {
        console.log("Saving template:", paymentDetails);
        // Assuming saving logic here, for example:
        // setPaymentTemplates([...paymentTemplates, paymentDetails]);
    };

    const payees = [
        { name: "Utility Company", id: 1 },
        { name: "Credit Card Company", id: 2 },
        { name: "Mortgage Bank", id: 3 }
    ];

    const scheduledPayments = [
        { id: 1, payeeName: "Utility Company", amount: 120.75, nextPaymentDue: new Date(2024, 4, 15) },
        { id: 2, payeeName: "Credit Card Company", amount: 250.00, nextPaymentDue: new Date(2024, 4, 22) },
        { id: 3, payeeName: "Mortgage Bank", amount: 1500.00, nextPaymentDue: new Date(2024, 4, 5) }
    ];

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
        if (name === "dueDate") {
            // Convert the input value to a Date object before setting the state
            const dateValue = new Date(value);
            setNewPaymentDetails(prevDetails => ({ ...prevDetails, [name]: dateValue }));
        } else {
            setNewPaymentDetails(prevDetails => ({ ...prevDetails, [name]: value }));
        }
    };

    const submitNewPayment = () => {
        if (newPaymentDetails.dueDate instanceof Date) {
            const isoDateString = newPaymentDetails.dueDate.toISOString();
            console.log('Submitting new payment:', { ...newPaymentDetails, dueDate: isoDateString });
            // Add logic to handle submitting the new payment
        } else {
            console.error('Due date is not a valid Date object.');
        }
    };

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

    return (
        <div style={{
            background: `url(${backgroundImage}) no-repeat center bottom`,
            backgroundSize: 'cover',
            minHeight: 'calc(150vh - 64px)',
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
                                                value={newPaymentDetails.payeeName}
                                                onChange={handleInputChange}
                                                margin="normal"
                                            />
                                            <TextField
                                                fullWidth
                                                label="Amount"
                                                name="amount"
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
                                                value={newPaymentDetails.dueDate.toISOString().substring(0, 10)}
                                                onChange={handleInputChange}
                                                margin="normal"
                                            />
                                            <FormControlLabel
                                                control={<Switch name="recurring" />}
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
                                                    options={payees}
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
                                                    {payees.map(payee => (
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
                                <PaymentGraph data={paymentData} />
                            </Box>
                        </Grid>
                        <Grid item xs={12} sm={4} md={4} lg={3}>
                            <PaymentCalendar scheduledPayments={scheduledPayments} />
                            <BillScheduler payees={scheduledPayments} />
                        </Grid>
                    </Grid>
                </Box>
            </Container>
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
    //         <CssBaseline />
    //         <MenuAppBar />
    //         <Container maxWidth="lg">
    //             <Box my={4} sx={{ background: 'rgba(255, 255, 255, 0.8)', padding: 2, borderRadius: '8px' }}>
    //                 <Grid container spacing={3}>
    //                     <Grid item xs={12} md={8}>
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
    //                                 <Autocomplete
    //                                     options={payees}
    //                                     getOptionLabel={(option) => option.name}
    //                                     style={{ width: 300 }}
    //                                     renderInput={(params) => <TextField {...params} label="Payee" variant="outlined" />}
    //                                     onChange={(event, newValue) => setSelectedPayee(newValue)}
    //                                 />
    //                                 <FormControlLabel
    //                                     control={<Switch name="recurring" />}
    //                                     label="Enable AutoPay"
    //                                 />
    //                                 <Select
    //                                     labelId="select-account-label"
    //                                     id="select-account"
    //                                     value={selectedPayee ? selectedPayee.id : ''}
    //                                     label="Pay from"
    //                                     fullWidth
    //                                     margin="normal"
    //                                     onChange={(e) => setCurrentPaymentDetails({ ...currentPaymentDetails, account: e.target.value })}
    //                                 >
    //                                     {payees.map(payee => (
    //                                         <MenuItem key={payee.id} value={payee.id}>{payee.name}</MenuItem>
    //                                     ))}
    //                                 </Select>
    //                                 <SavePaymentTemplate currentPaymentDetails={currentPaymentDetails} onSaveTemplate={handleSaveTemplate} />
    //                                 <UsePaymentTemplate templates={paymentTemplates} onSelectTemplate={applyTemplate} />
    //                             </CardContent>
    //                         </Card>
    //                         <Box mt={2} md={12}>
    //                             <PaymentGraph data={payees} />
    //                         </Box>
    //                     </Grid>
    //                     <Grid item xs={12} md={4}>
    //                         {/*<PaymentCalendar scheduledPayments={selectedPayee ? selectedPayee.payments : []} />*/}
    //                         <PaymentCalendar scheduledPayments={scheduledPayments}/>
    //                         <BillScheduler payees={scheduledPayments} />
    //                     </Grid>
    //                 </Grid>
    //             </Box>
    //         </Container>
    //     </div>
    // );

}