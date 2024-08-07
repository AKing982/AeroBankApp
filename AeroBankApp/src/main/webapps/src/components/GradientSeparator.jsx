import {Box} from "@mui/system";

export default function GradientSeparator(){
    return (
        <Box
            sx={{
                height: '50px',  // Adjust the height as necessary
                width: '100%',
                backgroundImage: 'linear-gradient(to top, #f0f0f0, #888888)',  // Light grey to dark grey gradient
                marginBottom: 2,  // Adds space below the divider
            }}
        />
    );
}