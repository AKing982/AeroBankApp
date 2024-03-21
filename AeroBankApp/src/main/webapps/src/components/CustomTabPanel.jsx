import {Tab, Tabs, Typography} from "@mui/material";
import {Box} from "@mui/system";
import PropTypes from "prop-types";
import {useState} from "react";
import TransactionView from "./TransactionView";
import DepositView from "./DepositView";
import WithdrawView from "./WithdrawView";
import TransferView from "./TransferView";
import SettingsView from "./SettingsView";
import BillPayView from "./BillPayView";
import DashBoard from "./DashBoard";
import BillPayPage from "./BillPayPage";
import {useNavigate} from "react-router-dom";
import GenerateReports from "./GenerateReports";
import AuditLogs from "./AuditLogs";
import AccountSummaries from "./AccountSummaries";

function CustomTabPanel({children, value, index, ...other})
{
    const navigate = useNavigate();
    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`simple-tabpanel-${index}`}
            aria-labelledby={`simple-tab-${index}`}
            {...other}
        >
            {value === index && (
                <Box sx={{ p: 3 }}>
                    <Typography>{children}</Typography>
                </Box>
            )}
        </div>
    );
}

CustomTabPanel.propTypes = {
    children: PropTypes.node,
    index: PropTypes.number.isRequired,
    value: PropTypes.number.isRequired,
};

function a11yProps(index) {
    return {
        id: `simple-tab-${index}`,
        'aria-controls': `simple-tabpanel-${index}`,
    };
}

export default function BasicTabs({role, accounts}) {
    const [value, setValue] = useState(1);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Box sx={{ width: '100%', bgcolor: 'background.paper'}}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider', bgcolor: '#e0f2f1' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Dashboard" {...a11yProps(0)} sx={{fontWeight: 'bold'}}/>
                    <Tab label="Transactions" {...a11yProps(1)} sx={{fontWeight: 'bold'}} />
                    <Tab label="Make Deposit" {...a11yProps(2)} sx={{fontWeight: 'bold'}}/>
                    <Tab label="Make a Withdrawal" {...a11yProps(3)} sx={{fontWeight: 'bold'}} />
                    <Tab label="Make a Transfer" {...a11yProps(4)} sx={{fontWeight: 'bold'}}/>
                    <Tab label="Bill Pay" {...a11yProps(5)} sx={{fontWeight: 'bold'}}/>
                    <Tab label="Account Activity" {...a11yProps(6)} sx={{fontWeight: 'bold'}}/>
                    {role === 'ADMIN' &&  <Tab label="Settings" {...a11yProps(7)} sx={{fontWeight: 'bold'}}/>}
                </Tabs>
            </Box>
            <CustomTabPanel value={value} index={0}>
            <DashBoard user="AKing94" accounts={accounts}/>
            </CustomTabPanel>
            <CustomTabPanel value={value} index={1}>
                <TransactionView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={2}>
                <DepositView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={3}>
                <WithdrawView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={4}>
                <TransferView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={5}>
                <BillPayPage />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={6}>
                <AccountSummaries />
            </CustomTabPanel>
            {role === 'ADMIN' && (
                <CustomTabPanel value={value} index={7}>
                <SettingsView />
            </CustomTabPanel>

            )}
            {role === 'AUDITOR' && (
                <CustomTabPanel value={value} index={7}>
                    <AccountSummaries />
                    <AuditLogs />
                </CustomTabPanel>
            )}

        </Box>
    )
}