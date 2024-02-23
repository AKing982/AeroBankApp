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
 import {Alert, Snackbar} from "@mui/material";

export default function UserSetupSettings()
{
    const [firstName, setFirstName] = useState(null);
    const [lastName, setLastName] = useState(null);
    const [username, setUserName] = useState(null);
    const [email, setEmail] = useState(null);
    const [pinNumber, setPinNumber] = useState(null);
    const [accountNumber, setAccountNumber] = useState('');
    const [isAdmin, setIsAdmin] = useState(null);
    const [password, setPassword] = useState(null);
    const [confirmPassword, setConfirmPassword] = useState(null);
    const [branch, setBranch] = useState(null);
    const [role, setRole] = useState(null);
    const [validPasswords, setValidPasswords] = useState(false);
    const [userData, setUserData] = useState([]);
    const [saveClicked, setSaveClicked] = useState(null);
    const [saveDialogOpen, setSaveDialogOpen] = useState(false);
    const [accountName, setAccountName] = useState(null);
    const [balance, setBalance] = useState(null);
    const [accountType, setAccountType] = useState(null);
    const [selectedUser, setSelectedUser] = useState(null);
    const [snackbarOpen, setSnackBarOpen] = useState(false);
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [snackBarMessage, setSnackBarMessage] = useState('');


    const user = sessionStorage.getItem('username');

    const handleFirstNameChange = (event) => {
        setFirstName(event.target.value);
    }

    const handleLastNameChange = (event) => {
        setLastName(event.target.value);
    }

    const handleUserNameChange = (event) => {
        setUserName(event.target.value);
    }

    const handleUserSelection = (user) => {
        setSelectedUser(user);
        console.log('User Selected in UserList: ', user);

        fetchUserDetails(user);
    };

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    }

    const handlePINChange = (event) => {
        setPinNumber(event.target.value);
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    }

    const handleAccountTypeChange = (event) => {
        setAccountType(event.target.value);
    }

    const handleRoleChange = (event) => {
        console.log("role: ", event.target.value);
        setRole(event.target.value);
    }

    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    }

    const handleValidatePasswordFields = () => {
        if(password !== confirmPassword)
        {
            setValidPasswords(true);
        }
    }

    const handleCloseSnackBar = (event, reason) => {
        if(reason === 'clickaway')
        {
            return;
        }
        setSnackBarOpen(false);
    }

    function fetchUserAccountNumber(user)
    {
        return axios.get(`http://localhost:8080/AeroBankApp/api/users/account/${user}`)
            .then(response => {
                console.log('Fetching User Account Number : ', response.data.accountNumber);
                return response.data.accountNumber;
            })
            .catch(error => {
                console.error('Error fetching Account Number: ', error);
                return null;
            });

    }

    function fetchUserDetails(user)
    {
        return axios.get(`http://localhost:8080/AeroBankApp/api/users/${user}`)
            .then(response => {
                console.log('Response Object: ', response.data);
                const firstItem = response.data[0];
                setEmail(firstItem.email);
                setRole(firstItem.role);
                setUserName(firstItem.userName);
                setPinNumber(firstItem.pinNumber);
                setFirstName(firstItem.firstName);
                setLastName(firstItem.lastName);
            })
            .catch(error => {
                console.error('Error fetching User Details: ', error);
            });

    }

    useEffect(() => {
        axios.get(`http://localhost:8080/AeroBankApp/api/users/${user}`)
            .then(response => {
                console.log(response.data);
                setUserData(response.data);
            })
            .catch(error => {
                console.log('There was an error: ', error);
            })
    }, []);

    function sendSavedUser(userDataRequest)
    {
        return axios.post(`http://localhost:8080/AeroBankApp/api/users/save`, userDataRequest)
            .then(response => {
                console.log('Saving user to database successfully.');
                setSnackBarOpen(true);
                setSnackBarSeverity('success');
                setSnackBarMessage('Data was saved successfully.');
            })
            .catch(error => {
                console.error('Error sending POST request...');
                setSnackBarOpen(true);
                setSnackBarSeverity('error');
                setSnackBarMessage('There was an issue saving the user to the database...');
            });
    }

    const handleSaveButtonClick = async () => {

        // Fetch the accountNumber
        let accountNumber = await fetchUserAccountNumber(user);

        let userID = sessionStorage.getItem('userID');

        const userData = {
            userID: userID,
            firstName: firstName,
            lastName: lastName,
            user: username,
            email: email,
            pin: pinNumber,
            pass: password,
            role: role,
            accountNumber: accountNumber
        };

        console.log('User Data: ', userData);

        sendSavedUser(userData);
    }


    const parseRoleForPost = (role) => {
        let modifiedRole = null;
        if(role === 10)
        {
            setRole('USER');
        }
        else if(role === 20)
        {
            setRole('ADMIN');
        }
        else if(role === 30)
        {
            modifiedRole = 'MANAGER';
        }
        return modifiedRole;
    }

    const handleDialogOpen = () => {
        setSaveDialogOpen(true);
    }

    const handleAccept = () => {
        setSaveClicked(true);
        setSaveDialogOpen(false);
    }

    const handleDialogClose = () => {
        setSaveDialogOpen(false);
    }

    const saveNewUser = (event) => {
        setRole(event.data.role);
        setEmail(event.data.email)
    }


    return (
        <div className="user-setup-container">
            <header className="user-setup-header">
            </header>
            <div className="user-setup-left">
                <div className="user-setup-form-group">
                    <div className="user-setup-firstName">
                        <label htmlFor="setup-firstName" className="setup-firstName-label">First Name: </label>
                        <UserTextField label="First Name" value={firstName} onChange={handleFirstNameChange} />
                    </div>
                    <div className="user-setup-lastName">
                        <label htmlFor="setup-lastName" className="setup-lastName-label">Last Name: </label>
                        <UserTextField label="Last Name" value={lastName} onChange={handleLastNameChange}/>
                    </div>
                    <div className="user-setup-username">
                        <label htmlFor="setup-username" className="user-setup-username-label">User Name: </label>
                        <UserTextField label="User Name" value={username} onChange={handleUserNameChange}/>
                    </div>
                    <div className="user-setup-email">
                        <label htmlFor="setup-email" className="user-setup-email-label">Email: </label>
                        <UserTextField label="Email" value={email} onChange={handleEmailChange}/>
                    </div>
                    <div className="user-setup-pinNumber">
                        <label htmlFor="user-setup-PIN" className="setup-PIN-label">PIN: </label>
                        <PinNumberField value={pinNumber} onChange={handlePINChange} label="PIN" />
                    </div>
                    <div className="user-setup-password">
                        <label htmlFor="setup-password" className="setup-password-label">Password: </label>
                        <PasswordField label="Password" value={password} onChange={handlePasswordChange} isValidPassword={handleValidatePasswordFields} />
                    </div>
                    <div className="user-setup-confirm-password">
                        <label htmlFor="confirm-pass" className="confirm-password-label" >Confirm Password: </label>
                        <PasswordField label="Confirm Password" value={confirmPassword} onChange={handleConfirmPasswordChange}/>
                    </div>
                    <div className="user-setup-role-combobox">
                        <label htmlFor="role-combo" className="user-setup-role-label">Select Role: </label>
                        <RoleSelectBox value={role} onChange={handleRoleChange}/>
                    </div>
                    <div className="user-setup-save-button">
                        <BasicButton submit={handleSaveButtonClick} text="Save"/>
                    </div>
                    <SaveUserDialog open={saveDialogOpen} handleClose={handleDialogClose} handleAccept={handleAccept} />
                </div>
                <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleCloseSnackBar}>
                    <Alert onClose={handleCloseSnackBar} variant="filled" severity={snackBarSeverity}>
                        {snackBarMessage}
                    </Alert>
                </Snackbar>
                <div className="user-setup-right">
                    <div className="user-setup-list">
                        <label htmlFor="Current Users" className="current-users-label">Current Users</label>
                        <UserList onUserSelect={handleUserSelection}/>
                    </div>
                </div>
                <div className="user-setup-footer">
                </div>
                </div>

        </div>
    );
}