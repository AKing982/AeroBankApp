import {useState} from "react";
import UserTextField from "./UserTextField";
import DBPasswordField from "./DBPasswordField";
import PinNumberField from "./PinNumberField";
import CheckBoxIcon from "./CheckBoxIcon";
import PasswordField from "./PasswordField";
import RoleSelectBox from "./RoleSelectBox";
import UserList from "./UserList";

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

    const handleRoleChange = (event) => {
        setRole(event.target.value);
    }


    return (
        <div className="user-setup-container">
            <header className="user-setup-header">
            </header>
            <div className="user-setup-left">
                <div className="user-setup-firstName">
                    <UserTextField label="First Name" value={firstName} onChange={handleFirstNameChange} />
                </div>
                <div className="user-setup-lastName">
                    <UserTextField label="Last Name" value={lastName} onChange={handleLastNameChange}/>
                </div>
                <div className="user-setup-username">
                    <UserTextField label="User Name" value={username} onChange={handleUserNameChange}/>
                </div>
                <div className="user-setup-email">
                    <UserTextField label="Email" value={email} onChange={handleEmailChange}/>
                </div>
                <div className="user-setup-pinNumber">
                    <PinNumberField value={pinNumber} onChange={handlePINChange} />
                </div>
                <div className="user-setup-admin-checkbox">
                    <CheckBoxIcon label="Is Admin"/>
                </div>
                <div className="user-setup-password">
                    <DBPasswordField />
                </div>
                <div className="user-setup-confirm-password">
                    <PasswordField label="Confirm Password" value={confirmPassword} onChange={handlePasswordChange}/>
                </div>
                <div className="user-setup-role-combobox">
                    <RoleSelectBox value={role} onChange={handleRoleChange}/>
                </div>
            </div>
            <div className="user-setup-right">
                <div className="user-setup-list">

                </div>
            </div>
            <div className="user-setup-footer">
            </div>
        </div>
    );
}