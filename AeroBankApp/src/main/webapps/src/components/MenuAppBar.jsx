import * as React from 'react';
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import InputBase from '@mui/material/InputBase';
import Badge from '@mui/material/Badge';
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import SearchIcon from '@mui/icons-material/Search';
import AccountCircle from '@mui/icons-material/AccountCircle';
import MailIcon from '@mui/icons-material/Mail';
import NotificationsIcon from '@mui/icons-material/Notifications';
import {Search} from "@mui/icons-material";
import MoreIcon from '@mui/icons-material/MoreVert';
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import NavigationMenu from "./NavigationMenu";

const StyledInputBase = styled(InputBase)(({ theme }) => ({
    color: 'inherit',
    '& .MuiInputBase-input': {
        padding: theme.spacing(1, 1, 1, 0),
        // vertical padding + font size from searchIcon
        paddingLeft: `calc(1em + ${theme.spacing(4)})`,
        transition: theme.transitions.create('width'),
        width: '100%',
        [theme.breakpoints.up('md')]: {
            width: '20ch',
        },
    },
}));

const SearchIconWrapper = styled(SearchIcon)({
    // Custom styles here
    color: 'blue',
    cursor: 'pointer',
    // other styles as needed
});

export default function MenuAppBar(){
    const [anchorElNav, setAnchorElNav] = useState(null);
    const [anchorElProfile, setAnchorElProfile] = useState(null);
    const isMenuOpen = Boolean(anchorElNav);
    const isProfileMenuOpen = Boolean(anchorElProfile);
    const navigate = useNavigate();

    const handleNavigate = (path) => {
        handleClose();
        navigate(path);
    };

    const StyledMenuItem = styled(MenuItem)(({ theme }) => ({
        background: 'linear-gradient(to bottom, #f9f9f9 0%, #e5e5e5 100%)',  // Light grey gradient
        color: theme.palette.text.primary,  // Use primary text color for better contrast
        '&:hover': {
            background: 'linear-gradient(to bottom, #e5e5e5 0%, #cccccc 100%)',  // Darker gradient on hover
            textDecoration: 'none',
        },
        '& .MuiTypography-root': {
            fontWeight: 'bold',
        }
    }));

    const StyledMenuIcon = styled(MenuIcon)({
        // Add your custom styles here
        color: 'white', // Example: Change color to white
        fontSize: '48px', // Example: Increase font size
    });

    const isActive = (path) => {
        // Implement your logic to determine if the path is active
        // For example, comparing with the current URL
        return window.location.pathname === path;
    };

    const handleMenu = (event) => {
        setAnchorElNav(event.currentTarget);
    };

    const handleProfileMenuOpen = (event) => {
        setAnchorElProfile(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorElNav(null);
        setAnchorElProfile(null);
    };

    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="open drawer"
                        sx={{ mr: 2 }}
                        onClick={handleMenu}
                    >
                        <StyledMenuIcon />
                    </IconButton>
                    <Box sx={{ flexGrow: 1 }} />
                    <IconButton
                        size="large"
                        aria-label="show new mails"
                        color="inherit"
                    >
                        <Badge badgeContent={4} color="error">
                            <MailIcon />
                        </Badge>
                    </IconButton>
                    <IconButton
                        size="large"
                        aria-label="show new notifications"
                        color="inherit"
                    >
                        <Badge badgeContent={17} color="error">
                            <NotificationsIcon />
                        </Badge>
                    </IconButton>
                    <IconButton
                        size="large"
                        edge="end"
                        aria-label="account of current user"
                        aria-haspopup="true"
                        color="inherit"
                        onClick={handleProfileMenuOpen}
                    >
                        <AccountCircle />
                    </IconButton>
                    <NavigationMenu
                        anchorEl={anchorElNav}
                        isOpen={isMenuOpen}
                        onClose={handleClose}
                        handleNavigation={handleNavigate}
                        isActive={isActive} />
                    <Menu
                        id="profile-menu"
                        anchorEl={anchorElProfile}
                        anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        keepMounted
                        transformOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        open={isProfileMenuOpen}
                        onClose={handleClose}
                    >
                        <StyledMenuItem onClick={() => handleNavigate("/profile")}>
                            <Typography>Profile</Typography>
                        </StyledMenuItem>
                        <StyledMenuItem onClick={() => handleNavigate("/myAccount")}>
                            <Typography>My account</Typography>
                        </StyledMenuItem>
                        <StyledMenuItem onClick={() => handleNavigate("/")}>
                            <Typography>Logout</Typography>
                        </StyledMenuItem>
                    </Menu>
                </Toolbar>
            </AppBar>
        </Box>
    );
}