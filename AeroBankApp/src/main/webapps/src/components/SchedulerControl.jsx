import {Button, Card, FormControl, Grid, Slider, Switch, Typography} from "@mui/material";
import {useState} from "react";

export default function SchedulerControl()
{
    const [isChecked, setIsChecked] = useState(null);
    return (
        <Grid container spacing={2}>
            <Grid item xs={12}>
                <Card>
                    <FormControl>
                        <Typography>Scheduler Control</Typography>
                        <Button>Start</Button>
                        <Button>Pause</Button>
                        <Button>Stop</Button>
                        <Switch checked={isChecked} />
                        <Slider defaultValue={30} />
                    </FormControl>
                </Card>
            </Grid>
        </Grid>
    );
}