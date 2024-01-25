import React, {useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "./Header";

export default function LoginFormOLD()
{
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const validateForm = () => {
        if (!username || !password) {
            setError('Username and password are required');
            return false;
        }
        setError('');
        return true;
    };

    const isValidCsrfToken = (csrfToken) => {
        if(typeof csrfToken === 'string' && csrfToken.trim() !== '' || csrfToken instanceof Promise)
        {
            console.error("CSRF Token is a Promise, not a resolved value");
        }
    }

    const fetchCsrfToken = async () => {
        try
        {
            let csrfToken = sessionStorage.getItem('csrfToken');
            if(!csrfToken && isValidCsrfToken(csrfToken))
            {
                const response = await fetch("http://localhost:8080/csrf/token");
                if(!response.ok)
                {
                    throw new Error(`Server returned status: ${response.status}`);
                }
                csrfToken = await extractCsrFToken(response);
                saveToken(csrfToken);
            }

            return csrfToken;

        }catch(error)
        {
            console.error('Error fetching CSRF Token: ', error)
        }
    }

    const extractCsrFToken = async (response) => {

        const data = await response.json();
        return data.token;
    }

    const saveToken = (token) => {
        sessionStorage.setItem('csrfToken', token);
    }

    const authenticationResponse = async (token) => {
        if(!token)
        {
            console.error("CSRF Token is missing");
            return;
        }

        return await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
                'X-CSRF-TOKEN': token
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        if(!validateForm())
        {
            return;
        }
        try{

            const csrfToken = await fetchCsrfToken();
            if(!csrfToken)
            {
                console.error("CSRF Token is missing");
                return;
            }

            const response = await authenticationResponse(csrfToken);

                if(response.ok)
                {
                    console.log('Login Successful');
                    navigateToHomePage();
                }
                else
                {
                    console.log('Login Failed');
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
             <Header />
             <form>
                 <div>
                     <label htmlFor="username">Username: </label>
                     <input
                         type="text"
                         id="username"
                         value={username}
                         onChange={e => setUserName(e.target.value)}
                         />
                 </div>
                 <div>
                     <label htmlFor="password">Password: </label>
                     <input
                         type="password"
                         id="password"
                         value={password}
                         onChange={e => setPassword(e.target.value)}
                         />
                 </div>
                 <div className="forgot-password-container">
                     <button className="button" onClick={handleForgotPassword}>Forgot Password </button>
                 </div>
                 <div className="button-container">
                     <button className="button" type="submit" onClick={handleSubmit}>Login</button>
                     <button className="button" type="submit" onClick={navigateToRegister}>Register</button>
                 </div>
             </form>
         </div>
        );

}