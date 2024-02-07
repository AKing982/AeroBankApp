import {Box} from "@mui/system";
import {CircularProgress, FormControl, InputLabel, MenuItem, Select} from "@mui/material";

export default function DepositAccountCode({accounts, value, onChange, loading})
{
    console.log('Accounts: ', accounts, 'Type: ', typeof(accounts));
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
                        {loading ? (
                            <MenuItem disabled>
                                <CircularProgress size={24} />
                            </MenuItem>
                        ) : (
                            accounts.map((account, index) => (
                                <MenuItem key={index} value={account.accountCode}>
                                    {account.accountCode}
                                </MenuItem>
                            ))
                        )}

                    </Select>
                </FormControl>
            </Box>
        </div>

    );
}