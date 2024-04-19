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

<<<<<<< HEAD
=======

>>>>>>> b54900ca1034189b5c5015a6b5f864da56c7dcbc
export default function RegistrationStepper({regForm, handleFormChange, handleSwitchChange, addAccount, deleteAccount})
{
    const [activeStep, setActiveStep] = useState(0); // Change 0 to your desired default step
    const [completedSteps, setCompletedSteps] = useState(new Array(steps.length).fill(false));
    const [errors, setErrors] = useState([]);
    // You can change this function to update the active step as needed
    const handleStepChange = (step) => {
        if (completedSteps[step] || step < activeStep) {  // Allow going back to the first step or any completed step
            setActiveStep(step);
        }
    };

    const handleSubmit = (e) => {
        e.preventDefault();
       const validationErrors = validateData(regForm);
       setErrors(validationErrors);

       if(Object.keys(validationErrors).length === 0){
           let newCompletedSteps = [...completedSteps];
           newCompletedSteps[steps.indexOf('Account Information')] = true;
           setCompletedSteps(newCompletedSteps);
           handleStepChange(activeStep + 1);
       }

    };

    // Example validation function for user information
    const validateUserInfo = (userInfo) => {
        return userInfo.username.length > 0 && userInfo.email.includes("@");
    };

// Example validation function for account information
    const validateAccountInfo = (accountInfo) => {
        return accountInfo.password.length >= 8;
    };

// Example validation function for security questions
    const validateSecurityQuestion = (securityQuestion) => {
        return securityQuestion.answer.length > 0;
    };

    const validateCurrentStep = () => {
        let isValid = false;
        switch (activeStep) {
            case 0: // User Information
                isValid = validateUserInfo(regForm.userInfo);
                break;
            case 1: // Account Information
                isValid = validateAccountInfo(regForm.accountInfo);
                break;
            case 2: // Security Question
                isValid = validateSecurityQuestion(regForm.securityQuestion);
                break;
            case 3: // Review Step
                isValid = true; // Normally review step does not need validation
                break;
            default:
                isValid = false;
        }
        return isValid;
    };


    const handleNext = () => {
        if(validateCurrentStep()){
            let newCompletedSteps = [...completedSteps];
            newCompletedSteps[activeStep] = true;
            setCompletedSteps(newCompletedSteps);
            setActiveStep(prevActiveStep => prevActiveStep + 1);
        }
    }

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

    return (
        <Box sx={{ width: '100%' }}>
            <Stepper activeStep={activeStep} alternativeLabel>
                {steps.map((label, index) => (
                    <Step key={label} onClick={() => handleStepChange(index)} disabled={!completedSteps[index]}>
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