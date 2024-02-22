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
export default function EmailSettings()
{
    const [outgoingMailServer, setOutGoingMailServer] = useState(null);
    const [port, setPort] = useState(null);
    const [isTLSEnabled, setISTLSEnabled] = useState(false);
    const [username, setUserName] = useState(null);
    const [password, setPassword] = useState(null);
    const [testEmail, setTestEmail] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [testButtonClicked, setTestButtonClicked] = useState(null);
    const [fromEmail, setFromEmail] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');


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

    const handleConfirmPasswordChange = (event) => {
        setConfirmPassword(event.target.value);
    }

/*
    useEffect(() => {
        setIsLoading(true);
        axios.get('http://localhost:8080/AeroBankApp/api/email/data')
            .then(response =>{
                setUserName(response.data.username);
                setPort(response.data.port);
                setPassword(response.data.password);
            })
    })

 */

    return (
        <div className="email-settings-container">
            <header className="email-settings-header">
            </header>
            <div className="email-settings-panel">
                <div className="email-type-radio-group">
                    <EmailRadioGroup />
                </div>
                <div className="outgoing-mail-server-port">
                    <MailServerField />
                    <PortField value={port} onChange={handlePortChange}/>
                </div>
                <div className="email-TLS-checkbox">
                    <TLSCheckBox />
                </div>
                <div className="email-username-field">
                    <EmailUserField />
                </div>
                <div className="email-password-field">
                    <DBPasswordField/>
                </div>
                <div className="confirm-password-field">
                    <PasswordField value={confirmPassword} onChange={handleConfirmPasswordChange} label="Confirm Password"/>
                </div>
                <div className="test-connection-email">
                    <TestEmailField value={fromEmail} label="From Email" onChange={handleFromEmailChange}/>
                </div>
                <div className="save-connection-button">
                    <BasicButton text="Save" />
                </div>
                <div className="test-email-field">
                    <TestEmailField value={testEmail} label="Test Email" onChange={handleTestEmailChange}/>
                </div>
                <div className="test-connection-email-button">
                    <TestEmailButton value={testButtonClicked} onChange={handleTestButtonClicked}/>
                </div>
            </div>
        </div>
    );
}