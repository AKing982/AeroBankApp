import {Button, CircularProgress, Paper, TextField, Typography} from "@mui/material";
import {Container} from "@mui/system";
import {useState} from "react";
import axios from "axios";

export default function TwoFactorAuthPage(){
    const [code, setCode] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleCodeChange = (event) => {
        setCode(event.target.value);
    };

    const verifyCode = async () => {
        setLoading(true);
        setError('');
        try {
            // Adjust the URL and data structure as needed for your backend
            const response = await axios.post('/api/verify-2fa', { code });
            console.log(response.data);
            // Redirect or update UI upon successful verification
            // For example, redirect to the dashboard page
            window.location.href = '/dashboard';
        } catch (error) {
            console.error('Verification error:', error);
            setError('Failed to verify the code. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container component="main" maxWidth="xs">
            <Paper elevation={3} style={{ padding: '20px', marginTop: '20px' }}>
                <Typography component="h1" variant="h5">
                    Two-Factor Authentication
                </Typography>
                <Typography variant="body1" color="textSecondary" style={{ marginTop: '10px' }}>
                    Please enter the verification code sent to your device.
                </Typography>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="code"
                    label="Verification Code"
                    name="code"
                    autoComplete="code"
                    autoFocus
                    value={code}
                    onChange={handleCodeChange}
                    error={!!error}
                    helperText={error}
                />
                <Button
                    type="button"
                    fullWidth
                    variant="contained"
                    color="primary"
                    style={{ margin: '20px 0' }}
                    onClick={verifyCode}
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={24} /> : 'Verify Code'}
                </Button>
            </Paper>
        </Container>
    );
}