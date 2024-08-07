import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";


export default function AccountSelect({accounts, value, onChange})
{
    return (
        <div className="account-select">
            <Box sx={{display: 'flex', flexDirection: 'row', alignItems: 'flex-end', minWidth: 120 }}>
                <FormControl sx={{ width: 'auto', minWidth: '220px' }}>
                    <InputLabel
                        sx={{width: 'fit-content'}}
                        id="demo-simple-select-label">Account Code</InputLabel>
                    <Select
                        labelId="demo-simple-select-label"
                        id="demo-simple-select"
                        value={value}
                        label="Account"
                        onChange={onChange}
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