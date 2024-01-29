import BasicTimePicker from "./BasicTimePicker";

export default function TimePickerBox({height})
{
    return(
        <div>
            <label htmlFor="time-pickerbox-label" className="time-picker-label">Select a Time:</label>
            <div className="time-pickerBox-field">
                <BasicTimePicker label="Select Time" height={height}/>
            </div>
        </div>
    )
}