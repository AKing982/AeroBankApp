import {Container} from "@mui/system";
import {Button, Grid, MenuItem, TextField, Typography} from "@mui/material";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {useState} from "react";
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';

// ReportPreview component for displaying the generated report
const ReportPreview = ({ report }) => (
    <div>
        <Typography variant="h6" gutterBottom>Report Preview</Typography>
        <Typography variant="body1">{report}</Typography>
    </div>
);


export default function AccountSummaries(){

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [reportType, setReportType] = useState('');
    const [reportFormat, setReportFormat] = useState('');
    const [report, setReport] = useState('');

    const handleGenerateReport = async () => {
        // Placeholder for generating report
        // You would replace this with your API call to the backend
        let generatedReport = '';
        switch (reportType) {
            case 'Transaction Statements':
                generatedReport = `Transaction statements report generated for the period: ${startDate.toDateString()} to ${endDate.toDateString()} in ${reportFormat} format`;
                break;
            case 'Statements showing particular transactions':
                generatedReport = `Statements showing particular transactions report generated for the period: ${startDate.toDateString()} to ${endDate.toDateString()} in ${reportFormat} format`;
                break;
            case 'Audit Reports':
                generatedReport = `Audit reports generated for the period: ${startDate.toDateString()} to ${endDate.toDateString()} in ${reportFormat} format`;
                break;
            case 'Logging Data':
                generatedReport = `Logging data report generated for the period: ${startDate.toDateString()} to ${endDate.toDateString()} in ${reportFormat} format`;
                break;
            case 'Transaction Statistics':
                generatedReport = `Transaction statistics report generated for the period: ${startDate.toDateString()} to ${endDate.toDateString()} in ${reportFormat} format`;
                break;
            default:
                generatedReport = 'Please select a report type.';
        }
        setReport(generatedReport);
    };

    return (
        <Container>
            <Typography variant="h4" gutterBottom>Account Summary Report</Typography>
            <Grid container spacing={2}>
                <Grid item xs={12} md={6}>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DatePicker
                            label="Start Date"
                            value={startDate}
                            onChange={(newValue) => {
                                setStartDate(newValue);
                            }}
                            renderInput={(params) => <TextField {...params} />}
                        />
                    </LocalizationProvider>
                </Grid>
                <Grid item xs={12} md={6}>
                    <LocalizationProvider dateAdapter={AdapterDateFns}>
                        <DatePicker
                            label="End Date"
                            value={endDate}
                            onChange={(newValue) => {
                                setEndDate(newValue);
                            }}
                            renderInput={(params) => <TextField {...params} />}
                        />
                    </LocalizationProvider>
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        select
                        label="Report Type"
                        value={reportType}
                        onChange={(e) => setReportType(e.target.value)}
                        variant="outlined"
                        fullWidth
                    >
                        <MenuItem value="Transaction Statements">Transaction Statements</MenuItem>
                        <MenuItem value="Statements showing particular transactions">Statements showing particular transactions</MenuItem>
                        <MenuItem value="Audit Reports">Audit Reports</MenuItem>
                        <MenuItem value="Logging Data">Logging Data</MenuItem>
                        <MenuItem value="Transaction Statistics">Transaction Statistics</MenuItem>
                    </TextField>
                </Grid>
                <Grid item xs={12}>
                    <TextField
                        select
                        label="Report Format"
                        value={reportFormat}
                        onChange={(e) => setReportFormat(e.target.value)}
                        variant="outlined"
                        fullWidth
                    >
                        <MenuItem value="PDF">PDF</MenuItem>
                        <MenuItem value="HTML">HTML</MenuItem>
                        <MenuItem value="Excel Spreadsheet">Excel Spreadsheet</MenuItem>
                    </TextField>
                </Grid>
                <Grid item xs={12}>
                    <Button variant="contained" color="primary" startIcon={<PlayCircleOutlineIcon />} onClick={handleGenerateReport} fullWidth>Generate Report</Button>
                </Grid>
            </Grid>
            {report && (
                <ReportPreview report={report} />
            )}
        </Container>
    );
}