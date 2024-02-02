import * as React from 'react';
import PropTypes from 'prop-types';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import EmailSettings from "./EmailSettings";
import DatabaseSettings from "./DatabaseSettings";
import UserSetupSettings from "./UserSetupSettings";
import AccountSettings from "./AccountSettings";

function VerticalTab(props) {
    const { children, value, index, ...other } = props;

    return (
        <div
            role="tabpanel"
            hidden={value !== index}
            id={`vertical-tabpanel-${index}`}
            aria-labelledby={`vertical-tab-${index}`}
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

VerticalTab.propTypes = {
    children: PropTypes.node,
    index: PropTypes.number.isRequired,
    value: PropTypes.number.isRequired,
};

function a11yProps(index) {
    return {
        id: `vertical-tab-${index}`,
        'aria-controls': `vertical-tabpanel-${index}`,
    };
}

export default function VerticalTabs() {
    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Box
            sx={{ flexGrow: 1, bgcolor: 'background.paper', display: 'flex', height: 890}}
        >
            <Tabs
                orientation="vertical"
                variant="scrollable"
                value={value}
                onChange={handleChange}
                aria-label="Vertical tabs example"
                sx={{ borderRight: 1, borderColor: 'divider' }}
            >
                <Tab label="Email" {...a11yProps(0)} />
                <Tab label="Database" {...a11yProps(1)} />
                <Tab label="User Setup" {...a11yProps(2)} />
                <Tab label="Accounts" {...a11yProps(3)} />
                <Tab label="Transaction Security" {...a11yProps(4)} />
                <Tab label="Security" {...a11yProps(5)} />
                <Tab label="Scheduler" {...a11yProps(5)}/>
                <Tab label="API Integration" {...a11yProps(6)} />
                <Tab label="Notifications" {...a11yProps(7)}/>
            </Tabs>
            <VerticalTab value={value} index={0}>
                <EmailSettings />
            </VerticalTab>
            <VerticalTab value={value} index={1}>
                <DatabaseSettings />
            </VerticalTab>
            <VerticalTab value={value} index={2}>
                <UserSetupSettings />
            </VerticalTab>
            <VerticalTab value={value} index={3}>
                <AccountSettings />
            </VerticalTab>
            <VerticalTab value={value} index={4}>
                Item Five
            </VerticalTab>
            <VerticalTab value={value} index={5}>
                Item Six
            </VerticalTab>
            <VerticalTab value={value} index={6}>
                Item Seven
            </VerticalTab>
        </Box>
    );
}