import {Box, Container} from "@mui/system";
import {Button, TextField, Typography} from "@mui/material";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import {useNavigate} from "react-router-dom";
import {useState} from "react";
import axios from "axios";

export default function ForgotPasswordForm()
{
    const navigate = useNavigate();
    const [username, setUserName] = useState('');
    const [userExists, setUserExists] = useState(false);
    const [verificationCode, setVerificationCode] = useState('');
    const [showVerification, setShowVerification] = useState(false);
    const [showVerificationField, setShowVerificationField] = useState(false);
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [showPasswordFields, setShowPasswordFields] = useState(false);
    const [isUsernameVerified, setIsUsernameVerified] = useState(false);
    const [generatedValidationCode, setGeneratedValidationCode] = useState('');
    const [isVerificationCodeValid, setIsVerificationCodeValid] = useState(false);


    // Search the database for the username and if it exists allow the user to continue to the next segment
    const validateUserName = async (username) => {
        try{
            const validResponse = await axios.get(`http://localhost:8080/AeroBankApp/api/users/find/${username}`)
            if(validResponse.status === 200 || validResponse.status === 201){
                return validResponse.data.exists;
            }

        }catch(error){
            console.error('There was an error fetching username validation.', error);
        }
    }

    const generateValidationCode = async () => {
        try {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/validationCode/generate`)
            if(response.status === 200 || response.status === 201){
                return response.data;
            }

        }catch(error){
            console.error('There was an error fetching the generated validation code: ', error);
        }
    };

    const fetchUsersEmail = async (username) => {
        try{
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/email/${username}`)
            if(response.status === 200 || response.status === 201){
                return response.data.email;
            }

        }catch(error){
            console.error("There was an error fetching the user's email: ", error);
        }
    };

    const sendPasswordResetToServer = async (newPassword) => {
        try {
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/users/`)
        }catch(error){

        }
    }

    const sendValidationCodeToEmail = async (code, email) => {
        try {
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/validationCode/send-verification-email`, {
                email: email, // Email address to which the verification code is sent
                code: code   // Verification code to include in the email
            });

            if (response.status === 200) {
                console.log('Verification code sent successfully to ' + email);
                return true; // Indicates successful sending
            } else {
                console.log('Failed to send verification code, status: ' + response.status);
                return false; // Indicates failure
            }
        } catch (error) {
            console.error('Error occurred while sending the verification code: ', error);
            return false; // Handle errors and indicate failure
        }
    };

    const checkValidationCode = (code, verificationCode) => {
        console.log("Entered Code: ", code);
        console.log("Verification Code: ", verificationCode);
        if(code === verificationCode){
            return true;
        }
    };

    const verifyUsername = async () => {
        // Here you would replace with actual API call
        return await validateUserName(username);
    };

    const verifyVerificationCode = async () => {
        setIsVerificationCodeValid(true); // Simulate code verification success
    };

    const handleCancel = () => {
        setShowVerification(false);
        setUserName('');
        setVerificationCode('');
        navigate('/');
        console.log("Operation Cancelled");
    };

    const handleVerification = async () => {
        // Placeholder for verification logic
        setShowPasswordFields(true); // Assume verification was successful
    };


    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!isUsernameVerified) {
            const userExists = await verifyUsername();
            if (userExists) {
                const userEmail = await fetchUsersEmail(username);
                if (userEmail) {
                    const generatedCode = await generateValidationCode();
                    setGeneratedValidationCode(generatedCode);  // Store the generated code in state
                    const emailSent = await sendValidationCodeToEmail(generatedCode, userEmail);
                    if (emailSent) {
                        setIsUsernameVerified(true);
                        setShowVerificationField(true);
                        console.log("Verification code sent to " + userEmail);
                    } else {
                        console.error("Failed to send verification email.");
                    }
                } else {
                    console.error('User email not found');
                }
            } else {
                console.error('Username does not exist');
            }
        } else if (showVerificationField && !isVerificationCodeValid) {
            const codeIsValid = checkValidationCode(verificationCode, generatedValidationCode);
            if (codeIsValid) {
                setIsVerificationCodeValid(true);
                setShowPasswordFields(true);
            } else {
                console.error('Invalid verification code');
            }
        } else if (isVerificationCodeValid) {
            if (newPassword === confirmPassword) {
                console.log("Password reset request submitted with:", { username, newPassword });
                navigate('/'); // Redirect to login after successful password reset
            } else {
                console.error("Passwords do not match");
            }
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
                        multiline
                        rows={1}
                        autoFocus
                        value={username}
                        onChange={(e) => setUserName(e.target.value)}
                        disabled={isUsernameVerified}
                    />
                    {showVerificationField && (
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
                            disabled={isVerificationCodeValid}
                        />
                    )}
                    {isVerificationCodeValid && (
                        <>
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="newPassword"
                                label="New Password"
                                type="password"
                                multiline
                                rows={1}
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                            />
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="confirmPassword"
                                label="Confirm Password"
                                multiline
                                rows={1}
                                type="password"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                            />
                        </>
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
                            disabled={!username.trim()}
                        >
                            {isVerificationCodeValid ? 'Reset Password' : (showVerificationField ? 'Verify Code' : 'Verify Username')}
                        </Button>
                    </Box>
                </Box>
            </Box>
        </Container>
    );
}
