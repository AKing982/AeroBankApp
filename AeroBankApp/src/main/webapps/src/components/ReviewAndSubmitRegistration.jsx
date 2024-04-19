import {Button, Divider, List, ListItem, ListItemText, Typography} from "@mui/material";
import {Box, Container} from "@mui/system";
import {Fragment} from "react";
import React from 'react';

function ReviewAndSubmitRegistration({formData}){

    const handleSubmit = (e) => {
        console.log('Submitted');
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
            <Button variant="contained" color="primary" onClick={handleSubmit}>
                Submit All Information
            </Button>
        </Container>
    );
}

export default ReviewAndSubmitRegistration;