import {FormControl, FormControlLabel, FormLabel, Radio, RadioGroup} from "@mui/material";

export default function EmailRadioGroup()
{
    return (
        <FormControl>
            <FormLabel id="email-radio-buttons-group-label">Email Type</FormLabel>
            <RadioGroup
                row
                aria-labelledby="email-radio-buttons-group-label"
                name="row-radio-buttons-group"
            >
                <FormControlLabel value="imap" control={<Radio />} label="IMAP" />
                <FormControlLabel value="pop3" control={<Radio />} label="POP3" />
            </RadioGroup>
        </FormControl>
    );
}
