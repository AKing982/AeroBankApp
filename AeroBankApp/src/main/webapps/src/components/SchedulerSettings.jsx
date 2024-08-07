import JobManagement from "./JobManagement";
import TriggerManagement from "./TriggerManagement";
import SchedulerControl from "./SchedulerControl";
import MonitoringJobs from "./MonitoringJobs";
import {Grid} from "@mui/material";

export default function SchedulerSettings()
{
    return (
        <Grid container spacing={2}>
            <Grid item xs={6}>
                <JobManagement />
            </Grid>
            <Grid item xs={6}>
                <TriggerManagement />
            </Grid>
            <Grid item xs={6}>
                <SchedulerControl />
            </Grid>
            <Grid item xs={6}>
                <MonitoringJobs />
            </Grid>
        </Grid>
    );
}