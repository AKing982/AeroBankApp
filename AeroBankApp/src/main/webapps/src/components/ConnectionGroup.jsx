import {FormControl, FormControlLabel, FormLabel, Radio, RadioGroup} from "@mui/material";

export default function ConnectionGroup({value, onChange})
{
    console.log('value: ', value);
    return (
        <FormControl>
            <FormLabel id="demo-row-radio-buttons-group-label">Connection Type</FormLabel>
            <RadioGroup
                row
                aria-labelledby="demo-row-radio-buttons-group-label"
                name="row-radio-buttons-group"
                value={value}
                onChange={onChange}
            >
                <FormControlLabel value="mysql" control={<Radio />} label="MySQL" />
                <FormControlLabel value="ssql" control={<Radio />} label="SQL Server" />
                <FormControlLabel value="postgresql" control={<Radio />} label="PostgreSQL" />

            </RadioGroup>
        </FormControl>
    );
}