import {LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {Autocomplete, TextField} from "@mui/material";

export default function ComboBox({label, data, value, onChange}) {
    return (
        <Autocomplete
            disablePortal
            id="combo-box-demo"
            options={data}
            sx={{width: 260}}
            value={value}
            onChange={onChange}
            getOptionLabel={(option) => option}
            renderInput={(params) => <TextField {...params} label={label}/>}
        />
    );
}

const options = [
    'Once',
    'Daily',
    'Weekly',
    'Monthly'
]



