import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "./Header";
import '../LoginForm.css';
import AlertDialog from "./CustomAlert";
import '../CustomAlert.css';
import {Spinner} from "./Spinner";
import LoginAlert from "./LoginAlert";
import BasicButton from "./BasicButton";
import {
    Alert, Backdrop,
    Button,
    Card,
    CardContent,
    CircularProgress,
    IconButton,
    InputAdornment,
    TextField,
    Typography
} from "@mui/material";
import {Box} from "@mui/system";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import backgroundImage from '../background.jpg';
import axios from "axios";

export default function LoginFormOLD()
{
    const [userID, setUserID] = useState(0);
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const isLoginButtonEnabled = !username && !password;
    const [dialogMessage, setDialogMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const [showBackdrop, setShowBackdrop] = useState(false);
    const [loginAttempts, setLoginAttempts] = useState(0);
    const [loginSuccess, setLoginSuccess] = useState(0);
    const [userIsActive, setUserIsActive] = useState(0);
    const [currentLoginTime, setCurrentLoginTime] = useState('');

    const overlayStyle = {
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.5)', // Semi-transparent black background
        display: loading ? 'block' : 'none',
        zIndex: 1, // Ensure it's above other elements
    };

    const isValidCsrfToken = (csrfToken) => {
        if(typeof csrfToken === 'string' && csrfToken.trim() !== '' || csrfToken instanceof Promise)
        {
            console.error("CSRF Token is a Promise, not a resolved value");
        }
    }

    const saveUserNameToSession = (username) => {
        sessionStorage.setItem('username', username);
    }

    const makeRequestWithCsrf = async (url, method, body) => {
        let csrfToken = sessionStorage.getItem('csrfToken');

        if(!csrfToken)
        {
            csrfToken = await fetchCsrfToken();
            if(!csrfToken)
            {
                console.error("Unable to fetch CSRF Token");
                return;
            }
        }

        try
        {
            return await fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: JSON.stringify(body)
            });
        }catch(error)
        {
            console.error("Error making request: ", error);
        }
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

    const circularProgressStyle = {
        color: 'blue', // Change the color of CircularProgress
        size: 90, // Increase the size to make it brighter
    };

    function sendUserLogRequest(userID, loginSuccess, isActive)
    {
        const currentTime = new Date();
        const userLogData = {
            userID: userID,
            lastLogin: currentTime.toLocaleTimeString(),
            lastLogout: '',
            sessionDuration: 0,
            loginSuccess: loginSuccess,
            loginAttempts: loginAttempts,
            isActive: isActive
        }

        console.log('UserLog Request: ', userLogData);

        return axios.post(`http://localhost:8080/AeroBankApp/api/session/addUserLog`, userLogData)
            .then(response => {
                console.log('User Log Data Successfully posted...');
            })
            .catch(error => {
                console.error('Unable to send the User Log POST due to the error: ', error);
            });
    }

    const fetchUserID = async (username) => {

        try
        {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/id/${username}`);

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
        const currentLoginTime = new Date();
        sessionStorage.setItem('loginISOTime', loginISOTime);
        sessionStorage.setItem('loginTime', loginTime);
        sessionStorage.setItem('currentLoginTime', currentLoginTime.toLocaleString());

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
                    navigateToAuthPage();

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

    const navigateToAuthPage = () => {
        navigate('/mfa/authenticate');
    }

    const navigateToHomePage = () => {
        navigate('/home')
    }

    const handleForgotPassword = () => {
        navigate("/forgot-password");
    }

    const navigateToRegister = () => {
        navigate('/registration')
    }

    const handleRegister = (event) => {
        event.preventDefault();

    }

    const handleClickShowPassword = () => {
        setShowPassword(!showPassword);
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };



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