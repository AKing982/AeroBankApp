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
    const [dbType, setDBType] = useState(null);
    const [connectionData, setConnectionData] = useState([]);

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
        axios.get('http://localhost:8080/AeroBankApp/api/connections/list')
            .then(response =>{
                setConnectionData(response.data);
                configureConnections(response.data);
                console.log('Data: ',response.data);

                if(response.data.length > 0)
                {
                    const connection = response.data[0];
                    configureConnections(connection);
                }
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation: ', error)
            })

    }, []);

    const parseDatabaseTypeToLowerCase = (type) => {
        let dbType = null;
        if(type === 'MYSQL')
        {
            dbType = 'mysql';
        }
        else if(type === 'SQLSERVER' || type === 'SSQL')
        {
            dbType = 'ssql';
        }
        else if(type === 'POSTGRESQL' || type === 'PSQL')
        {
            dbType = 'postgresql';
        }
        return dbType;

    }

    const handleRadioButtonChange = (event) => {
        setDBType(event.target.value);
    }

    const configureConnections = (connectionData) => {
        console.log('DBServer: ', connectionData.dbServer);
        console.log('DBName: ', connectionData.dbName);
        console.log('DBPort: ', connectionData.dbPort);
        console.log('DBUser: ', connectionData.dbUser);
        console.log('DBType: ', connectionData.dbType);
        setUserName(connectionData.dbUser);
        setDatabaseName(connectionData.dbName);
        setServer(connectionData.dbServer);
        setUserName(connectionData.dbUser);
        setPort(connectionData.dbPort);
        setDBType(connectionData.dbType);
    }

    return(
        <div className="database-settings-container">
            <header className="database-settings-header">
            </header>
            <div className="database-settings-panel">
                <div className="connection-type-radio-buttons">
                    <ConnectionGroup value={parseDatabaseTypeToLowerCase(dbType)} onChange={handleRadioButtonChange}/>
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