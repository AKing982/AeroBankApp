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
import {useEffect, useState} from "react";
import PasswordField from "./PasswordField";
import Password from "./Password";

function UserRegistrationForm({activeStep, handleStepChange, formData, handleFormDataChange, handleSwitchChange}){

    const [isPasswordsValid, setIsPasswordsValid] = useState(false);
    const [openSnackBar, setOpenSnackBar] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [isNextButtonEnabled, setIsNextButtonEnabled] = useState(false);

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
            setIsPasswordsValid(true);
            setOpenSnackBar(true);
            setSnackBarMessage("Password's match");
            setSnackBarSeverity('success');
        }else{
            setIsPasswordsValid(false);
            setOpenSnackBar(true);
            setSnackBarMessage("Password's don't match.");
            setSnackBarSeverity('error');
        }
    }


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
            <Snackbar
                open={openSnackBar}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                message="Password's don't match"
            >
                <Alert onClose={handleCloseSnackbar} severity={snackBarSeverity} variant="filled" sx={{width: '100%'}}>
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </Container>
    );
}

export default UserRegistrationForm;