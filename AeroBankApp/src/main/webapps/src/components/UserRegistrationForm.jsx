import {Button, FormControlLabel, Switch, TextField, Typography} from "@mui/material";
import {Container} from "@mui/system";
import {useState} from "react";
import PasswordField from "./PasswordField";
import Password from "./Password";

function UserRegistrationForm({activeStep, handleStepChange, formData, handleFormDataChange, handleSwitchChange}){

    const handleNextButtonClick = (e) => {
        e.preventDefault();
        handleStepChange(activeStep + 1);
    };

    const handleSubmit = () => {

    };

    const handleChange = () => {

    };


    return(
        <Container maxWidth="sm">
            <Typography variant="h6" align="center" gutterBottom>
                User Registration
            </Typography>
            <form onSubmit={handleSubmit}>
                <TextField
                    fullWidth
                    label="First Name"
                    name="firstName"
                    multiline
                    rows={1}
                    value={formData.firstName}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Last Name"
                    name="lastName"
                    multiline
                    rows={1}
                    value={formData.lastName}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Username"
                    name="username"
                    multiline
                    rows={1}
                    value={formData.username}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <TextField
                    fullWidth
                    label="Email"
                    name="email"
                    type="email"
                    multiline
                    rows={1}
                    value={formData.email}
                    onChange={handleFormDataChange}
                    margin="normal"
                    required
                />
                <PasswordField
                    label="Password"
                    name="password"
                    value={formData.password}
                    onChange={handleFormDataChange}
                />
                <PasswordField
                    label="Confirm Password"
                    name="confirmPassword"
                    value={formData.confirmPassword}
                    onChange={handleFormDataChange}
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
                    onClick={handleNextButtonClick}
                >
                    Next
                </Button>
            </form>
        </Container>
    );
}

export default UserRegistrationForm;