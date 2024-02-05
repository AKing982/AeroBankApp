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
    Alert,
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

export default function LoginFormOLD()
{
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const isLoginButtonEnabled = !username && !password;
    const [dialogMessage, setDialogMessage] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);

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
      /**  if(!token)
        {
            console.error("CSRF Token is missing");
            return;
        }
          **/


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

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);

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
                }
                else
                {
                    console.log('Login Failed');
                    setDialogMessage(`Incorrect Username or Password`);
                    setIsDialogOpen(true);
                    setError('Incorrect Username or Password');
                }

            }, 2000);

        }catch(error)
        {
             console.error("Network Error: ", error);
             setError('A network error occurred, please try again later.');
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
        <Box sx={{ position: 'relative', display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', p: 2, backgroundImage:`url(${backgroundImage})` }}>
            <div style={overlayStyle}></div>
            <Card sx={{position: 'relative'}}>
                <CardContent>
                    <Typography variant="h5" component="div" gutterBottom sx={{fontWeight: 'bold', textAlign: 'left'}}>
                        Sign In
                    </Typography>
                    {loading && (
                        <Box sx={{ display: 'flex', justifyContent: 'center', mb: 2 }}>
                            <CircularProgress color="secondary" sx={{circularProgressStyle}} />
                        </Box>
                    )}
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