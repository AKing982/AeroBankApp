import {LocalizationProvider, TimePicker} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DemoContainer} from "@mui/x-date-pickers/internals/demo";

export default function BasicTimePicker({height, label, value, onChange})
{
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DemoContainer components={['TimePicker']}>
                <TimePicker
                    value={value ?? null}
                    onChange={onChange}
                    label={label} sx={{
                    height: `${height}px`,
                    '.MuiInputBase-input': {
                        height: `${height-20}px`,
                    },
                    '.MuiInputLabel-shrink': {
                        transform: 'translate(0, -50%) scale(0.75)',
                    }}}/>
            </DemoContainer>
        </LocalizationProvider>
    );
}