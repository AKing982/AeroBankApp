import {
    Alert,
    Button,
    FormControl,
    FormControlLabel,
    InputLabel, MenuItem, Select,
    Snackbar,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {Container} from "@mui/system";
import React, {useEffect, useState} from "react";
import PasswordField from "./PasswordField";
import Password from "./Password";
import PasswordStrengthIndicator from "./PasswordStrengthIndicator";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogActions from "@mui/material/DialogActions";
import Dialog from "@mui/material/Dialog";

function UserRegistrationForm({activeStep, handleStepChange, formData, handleFormDataChange, handleSwitchChange}){

    const [isPasswordsValid, setIsPasswordsValid] = useState(false);
    const [openSnackBar, setOpenSnackBar] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [isNextButtonEnabled, setIsNextButtonEnabled] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    const [dialogBtnTitle, setDialogBtnTitle] = useState('');
    const [dialogMessage, setDialogMessage] = useState('');
    const [dialogTitle, setDialogTitle] = useState('');
    const [dialogAction, setDialogAction] = useState(() => () => setOpenDialog(false));

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    useEffect(() => {
        const { firstName, lastName, username, email, password, confirmPassword } = formData;
        // Check if all fields are not empty
        const fieldsAreFilled = firstName && lastName && username && email && password && confirmPassword;
        setIsNextButtonEnabled(fieldsAreFilled);
    }, [formData]); // Re-evaluate whenever formData changes


  // Assuming roles are fetched or defined statically
    const roles = [
        { code: 'USER', name: 'USER' },
        { code: 'TELLER', name: 'TELLER' },
        { code: 'MANAGER', name: 'MANAGER' },
        { code: 'AUDITOR', name: 'AUDITOR' },
        { code: 'ADMIN', name: 'ADMIN' }
    ];
    const handleNextButtonClick = (e) => {
        e.preventDefault();

        validatePasswordFields(formData.password, formData.confirmPassword);
        console.log("Password's match: ", isPasswordsValid);
        if(isPasswordsValid){
            handleStepChange(activeStep + 1);
        }
    };

    const handleSubmit = () => {

    };

    const handleChange = () => {

    };

    const handleCloseSnackbar = () => {
        setOpenSnackBar(false);
    }

    const validatePasswordFields = (password, confirmPassword) => {
        if(password === confirmPassword){
            if(!validatePasswordMeetsRequirements(password))
            {
                setOpenDialog(true);
                setDialogBtnTitle("Try Again");
                return;
            }else{
                setIsPasswordsValid(true);
            }
        }else{
            setIsPasswordsValid(false);
            setOpenDialog(true);
            setDialogTitle("Password Mismatch");
            setDialogMessage("The password and confirm password do not match. Please try again.");
            setDialogBtnTitle("Try Again");
           // setOpenSnackBar(true);
           // setSnackBarMessage("Password's don't match.");
           // setSnackBarSeverity('error');
        }
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


    return(
        <Container maxWidth="sm">
            <Typography variant="h6" align="center" gutterBottom>
                User Registration
            </Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="First Name"
                    name="firstName"
                    multiline
                    rows={1}
                    value={formData.firstName}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Last Name"
                    name="lastName"
                    multiline
                    rows={1}
                    value={formData.lastName}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Username"
                    name="username"
                    multiline
                    rows={1}
                    value={formData.username}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Email"
                    name="email"
                    type="email"
                    multiline
                    rows={1}
                    value={formData.email}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                {/* Role Selection */}
                <FormControl fullWidth margin="normal">
                    <InputLabel id="role-select-label">Role</InputLabel>
                    <Select
                        labelId="role-select-label"
                        id="role-select"
                        value={formData.role}
                        label="Role"
                        onChange={handleFormDataChange}
                        name="role"
                        required
                    >
                        {roles.map((role) => (
                            <MenuItem key={role.code} value={role.code}>{role.name}</MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <PasswordField
                    label="PIN"
                    name="pin"
                    value={formData.pin}
                    onChange={handleFormDataChange}
                    />
                <PasswordField
                    label="Password"
                    name="password"
                    value={formData.password}
                    onChange={handleFormDataChange}
                />
                <PasswordField
                    label="Confirm Password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleFormDataChange}
                    helperText="A strong password includes letters, numbers, and special characters."
                />
                <PasswordStrengthIndicator password={formData.password} />
                <Button
                    type="submit"
                    variant="contained"
                    color="primary"
                    fullWidth
                    size="large"
                    onClick={handleNextButtonClick}
                    disabled={!isNextButtonEnabled}
                >
                    Next
                </Button>
            </form>
            {/*<Snackbar*/}
            {/*    open={openSnackBar}*/}
            {/*    autoHideDuration={6000}*/}
            {/*    onClose={handleCloseSnackbar}*/}
            {/*    message="Password's don't match"*/}
            {/*>*/}
            {/*    <Alert onClose={handleCloseSnackbar} severity={snackBarSeverity} variant="filled" sx={{width: '100%'}}>*/}
            {/*        {snackBarMessage}*/}
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

export default UserRegistrationForm;