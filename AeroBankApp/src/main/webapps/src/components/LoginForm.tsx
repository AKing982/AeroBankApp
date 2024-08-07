// @ts-ignore
import React, { useState } from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {Box} from "@mui/system";

import {
    Alert,
    Backdrop, Button,
    Card,
    CardContent,
    CircularProgress, IconButton,
    InputAdornment,
    TextField,
    Typography
} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";

interface UserLogData {
    userID: number;
    lastLogin: Date;
    lastLogout: string;
    sessionDuration: number;
    loginSuccess: number;
    loginAttempts: number;
    isActive: number;
}

export default function LoginForm(){
    const navigate = useNavigate();

    // State definitions with TypeScript type annotations
    const [userID, setUserID] = useState<number>(0);
    const [username, setUserName] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
    const [dialogMessage, setDialogMessage] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const [showPassword, setShowPassword] = useState<boolean>(false);
    const [showBackdrop, setShowBackdrop] = useState<boolean>(false);
    const [loginAttempts, setLoginAttempts] = useState<number>(0);
    const [loginSuccess, setLoginSuccess] = useState<number>(0);
    const [userIsActive, setUserIsActive] = useState<number>(0);
    const [currentLoginTime, setCurrentLoginTime] = useState<string>('');

    const overlayStyle: React.CSSProperties = {
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent black background
        display: loading ? 'block' : 'none',
        zIndex: 1, // Ensure it's above other elements
    };

    const saveUserNameToSession = (username) => {
        sessionStorage.setItem('username', username);
    }

    const fetchCsrfToken = async () => {
        try
        {
            const response = await fetch("http://localhost:8080/csrf/token");
            if(!response.ok)
            {
                throw new Error(`Server Returned Status: ${response.status}`);
            }

            const token = await extractCsrFToken(response);
            saveCSRFToken(token);

            return token;

        }catch(error)
        {
            console.error('Error fetching CSRF Token: ', error)
        }
    }

    const extractCsrFToken = async (response) => {

        const data = await response.json();
        console.log("CSRF Token Extracted: ", data.token);
        return data.token;
    }

    const saveCSRFToken = (token) => {
        sessionStorage.setItem('csrfToken', token);
    }


    const saveJWTSession = (jwt) => {
        sessionStorage.setItem('jwtToken', jwt);
    }


    const authenticationResponse = async () => {

        return await fetch(`http://localhost:8080/AeroBankApp/api/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                //  'X-CSRF-TOKEN': token
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
    }


        const sendUserLogRequest = (userID: number, loginSuccess: number, isActive: number) => {
        const userLogData: UserLogData = {
            userID,
            lastLogin: new Date(),
            lastLogout: '',
            sessionDuration: 0,
            loginSuccess,
            loginAttempts,
            isActive
        };
        console.log('UserLog Request: ', userLogData);
        return axios
            .post(`http://localhost:8080/AeroBankApp/api/session/addUserLog`, userLogData)
            .then((response) => {
                console.log('User Log Data Successfully posted...');
            })
            .catch((error) => {
                console.error('Unable to send the User Log POST due to the error: ', error);
            });
    };
    const fetchUserID = async (username: string) => {
        try
        {
            const response  = await axios.get(`http://localhost:8080/AeroBankApp/api/users/id/${username}`);
            if(response.status === 200){
                const userID = response.data;
                console.log('Found UserID: ', userID);
                return userID;
            }else{
                console.log(`Request completed with status: ${response.status}`);
                return null;
            }
        }catch(error)
        {
            if(error.response){
                console.error(`Server responded with status ${error.response.status}: `, error.response.data);
            }else if(error.request){
                console.error('No response received for the request: ', error.request);
            }else{
                console.error('Error', error.message);
            }
            return null;
        }
    }
    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);
        setShowBackdrop(true);
        const loginTime = new Date().getTime().toString();
        const loginISOTime = new Date().toISOString();
        sessionStorage.setItem('loginISOTime', loginISOTime);
        sessionStorage.setItem('loginTime', loginTime);

        const userID = await fetchUserID(username);
        try{
            //   const csrfToken = await fetchCsrfToken();
            setTimeout(async () => {
                const response = await authenticationResponse();
                // Store the JWT Token in the sessionStorage

                if(response.ok)
                {
                    console.log('Login Successful');

                    const data = await response.json();
                    console.log(data);
                    const token = data.token;
                    const username = data.username;
                    saveJWTSession(token);
                    saveUserNameToSession(username);
                    navigateToHomePage();

                    setLoginAttempts(1);

                    // Create a User Log instance
                    console.log('Creating User Log for userID: ', userID);
                    sendUserLogRequest(userID, 1, 1);
                }
                else
                {
                    console.log('Login Failed');
                    setDialogMessage(`Incorrect Username or Password`);
                    setIsDialogOpen(true);
                    setError('Incorrect Username or Password');

                    setLoginAttempts((prevAttempts) => prevAttempts + 1);
                }

                setLoading(false);
                setShowBackdrop(false);

            }, 2000);

        }catch(error)
        {
            console.error("Network Error: ", error);
            setError('A network error occurred, please try again later.');
            setLoading(false);
            setShowBackdrop(false);
        }finally {
            setTimeout(() => setLoading(false), 2000);
        }
    };
    const navigateToHomePage = () => {
        navigate('/home')
    }
    const navigateToRegister = () => {
        navigate('/registration')
    }

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };
    const backgroundImage = require('../background.jpg');
    return (
        <Box sx={{ position: 'relative', display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', p: 2, backgroundImage:`url(${backgroundImage})` }}>
            <div style={overlayStyle}></div>
            <Card sx={{position: 'relative'}}>
                <CardContent>
                    <Typography variant="h5" component="div" gutterBottom sx={{fontWeight: 'bold', textAlign: 'left'}}>
                        Sign In
                    </Typography>
                    <Backdrop
                        sx={{
                            zIndex: (theme) => theme.zIndex.drawer + 1,
                            color: '#fff',
                        }}
                        open={showBackdrop} // Show the backdrop conditionally
                    >
                        <CircularProgress color="inherit" />
                    </Backdrop>
                    {error && (
                        <Alert severity="error">{error}</Alert>
                    )}
                    <Box
                        component="form"
                        noValidate
                        autoComplete="off"
                        onSubmit={handleSubmit}
                        sx={{ mt: 2 }}
                    >
                        <TextField
                            required
                            fullWidth
                            id="username"
                            label="User Name"
                            margin="normal"
                            value={username}
                            onChange={(e) => setUserName(e.target.value)}
                            variant="outlined"
                            InputProps={{
                                style: {
                                    height: '55px',
                                    padding: '0 5px', // Adjust horizontal padding if needed
                                    // You might not need to adjust the line height if the height and padding are set correctly
                                    // lineHeight: 'normal' // Adjust if the text isn't vertically centered
                                }
                            }}
                            // Adjust the height of the TextField, considering label and border
                            sx={{
                                '& .MuiOutlinedInput-root': {
                                    height: '55px', // Adjust to the desired input height
                                    alignItems: 'center', // This will vertically center the text field content
                                },
                                // Adjust the label position if needed
                                '& .MuiInputLabel-outlined.MuiInputLabel-shrink': {
                                    transform: 'translate(14px, -6px) scale(0.75)', // Adjust translation if needed
                                }
                            }}
                        />
                        <TextField
                            required
                            fullWidth
                            id="password"
                            label="Password"
                            type={showPassword ? 'text' : 'password'}
                            margin="normal"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            sx={{ fieldset: { height: 55 } }}
                            InputProps={{
                                style: {
                                    alignItems: 'center'
                                },
                                endAdornment: (
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            edge="end"
                                        >
                                            {showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                ),
                            }}
                            inputProps={{
                                style: {
                                    height: '40px',
                                    padding: '10px',
                                },
                            }}
                        />
                        <Box sx={{ mt: 2, display: 'flex', justifyContent: 'space-between' }}>
                            <Button variant="outlined" onClick={navigateToRegister}>
                                Register
                            </Button>
                            <Button
                                type="submit"
                                variant="contained"
                                color="primary"
                                disabled={!username || !password}
                            >
                                Login
                            </Button>
                        </Box>
                    </Box>
                </CardContent>
            </Card>
        </Box>
    );




}