import {useState} from "react";
import RegisterHeader from "./RegisterHeader";

export default function RegistrationForm()
{
    const [formData, setFormData] = useState({
        firstName: '',
        lastName:'',
        username: '',
        email: '',
        street: '',
        city: '',
        zip: '',
        pin:'',
        password: '',
        confirmPassword: ''
    });

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [username, setUserName] = useState('');
    const [email, setEmail] = useState('');
    const [street, setStreet] = useState('');
    const [city, setCity] = useState('');
    const [pin, setPin] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [errors, setErrors] = useState({});

    const validateData = (data) => {
        let errors = {};

        if(!data.username.trim())
        {
            errors.username = "Username Required";
        }

        if(!data.email)
        {
            errors.email = 'Email required';
        }
        else if(!/\S+@\S+\.\S+/.test(data.email))
        {
            errors.email = 'Email address is invalid';
        }

        if(!data.password)
        {
            errors.password = 'Password Required';
        }
        else if(data.password.length < 6)
        {
            errors.password = 'Password needs to be 6 characters or more';
        }

        if(!data.confirmPassword)
        {
            errors.confirmPassword = 'Confirm password required';
        }
        else if(data.confirmPassword !== data.password)
        {
            errors.confirmPassword = 'Passwords do not match';
        }
        return errors;
    };

    const handleChange = (e) => {
        const {name, value} = e.target;
        if (name === 'pin')
        {
            setFormData({...formData, [name]: value.slice(0, 4)});
        }
        else
        {
            setFormData({...formData, [name]: value});
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        const validationErrors = validateData(formData);
        setErrors(validationErrors);

        if(Object.keys(validationErrors).length === 0)
        {
            console.log('Form Data: ', formData);
        }
    };

    return (
        <div className="registration-container">
            <div className="background-image">
                <RegisterHeader />
                <form className="form-grid">
                    <div className="form-row">
                        <div className="form-field"><label>FirstName: </label><input type="text" name="firstName" value={formData.firstName} onChange={handleChange}/></div>
                        <div className="form-field"><label>LastName: </label><input type="text" name="lastName" value={formData.lastName} onChange={handleChange}/></div>
                        <div className="form-field"><label>Username: </label><input type="text" name="username" value={formData.username} onChange={handleChange}/>{errors.username && <p>{errors.username}</p>}</div>
                        <div className="form-field"><label>Email: </label><input type="text" name="email" value={formData.email} onChange={handleChange}/></div>
                        <div className="form-field"><label htmlFor="street">Street: </label><input type="text" name="street" id="street" inputMode="text" value={formData.street} onChange={handleChange}/></div>
                    </div>

                    <div className="form-row">
                        <div><label>City: </label><input type="text" name="city" value={formData.city} onChange={handleChange}/></div>
                        <div><label htmlFor="zip">ZIP: </label><input type="text" id="zip" name="zip" maxLength="5" inputMode="numeric" value={formData.zip} onChange={handleChange}/></div>
                        <div><label htmlFor="pin">PIN: </label><input
                                type="password"
                                id="pin"
                                name="pin"
                                value={formData.pin}
                                maxLength="4"
                                inputMode="numeric"
                                pattern="\d*"
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="password">Password: </label>
                            <input
                                type="password"
                                name="password"
                                id="password"
                                inputMode="numeric"
                                value={formData.password}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="confirmPassword">Confirm Password: </label>
                            <input
                                type="password"
                                name="confirmPassword"
                                value={formData.confirmPassword}
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                    <button type="submit" className="button" onSubmit={handleSubmit}>Register</button>
                </form>
            </div>
        </div>

    )
}