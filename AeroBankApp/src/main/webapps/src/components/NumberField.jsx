import {InputAdornment, TextField} from "@mui/material";

export default function NumberField({value, onChange, label})
{
    return (
        <TextField
            label={label}
            value={value}
            onChange={onChange}
            type="number"
            InputProps={{
                startAdornment: <InputAdornment position="start">$</InputAdornment>,
                inputProps: { min: 0 }  // This ensures only non-negative numbers can be entered
            }}
        />
    );
}