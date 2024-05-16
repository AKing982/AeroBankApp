import {LinearProgress, Typography} from "@mui/material";
import {Box} from "@mui/system";
import {useEffect, useState} from "react";
import LinearProgressWithLabel from "@mui/material/Slider/SliderValueLabel";
import PropTypes from "prop-types";

function ReportLoadingBar({value}){
    return (
        <Box sx={{display: 'flex', alignItems: 'center'}}>
            <Box sx={{width: '100%', mr: 1}}>
                <LinearProgress variant="determinate" value={value}/>
            </Box>
            <Box sx={{minWidth: 35}}>
                <Typography variant="body2" color="text.secondary">
                    {`${Math.round(value)}%`}
                </Typography>
            </Box>
        </Box>
    );

}

ReportLoadingBar.propTypes = {
    /**
     * The value of the progress indicator for the determinate and buffer variants.
     * Value between 0 and 100.
     */
    value: PropTypes.number.isRequired,
};

export default function ReportLoadingBarValueLabel(){
    const [progress, setProgress] = useState(10);

    useEffect(() => {
        const timer = setInterval(() => {
            setProgress((prevProgress) => (prevProgress >= 100 ? 10 : prevProgress + 10));
        }, 800);
        return () => {
            clearInterval(timer);
        };
    }, []);

    return (
        <Box sx={{width: '100%'}}>
            <ReportLoadingBar value={progress}/>
        </Box>
    );
}

