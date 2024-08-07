import ServerField from "./ServerField";
import React, {useEffect, useState} from "react";
import PortField from "./PortField";
import '../DatabaseSettings.css';
import DatabaseNameField from "./DatabaseNameField";
import DBUserField from "./DBUserField";
import DBPasswordField from "./DBPasswordField";
import BasicButton from "./BasicButton";
import ConnectionGroup from "./ConnectionGroup";
import axios from "axios";
import {Alert, Backdrop, CircularProgress, Snackbar} from "@mui/material";
import ErrorDialog from "./ErrorDialog";

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
    const [backDropOpen, setBackDropOpen] = useState(false);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [errorTitle, setErrorTitle] = useState('');
    const [errorHeader, setErrorHeader] = useState('');
    const [errorContent, setErrorContent] = useState('');
    const [errorException, setErrorException] = useState('');

    const validatePort = (port) => {
        if(port < 0 || port > 65535)
        {
            setIsValidPort(true);
        }
    }

    const handleError = () => {
        setErrorMessage('Detailed error message goes here...');
        setIsDialogOpen(true);
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

    function sendConnectionRequest(server, port, name, user, password, dbType)
    {
        const connectionRequest = {
            dbServer: server,
            dbPort: port,
            dbName: name,
            dbUser: user,
            dbPass: password,
            dbType: dbType
        }

        console.log('Connection Request: ', connectionRequest);

        setBackDropOpen(true);
        setTimeout(() => {
            return axios.post(`http://localhost:8080/AeroBankApp/api/connections/connect`, connectionRequest)
                .then(response => {
                    console.log('Connection to the database was successful');
                    setSnackBarSeverity('success');
                    setSnackBarMessage('Connection to the database was successful');
                    setSnackBarOpen(true);
                })
                .catch(error => {
                    console.error('There was an error connecting to the database: ', error);
                    setSnackBarOpen(true);
                    setIsDialogOpen(true);
                    setErrorHeader('Database Error Dialog');
                    setErrorContent('Unable to Connect to the Database');
                    setErrorException("Unable to connect");
                    setErrorTitle('Database Connection Error');
                    setErrorMessage("There was an error connecting to the database");
                    setSnackBarSeverity('error');
                    setSnackBarMessage('There was an error connecting to the database.');
                })
                .finally(() => {
                    setBackDropOpen(false);
                });
        }, 6000);

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
                if(response.data)
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

    const handleConnectionButtonClick = () => {
        let dbTypeToLower = parseDatabaseTypeToLowerCase(dbType);

        validateConnectionInput(server, port, databaseName, username, password, dbTypeToLower);

        sendConnectionRequest(server, port, databaseName, username, password, dbTypeToLower);
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
        if(type === 'mysql')
        {
            dbType = 'MYSQL';
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
                    <BasicButton text="Connect" submit={handleConnectionButtonClick}/>
                </div>
                <ErrorDialog
                    open={isDialogOpen}
                    onClose={() => setIsDialogOpen(false)}
                    errorMessage={errorMessage}
                    headerText={errorHeader}
                    contentText={errorContent}
                    exceptionText={errorException}
                    title={errorTitle}
                />
                <Snackbar open={snackbarOpen} autoHideDuration={6000} onClose={handleCloseSnackBar}>
                    <Alert onClose={handleCloseSnackBar} variant="filled" severity={snackBarSeverity}>
                        {snackBarMessage}
                    </Alert>
                </Snackbar>
                <Backdrop
                    sx={{
                        zIndex: (theme) => theme.zIndex.drawer + 1,
                        color: '#fff',
                    }}
                    open={backDropOpen} // Show the backdrop conditionally
                >
                    <CircularProgress color="inherit" />
                </Backdrop>
            </div>
        </div>
    )
}