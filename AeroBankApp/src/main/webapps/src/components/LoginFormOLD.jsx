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
    CircularProgress, Grid,
    IconButton,
    InputAdornment, Link,
    TextField,
    Typography
} from "@mui/material";
import {Box} from "@mui/system";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';
import Logo from './images/aerobank3.jpg'
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
        const currentLoginTime = new Date().toLocaleString();
        sessionStorage.setItem('loginISOTime', loginISOTime);
        sessionStorage.setItem('loginTime', loginTime);
        sessionStorage.setItem('currentLoginTime', currentLoginTime);

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
        <Grid container sx={{
            height: '130vh',
            backgroundImage: `url(${backgroundImage})`,
            backgroundSize: 'cover',
            backgroundRepeat: 'no-repeat',
            justifyContent: {xs: 'center', lg: 'flex-end'},
            p: 2,
            overflowY: 'auto',
        }}>
            {/* Banner at the top spanning the full width */}
            <Grid item xs={12} sx={{
                display: 'flex',
                height: '100px',
                justifyContent: 'space-between',
                alignItems: 'center',
                padding: '0.5rem 1rem',
                backgroundColor: 'rgba(255, 255, 255, 0.95)',
                boxShadow: '0 2px 5px rgba(0,0,0,0.2)'
            }}>
                {/* Left part with logo and menu items */}
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <img src={Logo} alt="Mountain America Credit Union" style={{ height: '100px', marginRight: '2rem' }} />
                    <Link to="/accounts" style={{ marginRight: '1rem', textDecoration: 'none', color: 'inherit' }}>ACCOUNTS</Link>
                    <Link to="/loans" style={{ marginRight: '1rem', textDecoration: 'none', color: 'inherit' }}>LOANS & CARDS</Link>
                    {/* ... other links */}
                </Box>

                {/* Right part with locations and login */}
                <Box>
                    <Link to="/locations" style={{ textDecoration: 'none', color: 'inherit' }}>LOCATIONS</Link>
                    <Button variant="outlined" sx={{ marginLeft: '1rem' }}>LOG IN</Button>
                </Box>
            </Grid>

            <div style={overlayStyle}></div>

            <Grid item xs={12} container justifyContent="center" alignItems="center">
                <Grid item xs={11} sm={8} md={6} lg={4} container direction="column" alignItems="center" justifyContent="center">

                    <Card sx={{
                        backgroundColor: 'rgba(255, 255, 255, 0.5)',
                        backdropFilter: 'blur(30px)',
                        width: '100%',
                        maxWidth: '480px',
                    }}>
                        <CardContent>
                            <Typography variant="h5" component="div" gutterBottom sx={{ fontWeight: 'bold', textAlign: 'left' }}>
                                Sign In
                            </Typography>
                            <Backdrop
                                sx={{
                                    zIndex: (theme) => theme.zIndex.drawer + 1,
                                    color: '#fff',
                                }}
                                open={showBackdrop}
                            >
                                <CircularProgress color="inherit" />
                            </Backdrop>
                            {error && (
                                <Alert severity="error" sx={{
                                    backgroundColor: 'rgba(255, 255, 255, 0.5)',
                                    backdropFilter: 'blur(80px)',
                                    color: 'text.primary',
                                    border: '1px solid rgba(255, 0, 0, 0.5)',
                                }}>{error}</Alert>
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
                                            padding: '0 5px',
                                        }
                                    }}
                                    sx={{
                                        '& .MuiOutlinedInput-root': {
                                            height: '55px',
                                            alignItems: 'center',
                                        },
                                        '& .MuiInputLabel-outlined.MuiInputLabel-shrink': {
                                            transform: 'translate(14px, -6px) scale(0.75)',
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
                </Grid>
            </Grid>
            {/* Footer */}
            <Box sx={{ width: '100%', backgroundColor: 'lightgrey', color: 'white', py: 3, maxWidth: 'auto'}}>
                <Grid container spacing={2} justifyContent="center">
                    {/* Footer content structured into Grid items */}
                    <Grid item xs={12} sm={4} md={3}>
                        {/* Service Center Info */}
                    </Grid>
                    <Grid item xs={6} sm={4} md={2}>
                        {/* Useful Links */}
                    </Grid>
                    <Grid item xs={6} sm={4} md={2}>
                        {/* About Us */}
                    </Grid>
                    <Grid item xs={6} sm={4} md={2}>
                        {/* Help */}
                    </Grid>
                    <Grid item xs={6} sm={4} md={3}>
                        {/* Social Media Icons */}
                    </Grid>
                </Grid>
                <Typography variant="caption" display="block" textAlign="center" sx={{ mt: 2 }}>
                    © 2024, all rights reserved. AeroBank
                </Typography>
            </Box>
            {/* End Footer */}
        </Grid>
    );

}