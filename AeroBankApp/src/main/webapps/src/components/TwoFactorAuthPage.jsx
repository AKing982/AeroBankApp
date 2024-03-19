import {Button, CircularProgress, Paper, TextField, Typography} from "@mui/material";
import {Container} from "@mui/system";
import {useState} from "react";
import axios from "axios";
import backgroundImage from '../background.jpg';

export default function TwoFactorAuthPage(){
    const [contact, setContact] = useState('');
    const [code, setCode] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');

    const handleContactChange = (event) => {
        setContact(event.target.value);
    };

    const handleCodeChange = (event) => {
        setCode(event.target.value);
    };

    const verifyCode = async () => {
        setLoading(true);
        setError('');
        try {
            // Replace '/api/verify-2fa' with your actual verification endpoint
            // Ensure your backend expects and handles the 'contact' field appropriately
            const response = await axios.post('/api/verify-2fa', { contact, code });
            console.log(response.data);
            // Redirect or update UI upon successful verification
            window.location.href = '/dashboard';
        } catch (error) {
            console.error('Verification error:', error);
            setError('Failed to verify the code. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container component="main" maxWidth="auto" style={{ backgroundImage: `url(${backgroundImage})`, backgroundSize: 'cover', height: '100vh', overflow: 'auto' }}>
            <Paper elevation={3} style={{ padding: '20px', marginTop: '20px', backgroundColor: 'rgba(255,255,255,0.8)' }}>
                <Typography component="h1" variant="h5">
                    Two-Factor Authentication
                </Typography>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="contact"
                    label="Email or Phone Number"
                    name="contact"
                    autoComplete="contact"
                    autoFocus
                    value={contact}
                    onChange={handleContactChange}
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="code"
                    label="Verification Code"
                    name="code"
                    autoComplete="code"
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