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
import ResponsiveDialog from "./ResponsiveDialog";

export default function EmailSettings() {
    const [host, setHost] = useState('');
    const [port, setPort] = useState(0);
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
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');
    const [isTestDialogOpen, setIsTestDialogOpen] = useState(false);



    const handlePortChange = (event) => {
        setPort(event.target.value);
    }

    const handleTestEmailChange = (event) => {
        setTestEmail(event.target.value);
    }

    const handleDialogAgree = () => {
        setIsTestDialogOpen(false); // Close the dialog and proceed with test email logic
        // Add the logic to handle the "Agree" action here
    };

    const handleDialogClose = () => {
        setIsTestDialogOpen(false); // Close the dialog without proceeding
    };


    const handleTestButtonClick = () => {
        setIsTestDialogOpen(true);
        if(!fromEmail)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please enter a from Email');
            setSnackBarSeverity('error');
            return;
        }
        if(!testEmail)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please Enter a Test Email');
            setSnackBarSeverity('error');
            return;
        }

        sendTestEmailRequest(fromEmail, testEmail)
            .then(response => {
                console.log('Test Email successful');
                setSnackBarOpen(true);
                setSnackBarSeverity('success');
                setSnackBarMessage('Test Email Successfully sent to: ' + testEmail);
            })
            .catch(error => {
                console.error('Error sending test Email.');
                setSnackBarOpen(true);
                setSnackBarSeverity('error');
                setSnackBarMessage('Error Sending test email.');
            });
    }

    function sendTestEmailRequest(fromEmail, toEmail)
    {
        console.log('From Email: ', fromEmail);
        console.log('Test Email: ', toEmail);
        const testRequest = {
            fromEmail: fromEmail,
            testEmail: toEmail
        };

        console.log('Test Request: ', testRequest);

        return axios.post(`http://localhost:8080/AeroBankApp/api/email/testConnection`, testRequest)
            .then(response => {
                console.log('Test Connection request successfully sent.')
            })
            .catch(error => {

                console.error('Error sending test connection: ', error);
            });
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
            setSnackBarSeverity('error');
            setSnackBarMessage("Password is empty... Please enter a password.");
            return;
        }
        if(!confirmPassword)
        {
            setSnackBarOpen(true);
            setSnackBarSeverity('error');
            setSnackBarMessage("Confirm password is empty.... Please enter password.");
            return;
        }
        if (password !== confirmPassword) {
            setSnackBarOpen(true);
            setSnackBarMessage("Password's do not match.");
            setSnackBarSeverity('error');
            console.error('Passwords do not match');
            return;
        }

        console.log('UserName: ', username);

        sendMailServerRequest(host, port, username, password)
            .then(response => {
                console.log('Request sent successfully: ', response);
                setSnackBarOpen(true);
                setSnackBarSeverity('success');
                setSnackBarMessage('Saved Mail Server Changes Successful.');
            })
            .catch(error => {
                console.error('An error has occurred with the request: ', error);
                setSnackBarOpen(true);
                setSnackBarSeverity('error');
                setSnackBarMessage('Error saving details...');
            })
    };

    const handleCloseSnackBar = (event, reason) => {
        if(reason === 'clickaway')
        {
            return;
        }
        setSnackBarOpen(false);
    }

    useEffect(() => {
        setIsLoading(true);
        axios.get(`http://localhost:8080/AeroBankApp/api/email/${1}`)
            .then(response => {
                // Simulate a delay
                setTimeout(() => {
                    if (response.data && response.data.length > 0) {
                        const firstItem = response.data[0];
                        console.log('Response Data: ', response.data);
                        setPort(firstItem.port);
                        setHost(firstItem.host);
                        setUserName(firstItem.username);
                    }
                    setIsLoading(false);
                }, 2000); // Delay of 2000 milliseconds (2 seconds)
            })
            .catch(error => {
                console.error('Error Fetching Mail Server Data: ', error);
                setIsLoading(false);
            });
    }, []);

    function sendMailServerRequest(host, port, username, password)
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
                    <MailServerField value={host} onChange={handleHostChange} isLoading={isLoading}/>
                    <PortField value={port} onChange={handlePortChange} isLoading={isLoading}/>
                </div>
                <div className="email-TLS-checkbox">
                    <TLSCheckBox />
                </div>
                <div className="email-username-field">
                    <EmailUserField value={username} onChange={handleUserNameChange} isLoading={isLoading}/>
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
                    <TestEmailButton onChange={handleTestButtonClick} />
                </div>
            </div>
            <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleCloseSnackBar}>
                <Alert onClose={handleCloseSnackBar} variant="filled" severity={snackBarSeverity}>
                    {snackBarMessage}
                </Alert>
            </Snackbar>
        </div>
    );
}