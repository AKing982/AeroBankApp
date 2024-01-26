import React, {useState} from "react";
import '../App.css';


export default function Header()
{
    const [welcomeText, setWelcomeText] = useState(null);

    if(!welcomeText)
    {
        setWelcomeText("Welcome to AeroBank");
    }

    return (
        <header id="header">
            <h2 className="welcome-text">{welcomeText}</h2>
        </header>
    );
}