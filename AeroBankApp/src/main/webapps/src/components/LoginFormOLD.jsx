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


    const handleSubmit = async (event) => {
        event.preventDefault();
        if(!validateForm())
        {
            return;
        }
        try{
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if(response.ok)
            {
                console.log('Login Successful');
                navigate('/home')
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
                     <button className="button" type="register" onClick={navigateToRegister}>Register</button>
                 </div>
             </form>
         </div>
        );

}