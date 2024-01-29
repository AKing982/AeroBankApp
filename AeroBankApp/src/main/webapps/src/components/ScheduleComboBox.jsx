import '../ScheduleComboBox.css';
import ComboBox from "./ComboBox";

export default function ScheduleComboBox({schedule, setSchedule, data})
{
    const handleScheduleChange = (event) => {
        setSchedule(event.target.value);
    }

    return (
            <div>
                <label htmlFor="schedule-combo-box" className="deposit-schedule-combobox">Schedule Interval: </label>
                <div className="schedule-interval-combo">
                    <ComboBox label="Schedule" data={data}/>
                </div>
            </div>



    )
}