import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';


export default function LoginForm() {
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
        if (!validateForm()) return;

        try {
            const response = await fetch('http://localhost:8080/api/auth/', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                console.log('Login Successful');
                navigate('/home');
            } else {
                setError('Login failed. Please check your credentials.');
            }
        } catch (error) {
            setError(`Network Error: ${error.message}`);
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleSubmit} className="login-form">
                <h2>Login</h2>
                {error && <div className="error-message">{error}</div>}
                <div className="form-field">
                    <label htmlFor="username">Username: </label>
                    <input
                        type="text"
                        id="username"
                        value={username}
                        onChange={e => setUserName(e.target.value)}
                        required
                    />
                </div>
                <div className="form-field">
                    <label htmlFor="password">Password: </label>
                    <input
                        type="password"
                        id="password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div className="actions">
                    <button type="submit" className="login-button">Login</button>
                    <button type="button" className="register-button" onClick={() => navigate('/register')}>
                        Register
                    </button>
                </div>
                <a href="/forgot-password" className="forgot-password-link">Forgot Password?</a>
            </form>
        </div>
    );
}