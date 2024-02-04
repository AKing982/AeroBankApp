import {Box} from "@mui/system";
import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";

export default function DepositAccountCode({accounts, value, onChange})
{
    return (
        <div className="account-select">
            <Box sx={{display: 'flex', flexDirection: 'row', alignItems: 'flex-end', minWidth: 120 }}>
                <FormControl sx={{ width: 'auto', minWidth: '1091px' }}>
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