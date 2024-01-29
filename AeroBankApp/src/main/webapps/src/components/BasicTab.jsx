import CustomTabPanel from "./CustomTabPanel";
import {Tab, Tabs} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";
import TransactionView from "./TransactionView";
import DepositView from "./DepositView";

export default function BasicTab()
{
    const [value, setValue] = useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    function a11yProps(index) {
        return {
            id: `simple-tab-${index}`,
            'aria-controls': `simple-tabpanel-${index}`,
        };
    }


    return (
        <Box sx={{ width: '100%' }}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Transactions" {...a11yProps(0)} />
                    <Tab label="Make a Deposit" {...a11yProps(1)} />
                    <Tab label="Make a Withdrawal" {...a11yProps(2)} />
                    <Tab label="Make a Transfer" {...a11yProps(3)} />
                    <Tab label="Settings" {...a11yProps(4)} />
                </Tabs>
            </Box>
            <CustomTabPanel value={value} index={0}>
                <TransactionView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={1}>
                <DepositView />
            </CustomTabPanel>
            <CustomTabPanel value={value} index={2}>
                Content for Make a Withdrawal
            </CustomTabPanel>
            <CustomTabPanel value={value} index={3}>
                Content for Make a Transfer
            </CustomTabPanel>
            <CustomTabPanel value={value} index={4}>
                Content for Settings
            </CustomTabPanel>
        </Box>
    );
}