import {Box} from "@mui/system";
import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";

export default function AccountTypeSelect({value, onChange})
{
    const selectValue = value == null ? '' : value;
    return (
        <Box sx={{ minWidth: 40 }}>
            <FormControl fullWidth>
                <InputLabel id="demo-simple-select-label">AccountType</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectValue}
                    label="AccountType"
                    onChange={onChange}
                >
                    <MenuItem value="CHECKING">Checking</MenuItem>
                    <MenuItem value="SAVINGS">Savings</MenuItem>
                    <MenuItem value="INVESTMENT">Investment</MenuItem>
                    <MenuItem value="RENT">Rent</MenuItem>
                </Select>
            </FormControl>
        </Box>
    );
}
