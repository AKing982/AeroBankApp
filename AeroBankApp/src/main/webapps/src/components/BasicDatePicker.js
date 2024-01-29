import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {DemoContainer} from "@mui/x-date-pickers/internals/demo";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import DatePickerBox from "./DatePickerBox";

export default function BasicDatePicker({label, height, title})
{
    return (
        <div>
            <label htmlFor="date-picker-label" className="basic-date-picker-label">{title}</label>
            <div className="date-picker-choice">
                <DatePickerBox height={height} label={label}/>
            </div>
        </div>

    );
}