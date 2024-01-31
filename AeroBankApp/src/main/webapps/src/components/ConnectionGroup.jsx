import {FormControl, FormControlLabel, FormLabel, Radio, RadioGroup} from "@mui/material";

export default function ConnectionGroup()
{
    return (
        <FormControl>
            <FormLabel id="demo-row-radio-buttons-group-label">Connection Type</FormLabel>
            <RadioGroup
                row
                aria-labelledby="demo-row-radio-buttons-group-label"
                name="row-radio-buttons-group"
            >
                <FormControlLabel value="mysql" control={<Radio />} label="MySQL" />
                <FormControlLabel value="ssql" control={<Radio />} label="SQL Server" />
                <FormControlLabel value="postgresql" control={<Radio />} label="PostgreSQL" />

            </RadioGroup>
        </FormControl>
    );
}