import {useState} from "react";
import {LocalizationProvider} from "@mui/x-date-pickers";
import {StaticDatePicker} from "@mui/lab";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {TextField} from "@mui/material";
import {Box} from "@mui/system";

function PaymentCalendar({scheduledPayments}){
    const [selectedDate, setSelectedDate] = useState(new Date());

    // Filter scheduled payments based on the selected date
    const paymentsOnSelectedDate = scheduledPayments.filter(payment =>
        new Date(payment.paymentDueDate).toDateString() === selectedDate.toDateString()
    );

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <Box sx={{ mb: 4, width: 300 }}>
                <StaticDatePicker
                    displayStaticWrapperAs="desktop"
                    value={selectedDate}
                    onChange={(newValue) => {
                        setSelectedDate(newValue);
                    }}
                    renderInput={(params) => <TextField {...params} />}
                    shouldDisableDate={(date) => {
                        // Disable dates that have no scheduled payments
                        return !scheduledPayments.some(payment =>
                            new Date(payment.nextPayment).toDateString() === date.toDateString()
                        );
                    }}
                />
                <div>
                    {paymentsOnSelectedDate.map(payment => (
                        <Box key={payment.id}>
                            <p>{payment.payeeName}: ${payment.paymentAmount.toFixed(2)} due</p>
                        </Box>
                    ))}
                </div>
            </Box>
        </LocalizationProvider>
    );
}

export default PaymentCalendar;