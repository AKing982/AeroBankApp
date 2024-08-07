import {Box} from "@mui/system";
import {Tab, Tabs} from "@mui/material";
import {TabPanel} from "@mui/lab";
import {useState} from "react";

export default function UserSetupContainer()
{
    const [value, setValue] = useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
                <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
                    <Tab label="Tab 1" />
                    <Tab label="Tab 2" />
                    <Tab label="Tab 3" />
                </Tabs>
            </Box>
            <TabPanel value={value} index={0}>
                Content of Tab 1
            </TabPanel>
            <TabPanel value={value} index={1}>
                Content of Tab 2
            </TabPanel>
            <TabPanel value={value} index={2}>
                Content of Tab 3
            </TabPanel>
        </Box>
    );
}