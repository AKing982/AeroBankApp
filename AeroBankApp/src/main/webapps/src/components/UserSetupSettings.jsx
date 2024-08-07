 import React, {useEffect, useState} from "react";
import UserTextField from "./UserTextField";
import DBPasswordField from "./DBPasswordField";
import PinNumberField from "./PinNumberField";
import CheckBoxIcon from "./CheckBoxIcon";
import PasswordField from "./PasswordField";
import RoleSelectBox from "./RoleSelectBox";
import UserList from "./UserList";
import BasicButton from "./BasicButton";
import './UserSetupSettings.css';
import axios from "axios";
import SaveUserDialog from "./SaveUserDialog";
import NumberField from "./NumberField";
import AccountTypeSelect from "./AccountTypeSelect";
 import {
    Alert,
    Button,
    FormControl,
    Grid, IconButton,
    InputAdornment,
    InputLabel,
    MenuItem,
    Select,
    Snackbar,
    TextField, Typography
 } from "@mui/material";
 import DialogTitle from "@mui/material/DialogTitle";
 import DialogContent from "@mui/material/DialogContent";
 import Dialog from "@mui/material/Dialog";
 import DialogActions from "@mui/material/DialogActions";
 import {Box} from "@mui/system";
 import {Visibility, VisibilityOff} from "@mui/icons-material";

