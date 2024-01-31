import ServerField from "./ServerField";
import {useEffect, useState} from "react";
import PortField from "./PortField";
import '../DatabaseSettings.css';
import DatabaseNameField from "./DatabaseNameField";
import DBUserField from "./DBUserField";
import DBPasswordField from "./DBPasswordField";
import BasicButton from "./BasicButton";
import ConnectionGroup from "./ConnectionGroup";
import axios from "axios";

export default function DatabaseSettings()
{
    const [server, setServer] = useState(null);
    const [port, setPort] = useState(null);
    const [isValidPort, setIsValidPort] = useState(false);
    const [databaseName, setDatabaseName] = useState(null);
    const [username, setUserName] = useState(null);
    const [password, setPassword] = useState(null);

    useEffect(() => {

    }, []);

    const validatePort = (port) => {
        if(port < 0 || port > 65535)
        {
            setIsValidPort(true);
        }
    }

    const handlePortChange = (event) => {
        setPort(event.target.value);
    }

    const handleServerChange = (event) => {
        setServer(event.target.value);
    }

    const handleDatabaseNameChange = (event) => {
        setDatabaseName(event.target.value);
    }

    const handleUserNameChange = (event) => {
        setUserName(event.target.value);
    }

    useEffect(() => {
        axios.get('http://localhost:8080/AeroBankApp/api/connections')
            .then(response =>{
                setUserName(response.data.username);
                setPort(response.data.port);
                setServer(response.data.server);
                setDatabaseName(response.data.name);
                setPassword(response.data.password);
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error)
            })

    }, []);

    return(
        <div className="database-settings-container">
            <header className="database-settings-header">
            </header>
            <div className="database-settings-panel">
                <div className="connection-type-radio-buttons">
                    <ConnectionGroup/>
                </div>
                <div className="server-port-field">
                    <ServerField value={server} onChange={handleServerChange}/>
                    <PortField value={port} onChange={handlePortChange}/>
                </div>
                <div className="database-container-name">
                    <DatabaseNameField value={databaseName} onChange={handleDatabaseNameChange}/>
                </div>
                <div className="database-sql-credentials">
                    <DBUserField value={username} onChange={handleUserNameChange}/>
                    <DBPasswordField />
                </div>
                <div className="test-connection-button">
                    <BasicButton text="Test Connection"/>
                </div>
                <div className="connect-button">
                    <BasicButton text="Connect"/>
                </div>
            </div>
        </div>
    )
}