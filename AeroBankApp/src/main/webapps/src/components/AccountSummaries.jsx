import {Box, Container} from "@mui/system";
import {Button, FormControl, Grid, InputLabel, MenuItem, Select, TextField, Typography} from "@mui/material";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFnsV3";
import {useState} from "react";
import PlayCircleOutlineIcon from '@mui/icons-material/PlayCircleOutline';
import ComboBox from "./ComboBox";

import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import {TreeItem, TreeView} from "@mui/x-tree-view";

// ReportPreview component for displaying the generated report
const ReportPreview = ({ report }) => (
    <div>
        <Typography variant="h6" gutterBottom>Report Preview</Typography>
        <Typography variant="body1">{report}</Typography>
    </div>
);
const reportData = [
    {
        id: 'transaction-reports',
        name: 'Transaction Reports',
        children: [
            {
                id: 'daily-transactions',
                name: 'Daily Transactions',
            },
            {
                id: 'monthly-transactions',
                name: 'Monthly Transactions',
                children: [
                    {
                        id: 'january-transactions',
                        name: 'January Transactions',
                    },
                    {
                        id: 'february-transactions',
                        name: 'February Transactions',
                    },
                    // Add more months as needed
                ],
            },
            // Add more sub-reports under Transaction Reports
        ],
    },
    {
        id: 'audit-reports',
        name: 'Audit Reports',
        children: [
            {
                id: 'financial-audit',
                name: 'Financial Audit',
            },
            {
                id: 'compliance-audit',
                name: 'Compliance Audit',
            },
            // Add more sub-reports under Audit Reports
        ],
    },
    // Add more top-level reports as needed
];

// Recursive function to render tree nodes
const renderTreeNodes = (reports) =>
    reports.map((report) => (
        <TreeItem key={report.id} nodeId={String(report.id)} label={report.name} itemId={report.id}>
            {report.children && renderTreeNodes(report.children)}
        </TreeItem>
    ));


const ReportTree = () => {
    return (
        <Box border={1} borderColor="grey.400" borderRadius={1} p={1}>
            <TreeView
                defaultCollapseIcon={<ExpandMoreIcon />}
                defaultExpandIcon={<ChevronRightIcon />}
            >
                {renderTreeNodes(reportData)}
            </TreeView>
        </Box>
    );
};

export default function AccountSummaries(){

    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [reportType, setReportType] = useState('');
    const [reportFormat, setReportFormat] = useState('');
    const [report, setReport] = useState('');

    const handleGenerateReport = async () => {
        // Placeholder for generating report
        // You would replace this with your API call to the backend
        const generatedReport = `Report generated for the period: ${startDate?.toDateString()} to ${endDate?.toDateString()} in ${reportFormat} format`;
        setReport(generatedReport);
    };

    const [selectedFormat, setSelectedFormat] = useState(null); // State for selected format
    const [selectedReportType, setSelectedReportType] = useState(null); // State for selected report type

    // Sample options for format and report type
    const formatOptions = ['PDF', 'HTML', 'Excel Spreadsheet'];
    const reportTypeOptions = ['Transaction Statements', 'Statements showing particular transactions', 'Audit Reports', 'Logging Data', 'Transaction Statistics'];

    return (
        <div className="account-summaries-container">
            <div className="sidebar">
                <h2>Reports</h2>
                <ReportTree /> {/* Render the ReportTree component */}
            </div>
            <div className="main-content">
                <Container>
                    <Typography variant="h4" gutterBottom>Account Summary Report</Typography>
                    {/* ComboBox for selecting format */}
                    <ComboBox
                        fullWidth
                        label="Select Format"
                        value={selectedFormat}
                        onChange={(event, newValue) => setSelectedFormat(newValue)}
                        options={formatOptions}
                        renderInput={(params) => <TextField {...params} />}
                    />
                    {/* ComboBox for selecting report type */}
                    <ComboBox
                        fullWidth
                        label="Select Report Type"
                        value={selectedReportType}
                        onChange={(event, newValue) => setSelectedReportType(newValue)}
                        options={reportTypeOptions}
                        renderInput={(params) => <TextField {...params} />}
                    />
                    {/* Include other content of the AccountSummaries component */}
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
            </div>
        </div>
    );
}