import '../ScheduleComboBox.css';

export default function ScheduleComboBox({schedule, setSchedule})
{
    const handleScheduleChange = (event) => {
        setSchedule(event.target.value);
    }

    return (
        <div>
            <label htmlFor="schedule-combo-box" className="deposit-schedule-combobox">Schedule Interval: </label>
            <select id="schedule-combo" value={schedule} onChange={handleScheduleChange}>
                <option value="">Select...</option>
                <option value="Once">Once</option>
                <option value="Daily">Daily</option>
                <option value="Weekly">Weekly</option>
                <option value="Monthly">Monthly</option>
                <option value="Bi-Weekly">Bi-Weekly</option>
                <option value="Bi-Daily">Bi-Daily</option>
            </select>
        </div>
    )
}