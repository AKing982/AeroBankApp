import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "./Header";
import '../LoginForm.css';
import AlertDialog from "./CustomAlert";
import '../CustomAlert.css';

export default function LoginFormOLD()
{
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const isLoginButtonEnabled = !username && !password;
    const [dialogMessage, setDialogMessage] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();


    const isValidCsrfToken = (csrfToken) => {
        if(typeof csrfToken === 'string' && csrfToken.trim() !== '' || csrfToken instanceof Promise)
        {
            console.error("CSRF Token is a Promise, not a resolved value");
        }
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

        try{

         //   const csrfToken = await fetchCsrfToken();

            const response = await authenticationResponse();

            // Store the JWT Token in the sessionStorage

                if(response.ok)
                {
                    console.log('Login Successful');

                    const data = await response.json();
                    const token = data.token;
                    saveJWTSession(token);


                    navigateToHomePage();
                }
                else
                {
                    console.log('Login Failed');
                    setDialogMessage(`Incorrect Username or Password`);
                    setIsDialogOpen(true);
                }

        }catch(error)
        {
             console.error("Network Error: ", error);
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

        <div className="background-image">
            <div className="login-box">
                <div className="login-header">
                    <span className="header-title">AeroBank Login</span>
                </div>
                <div className="login-body">
                    <form onSubmit={handleSubmit}>
                        <div className="input-field">
                            <label htmlFor="username" className="input-label">UserName </label>
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
                            <AlertDialog
                                title="Failed"
                                message="Invalid Credentials"
                                isOpen={isDialogOpen}
                                onClose={() => setIsDialogOpen(false)}/>

                        </div>
                    </form>
                </div>
            </div>
        </div>

       /**  <div className="background-image">
             <Header />
             <form onSubmit={handleSubmit}>
                 {error && <div className="error-message"> {error} </div>}
                 <div className="input-field">
                     <label htmlFor="username" className="input-label">Username: </label>
                     <input
                         type="text"
                         id="username"
                         className="input-field"
                         value={username}
                         onChange={e => setUserName(e.target.value)}
                         />
                 </div>
                 <div className="input-field">
                     <label htmlFor="password" className="input-label">Password: </label>
                     <input
                         type="password"
                         id="password"
                         className="input-field"
                         value={password}
                         onChange={e => setPassword(e.target.value)}
                         />
                 </div>
                 <div className="button-container">
                     <button className="button2" type="submit">Login</button>
                     <AlertDialog
                         title="Failed"
                         message="Invalid Credentials"
                         isOpen={isDialogOpen}
                         onClose={() => setIsDialogOpen(false)}/>
                     <button className="button2" type="submit" onClick={navigateToRegister}>Register</button>
                 </div>
             </form>
         </div>
        **/
        );

}