 import {useEffect, useState} from "react";
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
 import {Alert, Button, FormControl, Grid, InputLabel, MenuItem, Select, Snackbar, TextField} from "@mui/material";
 import DialogTitle from "@mui/material/DialogTitle";
 import DialogContent from "@mui/material/DialogContent";
 import Dialog from "@mui/material/Dialog";
 import DialogActions from "@mui/material/DialogActions";
 import {Box} from "@mui/system";

export default function UserSetupSettings()
{
    const [userDetails, setUserDetails] = useState({
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        pinNumber: '',
        password: '',
        confirmPassword: '',
        role: 'User'
    });

    const [userValidation, setUserValidation] = useState({
        validPasswords: false,
        snackbarOpen: false,
        snackbarSeverity: 'error',
        snackbarMessage: ''
    });

    const [saveDialogOpen, setSaveDialogOpen] = useState(false);

    const handleChange = (event) => {
        const { name, value } = event.target;
        setUserDetails(prevDetails => ({
            ...prevDetails,
            [name]: value
        }));
    };

    const handleSaveUser = () => {
        // Add logic to handle user save
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

    useEffect(() => {
        console.log("Updated User Details: ", userDetails);
    }, [userDetails]);

    return (
        <Grid container spacing={2}>
            <Grid item xs={12} md={8}>
                <TextField
                    label="First Name"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.firstName}
                    onChange={handleChange}
                    name="firstName"
                />
                <TextField
                    label="Last Name"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.lastName}
                    onChange={handleChange}
                    name="lastName"
                />
                <TextField
                    label="Username"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.username}
                    onChange={handleChange}
                    name="username"
                />
                <TextField
                    label="Email"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.email}
                    onChange={handleChange}
                    name="email"
                />
                <TextField
                    label="PIN Number"
                    variant="outlined"
                    fullWidth
                    margin="normal"
                    value={userDetails.pinNumber}
                    onChange={handleChange}
                    type="password"
                    name="pinNumber"
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
                <Box display="flex" justifyContent="center" mt={2} gap={2} sx={{ transform: 'translateX(-40px)' }}>
                    <Button variant="contained" onClick={() => {/* logic to add user */}}>Add User</Button>
                    <Button variant="contained" sx={{ backgroundColor: 'red', '&:hover': { backgroundColor: 'darkred' } }} onClick={() => {/* logic to remove user */}}>Remove User</Button>
                </Box>
            </Grid>
            <Snackbar open={userValidation.snackbarOpen} autoHideDuration={6000} onClose={() => setUserValidation({ ...userValidation, snackbarOpen: false })}>
                <Alert severity={userValidation.snackbarSeverity}>
                    {userValidation.snackbarMessage}
                </Alert>
            </Snackbar>
            <Dialog open={saveDialogOpen} onClose={() => setSaveDialogOpen(false)}>
                <DialogTitle>{"Confirm Save"}</DialogTitle>
                <DialogContent>
                    <p>Are you sure you want to save these details?</p>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setSaveDialogOpen(false)}>Cancel</Button>
                    <Button onClick={handleSaveUser} autoFocus>
                        Confirm
                    </Button>
                </DialogActions>
            </Dialog>
        </Grid>
    );
}