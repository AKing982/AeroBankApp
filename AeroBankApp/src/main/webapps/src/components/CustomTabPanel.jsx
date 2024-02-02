import {Tab, Tabs, Typography} from "@mui/material";
import {Box} from "@mui/system";
import PropTypes from "prop-types";
import {useState} from "react";
import TransactionView from "./TransactionView";
import DepositView from "./DepositView";
import WithdrawView from "./WithdrawView";
import TransferView from "./TransferView";
import SettingsView from "./SettingsView";

export default function CustomTabPanel({children, value, index, ...other})
{
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

function BasicTabs() {
    const [value, setValue] = useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Transactions" {...a11yProps(0)} />
                    <Tab label="Make Deposit" {...a11yProps(1)} />
                    <Tab label="Make a Withdrawal" {...a11yProps(2)} />
                    <Tab label="Make a Transfer" {...a11yProps(3)}/>
                    <Tab label="Settings" {...a11yProps(4)}/>
                </Tabs>
            </Box>
            <CustomTabPanel value={value} index={0}>
                <TransactionView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={1}>
                <DepositView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={2}>
                <WithdrawView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={3}>
                <TransferView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={4}>
                <SettingsView />
            </CustomTabPanel>
        </Box>
    )
}