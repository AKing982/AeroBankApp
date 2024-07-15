import {useNavigate} from "react-router-dom";
import React, {useState, FunctionComponent} from "react";
import axios, {AxiosResponse} from "axios";
import {Box, Container} from "@mui/system";
// @ts-ignore
import backgroundImage from "./images/pexels-julius-silver-753325.jpg";
import {Button, IconButton, InputAdornment, TextField, Typography} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import PasswordStrengthIndicator from "./PasswordStrengthIndicator";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import apiService from '../api/ForgotPasswordApiService';




const ForgotPasswordForm: FunctionComponent = () =>
{
    const navigate = useNavigate();
    const [username, setUserName] = useState<string>('');
    const [userExists, setUserExists] = useState<boolean>(false);
    const [verificationCode, setVerificationCode] = useState<string>('');
    const [showVerification, setShowVerification] = useState<boolean>(false);
    const [showVerificationField, setShowVerificationField] = useState<boolean>(false);
    const [newPassword, setNewPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');
    const [showPasswordFields, setShowPasswordFields] = useState<boolean>(false);
    const [isUsernameVerified, setIsUsernameVerified] = useState<boolean>(false);
    const [generatedValidationCode, setGeneratedValidationCode] = useState<string>('');
    const [isVerificationCodeValid, setIsVerificationCodeValid] = useState<boolean>(false);
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [showNewPassword, setShowNewPassword] = useState<boolean>(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState<boolean>(false);
    const [snackbarMessage, setSnackbarMessage] = useState<string>('');
    const [snackBarSeverity, setSnackBarSeverity] = useState<string>('error');
    const [openSnackbar, setOpenSnackbar] = useState<boolean>(false);
    const [openDialog, setOpenDialog] = useState<boolean>(false);
    const [dialogBtnTitle, setDialogBtnTitle] = useState<string>('');
    const [dialogMessage, setDialogMessage] = useState<string>('');
    const [dialogTitle, setDialogTitle] = useState<string>('');
    const [dialogAction, setDialogAction] = useState<() => void>(() => setOpenDialog(false));

    const handleCloseDialog = () => {
        setOpenDialog(false);
    }

   const buildPasswordResetRequest = (password: string, user: string) : {password: string, user: string} => {
        return {
            user: user,
            password: password
        };
   };

   const verifyVerificationCode = async () : Promise<void> => {
       setIsVerificationCodeValid(true);
   }

   const handleCancel = () : void => {
       setShowVerification(false);
       setUserName('');
       setVerificationCode('');
       navigate('/');
       console.log("Operation Cancelled");
   }

    const handleVerification = async () : Promise<void> => {
        // Placeholder for verification logic
        setShowPasswordFields(true); // Assume verification was successful
    };

    const handleClickShowPassword = (): void => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) : void => {
        event.preventDefault();
    };

    const toggleNewPasswordVisibility = () : void => {
        setShowNewPassword(!showNewPassword);
    };

    const toggleConfirmPasswordVisibility = () : void => {
        setShowConfirmPassword(!showConfirmPassword);
    };

    const handleDialogButtonClick = () : void => {
        // Navigate to the login page
        navigate('/');

        // Close the dialog
        setOpenDialog(false);
    };




    const handleCloseSnackBar = () : void => {
       setOpenSnackbar(false);
   }

   const handleGoToLogin = () : void => {
       navigate('/');
   };

   const validatePasswordMeetsRequirements = (password: string) : boolean => {
       let hasMinLength : boolean = password.length >= 8;
       let hasUpper : RegExp = /[A-Z]/;
       let hasLower : RegExp = /[a-z]/;
       let hasNumber : RegExp = /\d/;
       let hasSpecial : RegExp = /[!@#$%^&*(),.?":{}|<>]/;
       if(!hasMinLength){
           setDialogTitle("Weak Password Error");
           setDialogMessage(`Password must be at least ${hasMinLength} characters long.`);
           throw new Error("Password must be at least 8 characters long.");
       }
       if(!hasNumber.test(password) || !hasUpper.test(password) || !hasLower.test(password) || !hasSpecial.test(password)){
           setDialogTitle("Error");
           setDialogMessage("Password must include uppercase letters, lowercase letters, numbers, and special characters.");
           throw new Error("Password must include uppercase letters, lowercase letters, numbers, and special characters.");
       }
       return true;
   }

   const handleSubmit = async(event: React.FormEvent<HTMLFormElement>) : Promise<void> => {
       event.preventDefault();


       if (!isUsernameVerified) {
           const userExists = await apiService.verifyUsername();
           if (userExists) {
               const userEmail = await apiService.fetchUsersEmail(username);
               if (userEmail) {
                   const generatedCode = await apiService.generateValidationCode();
                   setGeneratedValidationCode(generatedCode);  // Store the generated code in state
                   const emailSent = await apiService.sendValidationCodeToEmail(generatedCode, userEmail);
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
           const codeIsValid = apiService.checkValidationCode(verificationCode, generatedValidationCode);
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
               const currentMatchesExistingPassword = await apiService.validateNewPassword(newPassword);
               if(!currentMatchesExistingPassword){
                   const passwordResetSuccess = await apiService.sendPasswordResetToServer(newPassword);
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
   }
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

export default ForgotPasswordForm;