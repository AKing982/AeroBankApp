import {Container} from "@mui/system";
import {
    Button,
    Checkbox, FormControl,
    FormControlLabel, FormLabel,
    Grid,
    IconButton,
    MenuItem,
    Paper, Radio, RadioGroup,
    Select,
    TextField,
    Typography
} from "@mui/material";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {useEffect, useState} from "react";
import MenuAppBar from "./MenuAppBar";
import GradientSeparator from "./GradientSeparator";
import ReportList from "./ReportList";
import ReportPDF from "./ReportPDF";
import {PDFViewer} from "@react-pdf/renderer";
import ReportLoadingBar from "./ReportLoadingBar";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import PropTypes from "prop-types";
import SaveIcon from '@mui/icons-material/Save';
import axios from "axios";

function ReportLoadingDialog({open, onClose, reportName, progress}){
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>Loading Report</DialogTitle>
            <DialogContent>
                <Typography variant="subtitle1" gutterBottom>
                    Generating: {reportName}
                </Typography>
                <ReportLoadingBar value={progress} />
            </DialogContent>
        </Dialog>
    );
}

ReportLoadingDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    onClose: PropTypes.func.isRequired,
    reportName: PropTypes.string.isRequired,
    progress: PropTypes.number.isRequired,
};



export default function AccountSummaries(){

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [report, setReport] = useState(null);
    const [selectedReportType, setSelectedReportType] = useState('');
    const [reportContent, setReportContent] = useState('');
    const [reports, setReports] = useState([
        { id: '1', title: 'Bill Statements', children: [] },
        { id: '2', title: 'Transaction Statements', children: [] },
        { id: '3', title: 'Audit Logs', children: [] }
    ]);
    const [selectedReportName, setSelectedReportName] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [progress, setProgress] = useState(0);
    const [isGenerating, setIsGenerating] = useState(false);
    const [exportOption, setExportOption] = useState('pdf');
    const [reportName, setReportName] = useState('');

    const handleExportChange = (event) => {
        setExportOption(event.target.value);
    };

    const handleExport = () => {
        if (exportOption.pdf) {
            // Implement PDF export logic
        }
        if (exportOption.excel) {
            // Implement Excel export logic
        }
        if (exportOption.html) {
            // Implement HTML export logic
        }
    };

    const reportTypes = [
        { id: '1', title: 'Bill Statements', children: [
                { id: '1-1', title: 'Electric Bill', children: null },
                { id: '1-2', title: 'Water Bill', children: null }
            ] },
        { id: '2', title: 'Transaction Statements', children: null },
        { id: '3', title: 'Audit Logs', children: null }
        // More report types and potentially nested structures
    ];

    const reportList = [
        { id: 'financialReports', title: 'Financial Reports', children: [
                { id: 'billStatements', title: 'Bill Statements' },
                { id: 'transactionStatements', title: 'Transaction Statements' }
            ]},
        { id: 'auditLogs', title: 'Audit Logs' }
    ];

    // const handleGenerateReport = async () => {
    //     // Simulating a report generation
    //      const generatedReport = `Report for: ${selectedReportType} from ${startDate.toDateString()} to ${endDate.toDateString()}`;
    //      setReport(generatedReport);
    //    // setReport('This is a dynamically generated PDF report.');
    //
    //     // Here you would typically make a backend request to generate the report with JasperReports
    //     // axios.post('backend-url-to-generate-report', { startDate, endDate, reportType: selectedReportType })
    //     //     .then(response => setReportContent(response.data))
    //     //     .catch(error => console.error('Error generating report:', error));
    // };

    // useEffect(() => {
    //     if (isGenerating) {
    //         const timer = setInterval(() => {
    //             setProgress((prevProgress) => {
    //                 if (prevProgress >= 100) {
    //                     clearInterval(timer);
    //                     setIsGenerating(false);
    //                     return 100;
    //                 }
    //                 return prevProgress + 10;
    //             });
    //         }, 800);
    //         return () => clearInterval(timer);
    //     }
    // }, [isGenerating]);

    const handleGenerateReport = () => {
        setIsLoading(true);
        let currentProgress = 10;
        const progressInterval = setInterval(() => {
            if (currentProgress >= 100) {
                clearInterval(progressInterval);
                setIsLoading(false);
                const generatedReport = `Report for: ${selectedReportType} from ${startDate.toDateString()} to ${endDate.toDateString()}`;
                setReport(generatedReport);
            } else {
                currentProgress += 10;
                setProgress(currentProgress);
            }
        }, 800);
    };

    const handleReportGeneration = async () => {
        setIsLoading(true);
        setProgress(10); // Start with initial 10%

        let currentProgress = 10;
        const maxProgressBeforeCompletion = 90;

        // Function to increment progress
        const incrementProgress = () => {
            if (currentProgress < maxProgressBeforeCompletion) {
                currentProgress += 10;
                setProgress(currentProgress);
            }
        };

        // Start an interval to increment progress
        const progressInterval = setInterval(incrementProgress, 5000);

        try {
            const formattedStartDate = startDate.toISOString().slice(0, 10); // Correctly format the startDate
            const formattedEndDate = endDate.toISOString().slice(0, 10); // Correctly format the endDate

            const response = await axios.post('http://localhost:8080/AeroBankApp/api/reports/generate-pdf', {
                startDate: formattedStartDate,
                endDate: formattedEndDate,
                reportType: selectedReportType,
                exportType: exportOption,
                reportName: reportName
            }, {
                responseType: 'blob' // Important for handling binary data
            });

            // Stop the interval as we are about to complete
            clearInterval(progressInterval);

            // Ensure we set the progress to 90% if not already there
            if (currentProgress < maxProgressBeforeCompletion) {
                setProgress(maxProgressBeforeCompletion);
            }


            // Delay showing the PDF to visually complete the progress bar
            setTimeout(() => {
                const blob = new Blob([response.data], { type: 'application/pdf' });
                const reportUrl = window.URL.createObjectURL(blob);
                console.log('Report URL: ', reportUrl);

                setReport(reportUrl);
                setProgress(100); // Finally, set the progress to 100%
                setIsLoading(false); // Turn off loading indicator
            }, 7800); // Short delay to ensure user sees completion

        } catch (error) {
            console.error('Error generating report:', error);
            clearInterval(progressInterval);
            setProgress(0);
            setIsLoading(false);
        }
    };

    const handleSaveReport = async () => {

    }

    const handleReportSelect = (reportId) => {
        setSelectedReportType(reportId);
    };

    const handleSelectReportType = (id) => {
        // Here you would handle the logic to display or react to the selected report
        console.log("Selected report type:", id);
        setSelectedReportType(id);
    };



    return (
        <div>
            <MenuAppBar />
            <GradientSeparator />
            <Container maxWidth="lg">
                <Grid container spacing={2}>
                    <Grid item xs={3}>
                        <ReportList reports={reports} setReports={setReports} onSelect={setSelectedReportType} />
                    </Grid>
                    <Grid item xs={9}>
                        <Paper elevation={3} sx={{ padding: 4, margin: 2 }}>
                            <Typography variant="h5" gutterBottom>Generate Report</Typography>
                            <LocalizationProvider dateAdapter={AdapterDateFns}>
                                <Grid container spacing={2}>
                                    <Grid item xs={12} sm={6}>
                                        <DatePicker
                                            label="Start Date"
                                            value={startDate}
                                            onChange={setStartDate}
                                            renderInput={(params) => <TextField {...params} fullWidth />}
                                        />
                                    </Grid>
                                    <Grid item xs={12} sm={6}>
                                        <DatePicker
                                            label="End Date"
                                            value={endDate}
                                            onChange={setEndDate}
                                            renderInput={(params) => <TextField {...params} fullWidth />}
                                        />
                                    </Grid>
                                    <Grid item xs={12}>
                                        <TextField
                                            fullWidth
                                            label="Report Name"
                                            value={reportName}
                                            onChange={(e) => setReportName(e.target.value)}
                                            margin="normal"
                                        />
                                        <Select
                                            fullWidth
                                            value={selectedReportType}
                                            onChange={(e) => setSelectedReportType(e.target.value)}
                                            displayEmpty
                                            inputProps={{ 'aria-label': 'Without label' }}
                                        >
                                            {reportTypes.map((type) => (
                                                <MenuItem key={type.id} value={type.id}>{type.title}</MenuItem>
                                            ))}
                                        </Select>
                                        <FormControl component="fieldset">
                                            <FormLabel component="legend">Export As</FormLabel>
                                            <RadioGroup
                                                row
                                                aria-label="export as"
                                                name="export-as"
                                                value={exportOption}
                                                onChange={handleExportChange}
                                            >
                                                <FormControlLabel value="pdf" control={<Radio />} label="PDF" />
                                                <FormControlLabel value="excel" control={<Radio />} label="Excel" />
                                                <FormControlLabel value="html" control={<Radio />} label="HTML" />
                                            </RadioGroup>
                                        </FormControl>
                                    </Grid>
                                    <Grid item xs={12}>
                                        <Button variant="contained" color="primary" onClick={handleReportGeneration}>
                                            Generate Report
                                        </Button>
                                        <IconButton onClick={handleSaveReport} color="primary">
                                            <SaveIcon />
                                        </IconButton>
                                        {isLoading && (
                                            <Grid item xs={12}>
                                                <Typography>{`Generating ${selectedReportType}: ${reportName}`}</Typography>
                                                <ReportLoadingBar value={progress} />
                                            </Grid>
                                        )}
                                    </Grid>
                                </Grid>
                            </LocalizationProvider>
                            {report && !isLoading && (
                                <PDFViewer style={{ width: '100%', height: '500px' }}>
                                    {/*<ReportPDF reportContent={report} reportName={reportName} />*/}
                                    <iframe src={report} title={reportName} width="100%" height="500px"/>
                                </PDFViewer>
                            )}
                        </Paper>
                    </Grid>
                </Grid>
            </Container>
        </div>
    );
}