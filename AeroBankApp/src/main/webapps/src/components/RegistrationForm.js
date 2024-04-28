import {useState} from "react";
import RegisterHeader from "./RegisterHeader";
import RegistrationStepper from "./RegistrationStepper";

import {Typography} from "@mui/material";
import {Box, Container} from "@mui/system";
import backgroundImage from './images/pexels-julius-silver-753325.jpg';

export default function RegistrationForm()
{
    const [registrationForm, setRegistrationForm] = useState({
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        password: '',
        confirmPassword: '',
        pin: '',
        role:'',
        securityQuestions: [],
        isAdmin: false,
        accounts: []
    });

    const handleFormChange = (event) => {
        console.log(event);

        if(!event || !event.target){
            console.error('Event or event target is undefined');
            return;
        }
        const {name, value} = event.target;
        setRegistrationForm({
            ...registrationForm,
            [name]: value,
        });
    };

    const handleAddNewAccount = (newAccount) => {
        setRegistrationForm((prevData) => ({
            ...prevData,
            accounts: [...prevData.accounts, newAccount],
        }));
    };

    const handleDeleteAccount = (indexToDelete) => {
        setRegistrationForm(prevData => ({
            ...prevData,
            accounts: prevData.accounts.filter((_, index) => index !== indexToDelete)
        }));
    };

    const handleSwitchChange = () => {
        setRegistrationForm({
            ...registrationForm,
            isAdmin: !registrationForm.isAdmin,
        });
    };


    return (
        <Container maxWidth={false} style={{ backgroundImage: `url(${backgroundImage})`, backgroundSize: 'cover', padding: 20, display: 'flex', alignItems: 'center', justifyContent: 'center', height: '180vh'}}>
            <Box sx={{ backgroundColor: 'rgba(255, 255, 255, 0.9)', borderRadius: 2, padding: 3 }}>
                <Typography variant="h4" gutterBottom>
                    Registration
                </Typography>
                <RegistrationStepper deleteAccount={handleDeleteAccount} addAccount={handleAddNewAccount} regForm={registrationForm} handleFormChange={handleFormChange} handleSwitchChange={handleSwitchChange}/>
            </Box>
        </Container>
    );
}