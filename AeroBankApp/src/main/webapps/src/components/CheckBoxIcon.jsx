import {FormControlLabel, FormGroup} from "@mui/material";
import {Check, CheckBox} from "@mui/icons-material";
import {useState} from "react";

export default function CheckBoxIcon({label})
{
    const [isChecked, setIsChecked] = useState(false);

    const handleChange = (event) => {
        setIsChecked(event.target.value);
    };

    return (
        <FormGroup>
            <FormControlLabel
                control={
                    <CheckBox
                        checked={isChecked}
                        onChange={handleChange}
                    />
                }
                label={label}
            />
        </FormGroup>
    );
}