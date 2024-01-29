import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";


export default function AccountSelect({accounts})
{
    const [selectedAccount, setSelectedAccount] = useState('');

    const handleChange = (event) => {
        setSelectedAccount(event.target.value);
    };

    return (
        <div className="account-select">
            <Box sx={{display: 'flex', flexDirection: 'row', alignItems: 'flex-end', minWidth: 120 }}>
                <FormControl sx={{ width: 'auto', minWidth: '220px' }}>
                    <InputLabel
                        sx={{width: 'fit-content'}}
                        id="demo-simple-select-label">Account</InputLabel>
                    <Select
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={selectedAccount}
                        label="Account"
                        onChange={handleChange}
                    >
                        {accounts.map((account) => (
                            <MenuItem key={account.id} value={account.accountCode}>
                                {account.accountCode}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            </Box>
        </div>

    );
}