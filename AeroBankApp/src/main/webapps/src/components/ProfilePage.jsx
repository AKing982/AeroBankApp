import {Avatar, Button, IconButton, styled, TextField, Typography} from "@mui/material";
import {Container} from "@mui/system";
import {useState} from "react";
import {PhotoCamera} from "@mui/icons-material";
import backgroundImage from './images/pexels-james-wheeler-417074.jpg';

function ProfilePage(){
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        phoneNumber: '',
        address: '',
        avatar: null,
    });

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // Here you would typically handle the submission to the backend
        console.log('Profile Updated:', formData);
        alert('Profile updated successfully!');
    };

    const Input = styled('input')({
        display: 'none',
    });

    const handleAvatarChange = (event) => {
        if (event.target.files[0]) {
            setFormData(prevState => ({
                ...prevState,
                avatar: URL.createObjectURL(event.target.files[0])
            }));
        }
    };

    return (
        <div style={{
            backgroundImage: `url(${backgroundImage})`, // Set the background image here
            backgroundSize: 'cover',
            backgroundRepeat: 'no-repeat',
            backgroundAttachment: 'fixed',
            minHeight: '100vh', // Set minimum height to fill the screen vertically
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center'
        }}>
            <Container component="main" maxWidth="sm" sx={{
                padding: 3,
                backgroundColor: 'rgba(255, 255, 255, 0.8)', // Semi-transparent background
                backdropFilter: 'blur(10px)', // CSS blur effect for background
                borderRadius: '8px', // Rounded corners for the form
                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)' // Subtle shadow for depth
            }}>
                <Typography variant="h6" gutterBottom>
                    Update Profile
                </Typography>
                <form onSubmit={handleSubmit}>
                    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', marginBottom: '20px' }}>
                        <Avatar
                            src={formData.avatar}
                            sx={{ width: 56, height: 56 }}
                        />
                        <input
                            accept="image/*"
                            style={{ display: 'none' }}
                            id="icon-button-file"
                            type="file"
                            onChange={handleAvatarChange}
                        />
                        <label htmlFor="icon-button-file">
                            <IconButton color="primary" aria-label="upload picture" component="span">
                                <PhotoCamera />
                            </IconButton>
                        </label>
                    </div>
                    <TextField
                        margin="normal"
                        fullWidth
                        label="First Name"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        label="Last Name"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        label="Email Address"
                        name="email"
                        type="email"
                        value={formData.email}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        label="Phone Number"
                        name="phoneNumber"
                        type="tel"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                    />
                    <TextField
                        margin="normal"
                        fullWidth
                        label="Address"
                        name="address"
                        value={formData.address}
                        onChange={handleChange}
                        multiline
                        rows={4}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        color="primary"
                        sx={{ mt: 3, mb: 2 }}
                    >
                        Save Changes
                    </Button>
                </form>
            </Container>
        </div>

    );
}
export default ProfilePage;