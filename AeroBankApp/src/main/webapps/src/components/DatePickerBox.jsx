import {DatePicker} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import {DemoContainer} from "@mui/x-date-pickers/internals/demo";
import {LocalizationProvider} from "@mui/x-date-pickers";

export default function DatePickerBox({label, height, width, value, onChange})
{
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DemoContainer components={['DatePicker']}>
                <DatePicker
                    value={value}
                    onChange={onChange}
                    label={label}
                            sx={{
                                height: `${height}px`,
                                width: `${width}px`,
                                '.MuiInputBase-input': {
                                    height: `${height-20}px`,
                                },
                                '.MuiInputLabel-shrink': {
                                    transform: 'translate(0, -50%) scale(0.75)',
                                },

                            }}/>
            </DemoContainer>
        </LocalizationProvider>
    );
}