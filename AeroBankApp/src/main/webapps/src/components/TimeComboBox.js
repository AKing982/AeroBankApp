import {useMemo} from "react";
import '../TimeComboBox.css';

export default function TimeComboBox({selectedTime, setSelectedTime})
{

    const handleTimeChange = (event) => {
        setSelectedTime(event.target.value);
    }

    const generateTimeOptions = useMemo(() => {

        const times = [];
        const counter = 24;

        for (let i = 0; i < 24; i++) { // Loop for 24 hours
            for (let j = 0; j < 60; j += 1) { // Inner loop for each 5-minute interval in an hour
                const hour = i < 10 ? `0${i}` : `${i}`;
                const minutes = j < 10 ? `0${j}` : `${j}`;
                times.push(`${hour}:${minutes}`);
            }
        }

        const timeSet = new Set(times);
        return Array.from(timeSet);
    }, []);

    const formatAMPM = (time) => {
        let [hours, minutes] = time.split(':');
        const ampm = parseInt(hours, 10) >= 12 ? 'PM' : 'AM';
        hours = ((parseInt(hours, 10) + 11) % 12 + 1).toString().padStart(2, '0');

        return `${hours}:${minutes} ${ampm}`;
    };


    return (
        <div>
            <label htmlFor="time-combo-box">
                Choose a time:
            </label>
            <select id="time-combo-box" value={selectedTime} onChange={handleTimeChange}>
                <option value="">Select time...</option>
                {generateTimeOptions.map((time) => (
                    <option key={time} value={time}>
                        {formatAMPM(time)}
                    </option>
                ))}
            </select>
        </div>
    );

}



