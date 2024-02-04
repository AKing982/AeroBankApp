import {TextField} from "@mui/material";
import {useState} from "react";

const PercentageField = ({label, onChange, ...props}) => {
    const [value, setValue] = useState('');

    const handleChange = (event) => {
        let inputValue = event.target.value;

        inputValue = inputValue.replace(/[^d.]/g, '');

        setValue(inputValue);

        if(onChange)
        {
            const numericValue = parseFloat(inputValue) / 100;
            onChange(numericValue)
        }
    }
    return (
        <TextField
            {...props}
            label={label}
            value={`${value}%`} // Display value as a percentage
            onChange={handleChange}
            inputProps={{ min: 0, max: 100, step: 'any', type: 'number' }} // Optional: restrict input range
        />
    );

}

export default PercentageField;