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

export default function UserSetupSettings()
{
    const [firstName, setFirstName] = useState(null);
    const [lastName, setLastName] = useState(null);
    const [username, setUserName] = useState(null);
    const [email, setEmail] = useState(null);
    const [pinNumber, setPinNumber] = useState(null);
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

    useEffect(() => {
        if(saveClicked)
        {
            console.log('Request Data: ', newUserData);
            axios.post('http://localhost:8080/AeroBankApp/api/users/save', newUserData,
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }})
                .then(response => {
                    console.log('Response: ', response.data);
                })
                .catch(error => {
                    console.log('Error: ', error);
                    if (error.response) {
                        // The request was made and the server responded with a status code
                        // that falls out of the range of 2xx
                        console.log('Error data:', error.response.data);
                        console.log('Error status:', error.response.status);
                        console.log('Error headers:', error.response.headers);
                    } else if (error.request) {
                        // The request was made but no response was received
                        console.log('Error request:', error.request);
                    } else {
                        // Something happened in setting up the request that triggered an Error
                        console.log('Error message:', error.message);
                    }
                    console.log('Error config:', error.config)
                })
            setSaveClicked(false);
        }

    }, [saveClicked])




    const newUserData = {
        first_name: firstName,
        last_name: lastName,
        user: username,
        email: email,
        pin: 2222,
        pass: password,
        role: role,
        accountNumber:'36-21-21'
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
                        <PinNumberField value={pinNumber} onChange={handlePINChange} />
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
                        <BasicButton submit={handleDialogOpen} text="Save"/>
                    </div>
                    <SaveUserDialog open={saveDialogOpen} handleClose={handleDialogClose} handleAccept={handleAccept} />
                </div>
                <div className="user-setup-right">
                    <div className="user-setup-list">
                        <label htmlFor="Current Users" className="current-users-label">Current Users</label>
                        <UserList />
                    </div>
                </div>
                <div className="user-setup-footer">
                </div>
                </div>

        </div>
    );
}