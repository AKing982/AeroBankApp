import {useEffect, useState} from "react";
import MailServerField from "./MailServerField";
import PortField from "./PortField";
import EmailRadioGroup from "./EmailRadioGroup";
import TLSCheckBox from "./TLSCheckBox";
import EmailUserField from "./EmailUserField";
import DBPasswordField from "./DBPasswordField";
import axios from "axios";
import BasicButton from "./BasicButton";
import TestEmailField from "./TestEmailField";
import TestEmailButton from "./TestEmailButton";
import '../EmailSettings.css';
import PasswordField from "./PasswordField";
import {Alert, Snackbar} from "@mui/material";
export default function EmailSettings() {
    const [host, setHost] = useState(null);
    const [port, setPort] = useState(null);
    const [isTLSEnabled, setISTLSEnabled] = useState(false);
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [testEmail, setTestEmail] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [testButtonClicked, setTestButtonClicked] = useState(null);
    const [fromEmail, setFromEmail] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [snackbarOpen, setSnackBarOpen] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');



    const handlePortChange = (event) => {
        setPort(event.target.value);
    }

    const handleTestEmailChange = (event) => {
        setTestEmail(event.target.value);
    }

    const handleTestButtonClicked = (event) => {
        setTestButtonClicked(event.target.value);
    }

    const handleFromEmailChange = (event) => {
        setFromEmail(event.target.value);
    }

    const handleUserNameChange = (event) => {
        setUserName(event.target.value);
    }


    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    }

    const handleHostChange = (event) => {
        setHost(event.target.value);
    }

    const handleSaveSubmit = () => {

        console.log('Password: ', password);
        console.log('Confirm Password: ', confirmPassword);
        if (!password)
        {
            setSnackBarOpen(true);
            setSnackBarMessage("Password is empty... Please enter a password.");
            return;
        }
        if(!confirmPassword)
        {
            setSnackBarOpen(true);
            setSnackBarMessage("Confirm password is empty.... Please enter password.");
            return;
        }
        if (password !== confirmPassword) {
            setSnackBarOpen(true);
            setSnackBarMessage("Password's do not match.");
            console.error('Passwords do not match');
            return;
        }



        console.log('UserName: ', username);

        sendEmailRequest(host, port, username, password)
            .then(response => {
                console.log('Request sent successfully: ', response);
            })
            .catch(error => {
                console.error('An error has occurred with the request: ', error);
            })
    };

    const handleCloseSnackBar = (event, reason) => {
        if(reason === 'clickaway')
        {
            return;
        }
        setSnackBarOpen(false);
    }

    const fetchEmailServerData = async () => {

    }


    function sendEmailRequest(host, port, username, password)
    {
        const request = {
            host: host,
            port: port,
            username: username,
            password: password,
        };

        console.log('Request Data: ', request);

        return axios.post(`http://localhost:8080/AeroBankApp/api/email/add`, request)
                .then(response => {
                    console.log('Email request sent successfully', response);
                })
                .catch(error => {
                    console.error('Error sending email request', error);
                });
    }


    return (
        <div className="email-settings-container">
            <header className="email-settings-header">
            </header>
            <div className="email-settings-panel">
                <div className="email-type-radio-group">
                    <EmailRadioGroup />
                </div>
                <div className="outgoing-mail-server-port">
                    <MailServerField value={host} onChange={handleHostChange}/>
                    <PortField value={port} onChange={handlePortChange}/>
                </div>
                <div className="email-TLS-checkbox">
                    <TLSCheckBox />
                </div>
                <div className="email-username-field">
                    <EmailUserField value={username} onChange={handleUserNameChange}/>
                </div>
                <div className="email-password-field">
                    <DBPasswordField value={password} onChange={handlePasswordChange}/>
                </div>
                <div className="confirm-password-field">
                    <PasswordField value={confirmPassword} onChange={handleConfirmPasswordChange} label="Confirm Password"/>
                </div>
                <div className="test-connection-email">
                    <TestEmailField value={fromEmail} label="From Email" onChange={handleFromEmailChange}/>
                </div>
                <div className="save-connection-button">
                    <BasicButton text="Save" submit={handleSaveSubmit} />
                </div>
                <div className="test-email-field">
                    <TestEmailField value={testEmail} label="Test Email" onChange={handleTestEmailChange}/>
                </div>
                <div className="test-connection-email-button">
                    <TestEmailButton value={testButtonClicked} onChange={handleTestButtonClicked}/>
                </div>
            </div>
            <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleCloseSnackBar}>
                <Alert onClose={handleCloseSnackBar} severity="error">
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </div>
    );
}