export default function UserSetupSettings()
{
    const [userDetails, setUserDetails] = useState({
        userID: 0,
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        pinNumber: '',
        password: '',
        confirmPassword: '',
        role: 'User'
    });

    const [showPassword, setShowPassword] = useState(false);
    const [showConfirmPassword, setShowConfirmPassword] = useState(false);
    const [showPinNumber, setShowPinNumber] = useState(false);

    const [userValidation, setUserValidation] = useState({
        validPasswords: false,
        snackbarOpen: false,
        snackbarSeverity: 'error',
        snackbarMessage: ''
    });

    const [saveDialogOpen, setSaveDialogOpen] = useState(false);
    const [dialogBtnTitle, setDialogBtnTitle] = useState('');
    const [dialogMessage, setDialogMessage] = useState('');
    const [dialogTitle, setDialogTitle] = useState('');
    const [openDialog, setOpenDialog] = useState(false);
    const [dialogAction, setDialogAction] = useState(() => () => setOpenDialog(false));


    const handleChange = (event) => {
        const { name, value } = event.target;
        setUserDetails(prevDetails => ({
            ...prevDetails,
            [name]: value
        }));
    };

    const togglePasswordVisibility = () => {
        setShowPassword(!showPassword);
    }

    const toggleConfirmPasswordVisibility = () => {
        setShowConfirmPassword(!showConfirmPassword);
    }

    const togglePinNumberVisibility = () => {
        setShowPinNumber(!showPinNumber);
    }

    const handleSaveUser = () => {
        // Add logic to handle user save
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };


    const fetchUserData = async (username) => {
        try {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/${username}`);
            const userData = response.data[0];
            console.log('User Data: ', userData);
            console.log("First Name: ", userData.firstName);
            console.log("Last Name: ", userData.lastName);
            console.log("UserName: ", userData.username);
            setUserDetails({
                userID: userData.userID,
                firstName: userData.firstName || '',
                lastName: userData.lastName || '',
                username: userData.userName || '',
                email: userData.email || '',
                role: userData.role || 'User'
            });
        } catch (error) {
            console.error('Error fetching user details:', error);
        }
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleOpenRemoveDialog = () => {
        setDialogTitle("Confirm Deletion");
        setDialogMessage(`Are you sure you want to delete the user ${userDetails.username}? This action cannot be undone.`);
        setDialogBtnTitle("Delete");
        setOpenDialog(true);
        setDialogAction(() => () => sendDeleteRequestToServer(userDetails.userID));
    };


    const sendDeleteRequestToServer = async (userID) => {
        try {
            const response = await axios.delete(`http://localhost:8080/AeroBankApp/api/settings/delete-user/${userID}`)
            if(response.status === 200 || response.status === 201){
                console.log('Delete request was successful.');
                setOpenDialog(false);
                setUserValidation({
                    snackbarOpen: true,
                    snackbarMessage: `User ${userDetails.username} was successfully deleted.`,
                    snackbarSeverity: 'success'
                });

            }else{
                console.log('Delete request failed with status: ', response.status);
            }

        }catch(error){
            console.error('There was an error while sending the delete request to the server: ', error);
            setUserValidation({
                snackbarOpen: true,
                snackbarMessage: 'Failed to delete the user. Please try again.',
                snackbarSeverity: 'error'
            });
        }
    }

    useEffect(() => {
        console.log("Updated User Details: ", userDetails);
    }, [userDetails]);

    return (
        <Grid container spacing={2}>
            <Grid item xs={12} md={3}>
                <TextField
                    label="First Name"
                    variant="outlined"
                    fullWidth
                    multiline
                    rows={1}
                    margin="normal"
                    value={userDetails.firstName}
                    onChange={handleChange}
                    name="firstName"
                />
                <TextField
                    label="Last Name"
                    variant="outlined"
                    fullWidth
                    multiline
                    rows={1}
                    margin="normal"
                    value={userDetails.lastName}
                    onChange={handleChange}
                    name="lastName"
                />
                <TextField
                    label="Username"
                    variant="outlined"
                    fullWidth
                    multiline
                    rows={1}
                    margin="normal"
                    value={userDetails.username}
                    onChange={handleChange}
                    name="username"
                />
                <TextField
                    label="Email"
                    variant="outlined"
                    multiline
                    rows={1}
                    fullWidth
                    margin="normal"
                    value={userDetails.email}
                    onChange={handleChange}
                    name="email"
                />
                <TextField
                    label="PIN"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.pinNumber}
                    onChange={handleChange}
                    type="password"
                    name="pinNumber"
                    sx={{ fieldset: { height: 55 } }}
                    InputProps={{
                        style: {
                            alignItems: 'center'
                        },
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={togglePinNumberVisibility}
                                    onMouseDown={handleMouseDownPassword}
                                    edge="end"
                                >
                                    {showPinNumber ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}
                    inputProps={{
                        style: {
                            height: '30px',
                            padding: '10px',
                        },
                    }}
                />
                <TextField
                    label="Password"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.password}
                    onChange={handleChange}
                    type="password"
                    name="password"
                    sx={{ fieldset: { height: 55 } }}
                    InputProps={{
                        style: {
                            alignItems: 'center'
                        },
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                    aria-label="toggle password visibility"
                                    onClick={togglePasswordVisibility}
                                    onMouseDown={handleMouseDownPassword}
                                    edge="end"
                                >
                                    {showPassword ? <VisibilityOff /> : <Visibility />}
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}
                    inputProps={{
                        style: {
                            height: '30px',
                            padding: '10px',
                        },
                    }}
                />
                <TextField
                    label="Confirm Password"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.confirmPassword}
                    onChange={handleChange}
                    type="password"
                    name="confirmPassword"
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
                            height: '30px',
                            padding: '10px',
                        },
                    }}
                />
                <FormControl fullWidth margin="normal">
                    <InputLabel>Role</InputLabel>
                    <Select
                        label="Role"
                        value={userDetails.role}
                        onChange={handleChange}
                        name="role"
                    >
                        <MenuItem value="ADMIN">Admin</MenuItem>
                        <MenuItem value="USER">User</MenuItem>
                        <MenuItem value="AUDITOR">Auditor</MenuItem>
                    </Select>
                </FormControl>
                <Button variant="contained" color="primary" onClick={handleSaveUser}>Save</Button>
            </Grid>
            <Grid item xs={12} md={4}>
                <UserList onUserSelect={(user) => fetchUserData(user)} />
                <Box display="flex" justifyContent="center" mt={2} gap={2} width="100%">
                    <Button variant="contained" onClick={() => {/* logic to add user */}}>Add User</Button>
                    <Button variant="contained" sx={{ backgroundColor: 'red', '&:hover': { backgroundColor: 'darkred' } }} onClick={handleOpenRemoveDialog}>Remove User</Button>
                </Box>
            </Grid>
            <Snackbar open={userValidation.snackbarOpen} autoHideDuration={6000} onClose={() => setUserValidation({ ...userValidation, snackbarOpen: false })}>
                <Alert severity={userValidation.snackbarSeverity}>
                    {userValidation.snackbarMessage}
                </Alert>
            </Snackbar>
            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>{dialogTitle}</DialogTitle>
                <DialogContent>
                    {dialogMessage}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDialog}>Cancel</Button>
                    <Button onClick={dialogAction} color="primary" autoFocus>
                        {dialogBtnTitle}
                    </Button>
                </DialogActions>
            </Dialog>
        </Grid>
    );
}