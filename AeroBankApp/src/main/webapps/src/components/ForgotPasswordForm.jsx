import {Box, Container} from "@mui/system";
import {Button, TextField, Typography} from "@mui/material";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import {useNavigate} from "react-router-dom";
import {useState} from "react";

export default function ForgotPasswordForm()
{
    const navigate = useNavigate();
    const [username, setUserName] = useState('');
    const [verificationCode, setVerificationCode] = useState('');
    const [showVerification, setShowVerification] = useState(false);

    const handleCancel = () => {
        setShowVerification(false);
        setUserName('');
        setVerificationCode('');
        navigate('/');
        console.log("Operation Cancelled");
    };


    const handleSubmit = (event) => {
        event.preventDefault();
        if (!showVerification) {
            if (username.trim()) {
                setShowVerification(true);
            }
        } else {
            console.log("Password reset request submitted with:", { username, verificationCode });
            // Proceed with backend submission or further validation
        }
    };

    return (
        <Container component="main" maxWidth="false" sx={{
            height: '100vh',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            backgroundImage: `url(${backgroundImage})`,
            backgroundSize: 'cover',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center',
        }}>
            <Box
                sx={{
                    backgroundColor: `rgba(255, 255, 255, 0.90)`,
                    borderRadius: 2,
                    p: 4,
                    boxShadow: 3,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    width: '100%',
                    maxWidth: '400px',
                }}
            >
                <Typography component="h1" variant="h5">
                    Forgot Password
                </Typography>
                <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1, width: '100%' }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        multiline
                        rows={1}
                        value={username}
                        onChange={(e) => setUserName(e.target.value)}
                        disabled={showVerification}
                    />
                    {showVerification && (
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="verificationCode"
                            label="Verification Code"
                            name="verificationCode"
                            autoComplete="off"
                            multiline
                            rows={1}
                            value={verificationCode}
                            onChange={(e) => setVerificationCode(e.target.value)}
                        />
                    )}
                    <Box sx={{ mt: 2, display: 'flex', justifyContent: 'space-between' }}>
                        <Button
                            type="button"
                            variant="outlined"
                            onClick={handleCancel}
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Cancel
                        </Button>
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            sx={{ mt: 3, mb: 2 }}
                            disabled={!username.trim() && !showVerification || (showVerification && !verificationCode.trim())}
                        >
                            {showVerification ? 'Submit' : 'Verify Username'}
                        </Button>
                    </Box>
                </Box>
            </Box>
        </Container>
    );
}