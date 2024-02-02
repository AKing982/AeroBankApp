import {Box} from "@mui/system";
import {FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import {useState} from "react";

export default function PinNumberField({value, onChange})
{
    const [showPIN, setShowPIN] = useState(null);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    }

    const handleClickShowPassword = () => {
        setShowPIN((show) => !show);
    }


    return (
        <div>
            <Box sx={{display: 'flex', flexWrap: 'wrap'}}>
                <FormControl sx={{ m: 1, width: '25ch'}} variant="outlined">
                    <InputLabel htmlFor="outlined-adornment-password">PIN</InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-password"
                        type={showPIN ? 'text' : 'password'}
                        value={value}
                        onChange={onChange}
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowPassword}
                                    onMouseDown={handleMouseDownPassword}
                                    edge="end"
                                >
                                    {showPIN ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        }
                        label="Password"
                        inputProps = {{
                            maxLength: 6
                        }}

                        sx={{height: '55px'}}
                    />
                </FormControl>
            </Box>
        </div>
    );

}