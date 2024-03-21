import {Container} from "@mui/system";
import {Button, TextField, Typography} from "@mui/material";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {useState} from "react";

export default function AccountSummaries(){

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [report, setReport] = useState('');

    const handleGenerateReport = async () => {
        // Placeholder for generating report
        // You would replace this with your API call to the backend
        const generatedReport = `Report generated for the period: ${startDate.toDateString()} to ${endDate.toDateString()}`;
        setReport(generatedReport);
    };

    return (
        <Container>
            <Typography variant="h4" gutterBottom>Account Summary Report</Typography>
            <LocalizationProvider dateAdapter={AdapterDateFns}>
                <DatePicker
                    label="Start Date"
                    value={startDate}
                    onChange={(newValue) => {
                        setStartDate(newValue);
                    }}
                    renderInput={(params) => <TextField {...params} />}
                />
                <DatePicker
                    label="End Date"
                    value={endDate}
                    onChange={(newValue) => {
                        setEndDate(newValue);
                    }}
                    renderInput={(params) => <TextField {...params} />}
                />
            </LocalizationProvider>
            <Button variant="contained" color="primary" onClick={handleGenerateReport}>Generate Report</Button>
            {report && (
                <div>
                    <Typography variant="body1" style={{ marginTop: '20px' }}>
                        {report}
                    </Typography>
                </div>
            )}
        </Container>
    );
}