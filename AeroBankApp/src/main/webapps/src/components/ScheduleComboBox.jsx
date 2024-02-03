import '../ScheduleComboBox.css';
import ComboBox from "./ComboBox";

export default function ScheduleComboBox({value, onChange, data})
{
    return (
            <div>

                <div className="schedule-interval-combo">
                    <label htmlFor="schedule-combo-box" className="deposit-schedule-combobox">Schedule Interval: </label>
                    <ComboBox
                        value={value}
                        onChange={onChange}
                        label="Schedule" data={data}/>
                </div>
            </div>
    )
}