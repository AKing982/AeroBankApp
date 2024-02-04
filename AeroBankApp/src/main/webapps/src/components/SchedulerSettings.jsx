import JobManagement from "./JobManagement";
import TriggerManagement from "./TriggerManagement";
import SchedulerControl from "./SchedulerControl";
import MonitoringJobs from "./MonitoringJobs";

export default function SchedulerSettings()
{
    return (
        <div className="scheduler-settings-container">
            <JobManagement />
            <TriggerManagement />
            <SchedulerControl />
            <MonitoringJobs />
        </div>
    );
}