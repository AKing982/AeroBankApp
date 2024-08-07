import {useState} from "react";
import {Box} from "@mui/system";
import {FormControl, IconButton, InputAdornment, InputLabel, OutlinedInput, TextField} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";

export default function Password({label, value, name, onChange, isValidPassword}){

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
         height: '100px', // Adjust this value as needed to match other fields
    };


    return (
        <TextField
            fullWidth
            type={showPassword ? 'text' : 'password'}
            label={label}
            variant="outlined"
            value={value}
            name={name}
            margin="normal"
            required
            onChange={onChange}
            InputProps={{
                endAdornment: (
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
                ),
            }}
        />
    );
}