import {Button, Divider, List, ListItem, ListItemText, Typography} from "@mui/material";
import {Box, Container} from "@mui/system";
import {Fragment, useEffect, useState} from "react";
import React from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";

function ReviewAndSubmitRegistration({formData, handleFormChange}){

    const [isLoading, setIsLoading] = useState(false);

    const navigate = useNavigate();

    const handleSubmit = (e) => {
        console.log('Submitted');

        const registrationRequest = buildRegistrationRequest(formData);
        console.log('Registration Request: ', registrationRequest);

        sendRegistrationToServer(registrationRequest);
    }

    const buildRegistrationRequest = (formData) => {
        return {
            firstName: formData.firstName,
            lastName: formData.lastName,
            username: formData.username,
            email: formData.email,
            PIN: formData.pin,
            password: formData.password,
            isAdmin: formData.isAdmin,
            accounts: formData.accounts,
            securityQuestions: formData.securityQuestions
        }
    }

    const sendRegistrationToServer = async (request) => {
        if(!request){
            return;
        }
        setIsLoading(true);
        try{
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/registration/register`, request);
            if(response.status === 200 || response.status === 201){
                console.log('Registration Saved successfully.');
            }else{
                throw new Error(`Unexpected status response: ${response.status}`);
            }
        }catch(error){
            console.error('There was an error submitting registration: ', error);

            if (error.response) {
                // Server responded with a status code that falls out of the range of 2xx
                console.log(error.response.data);
                console.log(error.response.status);
                console.log(error.response.headers);
            } else if (error.request) {
                // Request was made but no response was received
                console.log(error.request);
            } else {
                // An error occurred in setting up the request
                console.log('Error', error.message);
            }
        }finally{
            setIsLoading(false);
        }
    }

    console.log('Form Accounts: ', formData.accounts);
    console.log('Security Questions: ', formData.securityQuestions);
    return (
        <Container maxWidth="sm">
            <Typography variant="h6" align="center" gutterBottom>
                Review Your Information
            </Typography>
            <List>
                <Typography variant="subtitle1">User Information:</Typography>
                <ListItem>
                    <ListItemText primary="FirstName" secondary={`${formData.firstName}`} />
                </ListItem>
                <ListItem>
                    <ListItemText primary="LastName" secondary={`${formData.lastName}`}/>
                </ListItem>
                <ListItem>
                    <ListItemText primary="Username" secondary={`${formData.username}`}/>
                </ListItem>
                <ListItem>
                    <ListItemText primary="Email" secondary={formData.email} />
                </ListItem>
                <Typography variant="subtitle1" sx={{ mt: 2 }}>
                    Account Review:
                </Typography>
                <Box sx={{ maxHeight: '200px', overflow: 'auto', width: '100%' }}>
                    {formData.accounts && formData.accounts.map((account, index) => (
                        <Fragment key={index}>
                            <ListItem>
                                <ListItemText primary="Account Name" secondary={account.accountName}/>
                            </ListItem>
                            <ListItem>
                                <ListItemText primary="Account Type" secondary={account.accountType} />
                            </ListItem>
                            <ListItem>
                                <ListItemText primary="Initial Balance" secondary={`$${account.initialBalance}`} />
                            </ListItem>
                            {index < formData.accounts.length - 1 && <Divider />}
                        </Fragment>
                    ))}
                </Box>
                <Typography variant="subtitle1">Security Questions:</Typography>
                <Box sx={{ maxHeight: '200px', overflow: 'auto', width: '100%' }}>
                    {formData.securityQuestions && formData.securityQuestions.length > 0 ? (
                        formData.securityQuestions.map((param, index) => (
                            <Fragment key={param.id || index}>
                                <ListItem>
                                    <ListItemText primary="Security Question" secondary={param.question} />
                                </ListItem>
                                <ListItem>
                                    <ListItemText primary="Answer" secondary={param.answer} />
                                </ListItem>
                                {index < formData.securityQuestions.length - 1 && <Divider />}
                            </Fragment>
                        ))
                    ) : (
                        <ListItem>
                            <ListItemText primary="No security questions set." />
                        </ListItem>
                    )}
                </Box>
            </List>
            <Button variant="contained" color="primary" onClick={handleSubmit}>
                Submit All Information
            </Button>
        </Container>
    );
}

export default ReviewAndSubmitRegistration;