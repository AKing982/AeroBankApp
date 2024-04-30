import {Box} from "@mui/system";
import {LinearProgress, TextField, Typography} from "@mui/material";
import {useState} from "react";
const evaluatePasswordStrength = (password) => {
    let strength = 0;
    if (!password) return strength;

    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumbers = /[0-9]/.test(password);
    const hasNonalphas = /\W/.test(password);

    if (hasUpperCase) strength += 25;
    if (hasLowerCase) strength += 25;
    if (hasNumbers) strength += 25;
    if (hasNonalphas) strength += 25;

    return strength;
};


function PasswordStrengthIndicator({password}) {

    const passwordStrength = evaluatePasswordStrength(password);
    const color = passwordStrength > 75 ? 'success' : passwordStrength > 50 ? 'warning' : 'error';

    return (
        <Box>
            <Box sx={{ mt: 2, width: '100%' }}>
                <LinearProgress variant="determinate" value={passwordStrength} color={color} />
                <Typography variant="caption" display="block" gutterBottom>
                    Password Strength: {passwordStrength}%
                </Typography>
            </Box>
        </Box>
    );
}

export default PasswordStrengthIndicator;