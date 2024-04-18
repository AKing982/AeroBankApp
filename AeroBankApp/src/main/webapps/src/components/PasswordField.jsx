import {useState} from "react";
import {Box} from "@mui/system";
import {FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";

export default function PasswordField({label, value, name, onChange, isValidPassword})
{
    const [showPassword, setShowPassword] = useState(false);

    const handleClickShowPassword = () => {
        setShowPassword((show) => !show);
    }

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const invalidPasswordStyle = {
        borderColor: 'red',
            '&:hover': {
            borderColor: 'darkred',
        },
    };

    const fieldStyle = {
        // Set specific height if needed, though it should normally not be required:
        // height: '56px', // Adjust this value as needed to match other fields
    };


    return (
            <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 2}}>
                <FormControl sx={{width: '100ch', ...(isValidPassword ? {} : invalidPasswordStyle), my: 1 }} variant="outlined">
                    <InputLabel htmlFor="outlined-adornment-password">{label}</InputLabel>
                    <OutlinedInput
                        id="outlined-adornment-password"
                        type={showPassword ? 'text' : 'password'}
                        value={value}
                        name={name}
                        onChange={onChange}
                        endAdornment={
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={handleClickShowPassword}
                                    onMouseDown={handleMouseDownPassword}
                                    edge="end"
                                >
                                    {showPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        }
                        label="Password"
                        sx={{height: '55px'}}
                    />
                </FormControl>
            </Box>
    );
}