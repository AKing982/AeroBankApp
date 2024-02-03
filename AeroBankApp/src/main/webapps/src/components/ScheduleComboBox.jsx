import '../ScheduleComboBox.css';
import ComboBox from "./ComboBox";

export default function ScheduleComboBox({value, onChange, data})
{
    return (
            <div>

                <div className="schedule-interval-combo">
                    <ComboBox
                        value={value}
                        onChange={onChange}
                        label="Schedule" data={data}/>
                </div>
            </div>
    )
}