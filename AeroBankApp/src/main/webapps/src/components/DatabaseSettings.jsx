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
import {Alert, Snackbar} from "@mui/material";

export default function DatabaseSettings()
{
    const [server, setServer] = useState(null);
    const [port, setPort] = useState(null);
    const [isValidPort, setIsValidPort] = useState(false);
    const [databaseName, setDatabaseName] = useState(null);
    const [username, setUserName] = useState(null);
    const [password, setPassword] = useState('');
    const [dbType, setDBType] = useState(null);
    const [connectionData, setConnectionData] = useState([]);
    const [snackbarOpen, setSnackBarOpen] = useState(false);
    const [snackBarMessage, setSnackBarMessage] = useState('');
    const [snackBarSeverity, setSnackBarSeverity] = useState('error');

    const validatePort = (port) => {
        if(port < 0 || port > 65535)
        {
            setIsValidPort(true);
        }
    }

    function validateConnectionInput(server, port, name, user, pass, dbType)
    {
        if(!dbType)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please select a database type...');
            setSnackBarSeverity('error');
        }
        if(!server)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please enter a valid server name...');
            setSnackBarSeverity('error');
        }
        if(!port)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please enter a valid port...');
            setSnackBarSeverity('error');
        }

        if(!name)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please enter a valid database name...');
            setSnackBarSeverity('error');
        }

        if(!user)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please enter a valid username...');
            setSnackBarSeverity('error');
        }
        if(!pass)
        {
            setSnackBarOpen(true);
            setSnackBarMessage('Please enter a password...');
            setSnackBarSeverity('error');
        }

        validatePort(port);
    }

    function sendTestConnectionRequest(server, port, name, user, password, dbType) {
        const connectionRequest = {
            dbServer: server,
            dbPort: port,
            dbName: name,
            dbUser: user,
            dbPass: password,
            dbType: dbType
        }

        console.log('ConnectionRequest: ', connectionRequest);

        return axios.post(`http://localhost:8080/AeroBankApp/api/connections/testConnection`, connectionRequest)
            .then(response => {
                console.log('Test Connection was successful.');
                setSnackBarOpen(true);
                setSnackBarMessage("Test Connection was successful");
                setSnackBarSeverity('success');
            })
            .catch(error => {
                console.error('Test Connection has failed...');
                setSnackBarOpen(true);
                setSnackBarMessage('There was issue connecting to the database..');
                setSnackBarSeverity('error');
            });
    }

    const handleTestConnectionButtonClick = () => {

        let dbTypeToLowerCase = parseDatabaseTypeToLowerCase(dbType);
        // First Validate the Connection Input
        validateConnectionInput(server, port, databaseName, username, password, dbTypeToLowerCase);

        sendTestConnectionRequest(server, port, databaseName, username, password, dbTypeToLowerCase)
            .then(response => {
                console.log('Test Connection sent successfully.')
            })
            .catch(error => {
                console.error('Error sending test connection.');
            });
    }

    const handleCloseSnackBar = (event, reason) => {
        if(reason === 'clickaway')
        {
            return;
        }
        setSnackBarOpen(false);
    }

    const handlePortChange = (event) => {
        setPort(event.target.value);
    }

    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
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
        if(type === 'MYSQL' || type === 'mysql')
        {
            dbType = 'MySQL';
        }
        else if(type === 'SQLSERVER' || type === 'SSQL' || type === 'ssql')
        {
            dbType = 'SSQL';
        }
        else if(type === 'POSTGRESQL' || type === 'PSQL' || type === 'psql')
        {
            dbType = 'PSQL';
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
                    <ConnectionGroup value={dbType} onChange={handleRadioButtonChange}/>
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
                    <DBPasswordField value={password} onChange={handlePasswordChange}/>
                </div>
                <div className="test-connection-button">
                    <BasicButton text="Test Connection" submit={handleTestConnectionButtonClick}/>
                </div>
                <div className="connect-button">
                    <BasicButton text="Connect"/>
                </div>
                <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleCloseSnackBar}>
                    <Alert onClose={handleCloseSnackBar} variant="filled" severity={snackBarSeverity}>
                        {snackBarMessage}
                    </Alert>
                </Snackbar>
            </div>
        </div>
    )
}