import {useState} from "react";
import RegisterHeader from "./RegisterHeader";
import RegistrationStepper from "./RegistrationStepper";

export default function RegistrationForm()
{

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [username, setUserName] = useState('');
    const [email, setEmail] = useState('');
    const [street, setStreet] = useState('');
    const [city, setCity] = useState('');
    const [pin, setPin] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    return (
        <div className="registration-container">
            <div className="background-image">
                <RegistrationStepper />
                <form className="form-grid">

                </form>
            </div>
        </div>

    )
}