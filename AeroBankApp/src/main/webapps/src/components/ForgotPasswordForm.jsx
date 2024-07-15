import {Box, Container} from "@mui/system";
import {Alert, Button, IconButton, InputAdornment, Snackbar, TextField, Typography} from "@mui/material";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import {useNavigate} from "react-router-dom";
import React, {useState} from "react";
import axios from "axios";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import PasswordStrengthIndicator from "./PasswordStrengthIndicator";

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
    const [showPassword, setShowPassword] = useState(false);
    const [showNewPassword, setShowNewPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [snackbarMessage, setSnackbarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    const [dialogBtnTitle, setDialogBtnTitle] = useState('');
    const [dialogMessage, setDialogMessage] = useState('');
    const [dialogTitle, setDialogTitle] = useState('');
    const [dialogAction, setDialogAction] = useState(() => () => setOpenDialog(false));

    const handleCloseDialog = (): void => {
        setOpenDialog(false);
    };


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

        const request = buildPasswordResetRequest(newPassword, username);

        try {
            const response = await axios.put(`http://localhost:8080/AeroBankApp/api/users/update-password`, request);
            if(response.status === 200 || response.status === 201){
                return true;
            }else{
                console.log('Failed to sent password reset request to the server, status: ', response.status);
                return false;
            }
        }catch(error){
            console.error('There was an error sending the password reset: ', error);
            return false;
        }
    }

    const buildPasswordResetRequest = (password, user) => {
        return {
            user: user,
            password: password
        };
    };

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
        console.log("Entered Code Type: ", typeof(code));
        console.log("Verification Code: ", verificationCode);
        let verificationCodeAsStr = String(verificationCode);
        console.log("Verification Code Type: ", typeof(verificationCode));
        return code === verificationCodeAsStr;
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

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const toggleNewPasswordVisibility = () => {
        setShowNewPassword(!showNewPassword);
    };

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleDialogButtonClick = () => {
        // Navigate to the login page
        navigate('/');

        // Close the dialog
        setOpenDialog(false);
    };

    const validateNewPassword = async (password) => {

        const request = buildPasswordVerificationRequest(password);
        try {
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/users/verify-password`, request)
            if(response.status === 200 || response.status === 201){
                return response.data.matches;
            }
        }catch(error){
            console.error('An error occurred while validating password match: ', error);
        }
    };

    const buildPasswordVerificationRequest = (newPassword) => {
        return {
            user: username,
            newPassword: newPassword
        };
    };

    const handleCloseSnackBar = () => {
        setOpenSnackbar(false);
    };

    const handleGoToLogin = () => {
        navigate('/');  // Navigate to login page
    };

    const validatePasswordMeetsRequirements = (password) => {
        const hasMinLength = password.length >= 8;
        const hasUpper = /[A-Z]/;
        const hasLower = /[a-z]/;
        const hasNumber = /\d/;
        const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/;

        if(password.length < hasMinLength){
            setDialogTitle("Weak Password");
            setDialogMessage(`Password must be at least ${hasMinLength} characters long.`);
            return false;
        }
        if(!hasNumber.test(password) || !hasUpper.test(password) || !hasLower.test(password) || !hasSpecial.test(password)){
            setDialogTitle("Weak Password");
            setDialogMessage("Password must include uppercase letters, lowercase letters, numbers, and special characters.");
            return false;
        }
       return true;
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
                setOpenDialog(true);
                setDialogTitle("UserName Not Found");
                setDialogMessage("We couldn't find the username in our system. Please try again with another user.");
                setDialogBtnTitle("Try again.");
                console.error('Username does not exist');
            }
        } else if (showVerificationField && !isVerificationCodeValid) {
            const codeIsValid = checkValidationCode(verificationCode, generatedValidationCode);
            console.log('Code is valid: ', codeIsValid);
            if (codeIsValid) {
                setIsVerificationCodeValid(true);
                setShowPasswordFields(true);
            } else {
                setOpenDialog(true);
                setDialogTitle("Verification Failed");
                setDialogMessage("We couldn’t verify your code. Please check your email and make sure it’s correct.");
                setDialogBtnTitle("Try Again");
                console.error('Invalid verification code');
            }
        } else if (isVerificationCodeValid) {

            if (newPassword === confirmPassword) {
                if(!validatePasswordMeetsRequirements(newPassword)){
                    setOpenDialog(true);
                    setDialogBtnTitle("Try Again");
                    return;
                }
                const currentMatchesExistingPassword = await validateNewPassword(newPassword);
                if(!currentMatchesExistingPassword){
                    const passwordResetSuccess = await sendPasswordResetToServer(newPassword);
                    if(passwordResetSuccess){
                        console.log('Password reset was a success');
                        setOpenDialog(true);
                        setDialogTitle("Password Reset Successfully");
                        setDialogMessage("Your password has been successfully reset. Please wait while we redirect you to the login page.");
                        setDialogBtnTitle("Go to login.");
                        setDialogAction(() => handleGoToLogin());
                    }else{
                        setOpenDialog(true);
                        setDialogTitle("Reset Failed");
                        setDialogMessage("Failed to reset your password. Please try again or contact support if the problem persists.");
                        setDialogBtnTitle("Try Again");
                        setDialogAction(() => handleCloseDialog());
                        return;
                    }
                }else{
                    setOpenDialog(true);
                    setDialogTitle("Use a different Password");
                    setDialogMessage("Your new password cannot be the same as your current password. Please choose a different password to ensure your account security.");
                    setDialogBtnTitle("Try again.");
                    return;
                }

            } else if(newPassword !== confirmPassword){
                setOpenDialog(true);
                setDialogTitle("Password Mismatch");
                setDialogMessage("The new password and confirm password do not match. Please try again.");
                setDialogBtnTitle("Try Again");
                return; // Stop further execution.

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
                    {!showVerificationField && !isVerificationCodeValid && (
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="username"
                            label="Username"
                            name="username"
                            multiline
                            rows={1}
                            autoComplete="username"
                            autoFocus
                            value={username}
                            onChange={(e) => setUserName(e.target.value)}
                        />
                    )}
                    {showVerificationField && !isVerificationCodeValid && (
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="verificationCode"
                            label="Verification Code"
                            name="verificationCode"
                            multiline
                            rows={1}
                            autoComplete="off"
                            value={verificationCode}
                            onChange={(e) => setVerificationCode(e.target.value)}
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
                                type={showNewPassword ? 'text' : 'password'}
                                value={newPassword}
                                onChange={(e) => setNewPassword(e.target.value)}
                                sx={{ fieldset: { height: 55 } }}
                                InputProps={{
                                    style: {
                                        alignItems: 'center'
                                    },
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={toggleNewPasswordVisibility}
                                                onMouseDown={handleMouseDownPassword}
                                                edge="end"
                                            >
                                                {showNewPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    ),
                                }}
                                inputProps={{
                                    style: {
                                        height: '40px',
                                        padding: '10px',
                                    },
                                }}
                            />
                            <TextField
                                margin="normal"
                                required
                                fullWidth
                                id="confirmPassword"
                                label="Confirm Password"
                                type={showConfirmPassword ? 'text' : 'password'}
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                helperText="A strong password includes letters, numbers, and special characters."
                                sx={{ fieldset: { height: 55 } }}
                                InputProps={{
                                    style: {
                                        alignItems: 'center'
                                    },
                                    endAdornment: (
                                        <InputAdornment position="end">
                                            <IconButton
                                                aria-label="toggle password visibility"
                                                onClick={toggleConfirmPasswordVisibility}
                                                onMouseDown={handleMouseDownPassword}
                                                edge="end"
                                            >
                                                {showConfirmPassword ? <VisibilityOff /> : <Visibility />}
                                            </IconButton>
                                        </InputAdornment>
                                    ),
                                }}
                                inputProps={{
                                    style: {
                                        height: '40px',
                                        padding: '10px',
                                    },
                                }}
                            />
                            <PasswordStrengthIndicator password={newPassword} />
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
            {/*<Snackbar*/}
            {/*    open={openSnackbar}*/}
            {/*    autoHideDuration={6000}*/}
            {/*    onClose={handleCloseSnackBar}*/}
            {/*    message="Alert"*/}
            {/*    >*/}
            {/*    <Alert onClose={handleCloseSnackBar} severity={snackBarSeverity} variant="filled" sx={{width: '100%'}}>*/}
            {/*        {snackbarMessage}*/}
            {/*    </Alert>*/}
            {/*</Snackbar>*/}
            <Dialog
                open={openDialog}
                onClose={handleCloseDialog}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{dialogTitle}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        {dialogMessage}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={dialogAction} color="primary">
                        {dialogBtnTitle}
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}
