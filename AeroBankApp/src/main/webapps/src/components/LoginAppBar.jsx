import {Button, Link, Toolbar, Typography} from "@mui/material";
import {Box} from "@mui/system";
import AppBar from "@mui/material/AppBar";
import Logo from './images/KingsCreditUnionMainLogo.jpg';

function LoginAppBar(){
    return (
        <AppBar position="static" color="default" elevation={3} sx={{
            backgroundColor: 'rgba(255, 255, 255, 0.95)',
        }}>
            <Toolbar>
                {/* Left part with logo and menu items */}
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <img src={Logo} alt="UtahKings Credit Union" style={{ height: '100px', marginRight: '2rem' }} />
                    <Typography component="div">
                        <Link to="/accounts" style={{ marginRight: '1rem', textDecoration: 'none', color: 'inherit' }}>ACCOUNTS</Link>
                        <Link to="/loans" style={{ marginRight: '1rem', textDecoration: 'none', color: 'inherit' }}>LOANS & CARDS</Link>
                        {/* ... other links */}
                    </Typography>
                </Box>

                {/* Right part with locations and login */}
                <Box>
                    <Link to="/locations" style={{ textDecoration: 'none', color: 'inherit' }}>LOCATIONS</Link>
                    <Button variant="outlined" sx={{ marginLeft: '1rem' }}>LOG IN</Button>
                </Box>
            </Toolbar>
        </AppBar>
    );
}

export default LoginAppBar;