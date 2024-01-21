import React, {useState} from "react";
import '../App.css';


export default function Header()
{
    const [welcomeText, setWelcomeText] = useState(null);

    if(!welcomeText)
    {
        setWelcomeText("Welcome to \nAeroBank");
    }

    return (
        <header id="header">
            <h1 className="welcome-text">{welcomeText}</h1>
        </header>
    );
}