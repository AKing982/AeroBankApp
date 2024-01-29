import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "./Header";
import '../LoginForm.css';
import AlertDialog from "./CustomAlert";
import '../CustomAlert.css';
import {Spinner} from "./Spinner";
import LoginAlert from "./LoginAlert";

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


        return await fetch('http://localhost:8080/api/auth/login', {
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

    const handleSubmit = async (event) => {
        event.preventDefault();
        setLoading(true);

        try{

         //   const csrfToken = await fetchCsrfToken();

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

        }catch(error)
        {
             console.error("Network Error: ", error);
             setError('A network error occurred, please try again later.');
        }finally {
            setLoading(false);
        }

    };

    const navigateToHomePage = () => {
        navigate('/home')
    }

    const handleForgotPassword = () => {
        navigate("/forgot-password");
    }

    const navigateToRegister = () => {
        navigate('/register')
    }

    const handleRegister = (event) => {
        event.preventDefault();

    }


    return (
        <div className="login-container">
            {loading === true ? <Spinner/> : ''}

            {error && (
                <LoginAlert/>
            )}
            <div className="background-image">
                <div className="login-box">
                    <div className="login-header">
                        <span className="header-title">Please Login</span>
                    </div>
                    <div className="login-body">
                        <form onSubmit={handleSubmit}>
                            <div className="input-field">
                                <label htmlFor="username" className="input-label">User Name </label>
                                <input
                                    type="text"
                                    id="username"
                                    className="input-field"
                                    value={username}
                                    onChange={e => setUserName(e.target.value)}
                                />
                            </div>
                            <div className="input-field">
                                <label htmlFor="password" className="input-label">Password </label>
                                <input
                                    type="password"
                                    id="password"
                                    className="input-field"
                                    value={password}
                                    onChange={e => setPassword(e.target.value)}
                                />
                            </div>
                            <div className="login-footer">
                                <button className={`button2 ${isLoginButtonEnabled ? 'disabled' : ''}`} disabled={isLoginButtonEnabled}>Login</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        );

}