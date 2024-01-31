import {Checkbox, FormControlLabel, FormGroup} from "@mui/material";

export default function TLSCheckBox()
{
    return (
        <FormGroup>
            <FormControlLabel control={<Checkbox defaultChecked />} label="TLS Enabled" />
        </FormGroup>
    );
}