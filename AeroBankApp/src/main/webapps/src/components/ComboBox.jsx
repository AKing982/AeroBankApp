import {LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {Autocomplete, TextField} from "@mui/material";

export default function ComboBox({label, data}) {
    return (
        <Autocomplete
            disablePortal
            id="combo-box-demo"
            options={data}
            sx={{width: 220}}
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



