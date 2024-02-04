import {Button, FormControlLabel, Step, StepLabel, Stepper, Switch, TextField, Typography} from "@mui/material";
import {Box, Container} from "@mui/system";
import AccountInformation from "./AccountInformation";
import {useState} from "react";

const steps = [
    'User Information',
    'Account Information',
    'Database Connection',
    'Submit',
];

export default function RegistrationStepper()
{
    const [activeStep, setActiveStep] = useState(0); // Change 0 to your desired default step
    const [errors, setErrors] = useState({});

    // You can change this function to update the active step as needed
    const handleStepChange = (step) => {
        setActiveStep(step);
    };

    const [formData, setFormData] = useState({
        firstName: '',
        lastName:'',
        username: '',
        email: '',
        pin:'',
        password: '',
        confirmPassword: '',
        isAdmin: false,
    });


    const handleSubmit = (e) => {
        e.preventDefault();
        const validationErrors = validateData(formData);
        setErrors(validationErrors);

        if(Object.keys(validationErrors).length === 0)
        {
            console.log('Form Data: ', formData);
        }
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

    const handleSwitchChange = () => {
        setFormData({
            ...formData,
            isAdmin: !formData.isAdmin,
        });
    };


    return (
        <Box sx={{ width: '100%' }}>
            <Stepper activeStep={activeStep} alternativeLabel>
                {steps.map((label, index) => (
                    <Step key={label} onClick={() => setActiveStep(index)}>
                        <StepLabel>{label}</StepLabel>
                    </Step>
                ))}
            </Stepper>
            {activeStep === steps.indexOf('User Information') && (
                <Container maxWidth="sm">
                    <Typography variant="h4" align="center" gutterBottom>
                        User Registration
                    </Typography>
                    <form onSubmit={handleSubmit}>
                        <TextField
                            fullWidth
                            label="First Name"
                            name="firstName"
                            value={formData.firstName}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Last Name"
                            name="lastName"
                            value={formData.lastName}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Username"
                            name="username"
                            value={formData.username}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Email"
                            name="email"
                            type="email"
                            value={formData.email}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="PIN"
                            name="pin"
                            type="number"
                            value={formData.pin}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Password"
                            name="password"
                            type="password"
                            value={formData.password}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Confirm Password"
                            name="confirmPassword"
                            type="password"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            margin="normal"
                            required
                        />
                        <FormControlLabel
                            control={
                                <Switch
                                    checked={formData.isAdmin}
                                    onChange={handleSwitchChange}
                                    name="isAdmin"
                                    color="primary"
                                />
                            }
                            label="Administrator"
                        />
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            fullWidth
                            size="large"
                        >
                            Register
                        </Button>
                    </form>
                </Container>
            )}
            {activeStep === steps.indexOf('Account Information') && (
                <form className="account-form-grid" onSubmit={handleSubmit}>
                    <div className="form-row">
                        <AccountInformation />
                    </div>
                </form>
            )}
        </Box>
    );
}