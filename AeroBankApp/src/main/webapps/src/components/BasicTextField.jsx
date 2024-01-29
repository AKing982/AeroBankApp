import {TextField} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";

export default function BasicTextField({label, height, value, onChange})
{
    return(
        <Box
            component="form"
            sx={{
                '& > :not(style)': { m: 1, width: '25ch' },
            }}
            noValidate
            autoComplete="off"
        >
            <TextField id="outlined-basic" label={label} variant="outlined" value={value} onChange={onChange}
                       InputLabelProps={{style: {
                      }
                       }}
                       sx={{
                           '.MuiOutlinedInput-root': {
                               height: `${height}px`, // Set the height of the input field
                               // Adjust the position as necessary
                               marginLeft: '0px', // Change as needed to align the field
                               // Other styles here if needed
                           },
                           '.MuiInputBase-input': {
                               height: `${height - 20}px`, // Adjust the text field height
                               // Other styles here if needed
                           },
                           // If you need to move the label when the input is not focused:
                           '.MuiInputLabel-root': {
                               top: '50%', // Adjust as needed
                               transform: 'translate(20%, -70%)', // Center the label vertically
                           },
                           // If you need to move the label when the input is focused (or filled):
                           '.MuiInputLabel-shrink': {
                               top: '-0px', // Adjust as needed
                               // Other adjustments here if needed
                           }}}/>
        </Box>
    );
}