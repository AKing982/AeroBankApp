import {
    Button,
    FormControl,
    FormControlLabel, FormLabel, Radio, RadioGroup,
    Step,
    StepLabel,
    Stepper,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {Box, Container} from "@mui/system";
import AccountInformation from "./AccountInformation";
import {useState} from "react";
import ReviewAndSubmitRegistration from "./ReviewAndSubmitRegistration";
import UserRegistrationForm from "./UserRegistrationForm";
import SecurityQuestionForm from "./SecurityQuestionForm";


const steps = [
    'User Information',
    'Account Information',
    'Security Question Form',
    'Submit',
];


export default function RegistrationStepper({regForm, handleFormChange, handleSwitchChange, addAccount, deleteAccount})
{
    const [activeStep, setActiveStep] = useState(0); // Change 0 to your desired default step
    const [errors, setErrors] = useState({});
    const [loginType, setLoginType] = useState('OAuth');

    // You can change this function to update the active step as needed
    const handleStepChange = (step) => {
        setActiveStep(step);
    };


    const securityData = {
        question: "What was the name of your first pet?",
        answer: "Fluffy"
    };

    const handleSubmit = (e) => {
        e.preventDefault();
      //  const validationErrors = validateData(formData);
     //   setErrors(validationErrors);

       // if(Object.keys(validationErrors).length === 0)
       // {
         //   console.log('Form Data: ', formData);
       // }
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

    const handleLoginTypeChange = (event) => {
        setLoginType(event.target.value);
    }


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
                <UserRegistrationForm formData={regForm} handleFormDataChange={handleFormChange} handleSwitchChange={handleSwitchChange} activeStep={activeStep} handleStepChange={(step) => setActiveStep(step)} />
            )}
            {activeStep === steps.indexOf('Account Information') && (
                <form className="account-form-grid" onSubmit={handleSubmit}>
                    <div className="form-row">
                        <AccountInformation handleAddAccount={addAccount} handleDeleteAccount={deleteAccount} accountInfo={regForm} handleAccountInfoChange={handleFormChange} activeStep={activeStep} handleStepChange={(step) => setActiveStep(step)}/>
                    </div>
                </form>
            )}
            {activeStep === steps.indexOf('Security Question Form') && (
                <SecurityQuestionForm securityForm={regForm} handleSecurityFormChange={handleFormChange} activeStep={activeStep} handleStepChange={(step) => setActiveStep(step)} />
            )}
            {activeStep === steps.indexOf('Submit') && (
                <ReviewAndSubmitRegistration formData={regForm} />
            )}
        </Box>
    );
}