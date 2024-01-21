import React, {useState} from 'react';
import Header from "./Header";

export default function LoginForm()
{
    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        console.log('Submitted: ', {username, password});
    }

    const handleRegister = (event) => {
        event.preventDefault();
        console.log('Registering User: ', {username, password});
    }

    return (
         <div className="background-image">
             <Header />
             <form onSubmit={handleSubmit}>
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
                 <div className="button-container">
                     <button className="button" type="submit">Login</button>
                     <button className="button" type="register">Register</button>
                 </div>
             </form>
         </div>
        );

}