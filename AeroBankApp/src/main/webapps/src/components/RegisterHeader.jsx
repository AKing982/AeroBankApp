import {useState} from "react";

export default function RegisterHeader()
{
    const [registrationText, setRegistrationText] = useState('');

    if(!registrationText)
    {
        setRegistrationText("AeroBank Registration");
    }

    return (
        <header id="header">
            <h1 className="welcome-text">{registrationText}</h1>
        </header>

    )
}