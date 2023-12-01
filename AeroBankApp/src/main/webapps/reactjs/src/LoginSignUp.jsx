import React from 'react'
import './LoginSignUp.css'

import user_icon from '../Assets/person.png';
import email_icon from '../Assets/email.png'
import password_icon from '../Assets/password.png'

const LoginSignUp = () => {
    return (
        <div className='container'>
            <div className="header">
                <div className="text">Sign Up</div>
                <div className="underline"></div>
            </div>
            <div className="inputs">
                <div className="input">
                    <img src={user_icon} alt=""/>
                    <input type="text" placeholder="Name" />
                </div>
                <div className="input">
                    <img src={email_icon} alt=""/>
                    <input type="email"/>
                    <div className="input">
                        <img src={password_icon} alt=""/>
                        <input type="password" />
                    </div>
                </div>
            </div>
            <div className="forgot-password">Lost Password?</div>
            <div className="submit-container">
                <div className="submit">Sign Up</div>
                <div className="submit">Login</div>
            </div>
        </div>
    )
}

export default LoginSignUp;