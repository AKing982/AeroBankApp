import {Backdrop, Button, CircularProgress, Divider, List, ListItem, ListItemText, Typography} from "@mui/material";
import {Box, Container} from "@mui/system";
import {Fragment, useEffect, useState} from "react";
import React from 'react';
import {useNavigate} from "react-router-dom";
import axios from "axios";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import Dialog from "@mui/material/Dialog";
import AnimatedEllipsisDialog from "./AnimatedEllipsisDialog";
import DialogActions from "@mui/material/DialogActions";

function ReviewAndSubmitRegistration({formData}){

    const [isLoading, setIsLoading] = useState(false);
    const [showBackdrop, setShowBackDrop] = useState(false);
    const [isDialogOpen, setIsDialogOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');
    const [errorDialogOpen, setErrorDialogOpen] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Submitted');
        setIsLoading(true);
        setShowBackDrop(true);
        const registrationRequest = buildRegistrationRequest(formData);
        console.log('Registration Request: ', registrationRequest);

        const delay = ms => new Promise(res => setTimeout(res, ms));

        try {
            // Wait for 6000 ms before making the request
            await delay(6000);
            const response = await sendRegistrationToServer(registrationRequest);

            if(response){
                console.log('Preparing to navigate...');
                navigate('/');
            }
        } catch (error) {
            console.error("Network Error: ", error);
        } finally {
            setIsLoading(false);
            setShowBackDrop(false);
        }

    }

    const buildRegistrationRequest = (formData) => {
        return {
            firstName: formData.firstName,
            lastName: formData.lastName,
            username: formData.username,
            email: formData.email,
            PIN: formData.pin,
            role: formData.role,
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
        try{
            console.log('Sending request to server...');
            const response = await axios.post(`http://localhost:8080/AeroBankApp/api/registration/register`, request);
            console.log('Server responded with status: ', response.status);
            if(response.status === 200 || response.status === 201){
                console.log('Registration Saved successfully.');
                return true;
            }else{
                throw new Error(`Unexpected status response: ${response.status}`);
            }
        }catch(error){
            console.error('There was an error submitting registration: ', error);

            if (error.response) {
                // Server responded with a status code that falls out of the range of 2xx
                if(error.response.data.message === "Found invalid account segment less than required length."){
                    setErrorMessage(`Server responded with status ${error.response.status}: Unable to proceed with registration due to improper username...`);
                }
                setErrorMessage(`Server responded with status ${error.response.status}: ${error.response.data.message}`);
                console.log(error.response.data);
                console.log(error.response.status);
                console.log(error.response.headers);
            } else if (error.request) {
                // Request was made but no response was received
                setErrorMessage('Request was made but no response was received.');
                console.log(error.request);
            } else {
                // An error occurred in setting up the request
                setErrorMessage(`Error: ${error.message}`);
                console.log('Error', error.message);
            }
            setErrorDialogOpen(true);
            return false;
        }
    };

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
                    <ListItemText primary="Email" secondary={`${formData.email}`} />
                </ListItem>
                <ListItem>
                    <ListItemText primary="PIN" secondary={`${formData.pin}`}/>
                </ListItem>
                <ListItem>
                    <ListItemText primary="Role" secondary={`${formData.role}`}/>
                </ListItem>

                <Typography variant="subtitle1" sx={{ mt: 2 }}>
                    Account Review:
                </Typography>
                <Divider sx={{ height: 1, my: 2 }} />
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
                        formData.securityQuestions.map((question, index) => (
                            <Fragment key={question.id || index}> {/* Use a unique ID if available */}
                                <ListItem>
                                    <ListItemText primary="Security Question" secondary={question.question} />
                                </ListItem>
                                <ListItem>
                                    <ListItemText primary="Answer" secondary={question.answer} />
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
            <Backdrop
                sx={{ color: '#fff', zIndex: (theme) => theme.zIndex.drawer + 1}}
                open={showBackdrop}
            >
                <CircularProgress color="inherit" />
            </Backdrop>
            <AnimatedEllipsisDialog showBackdrop={showBackdrop} setShowBackDrop={setShowBackDrop} />

            {/* Button is always visible and interactive */}
            <Button variant="contained" color="primary" onClick={handleSubmit}>
                Submit All Information
            </Button>

            {/* Error Dialog */}
            <Dialog open={errorDialogOpen} onClose={() => setErrorDialogOpen(false)}>
                <DialogTitle>Error</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {errorMessage}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setErrorDialogOpen(false)} color="primary">
                        Close
                    </Button>
                </DialogActions>
            </Dialog>

        </Container>
    );
}

export default ReviewAndSubmitRegistration;