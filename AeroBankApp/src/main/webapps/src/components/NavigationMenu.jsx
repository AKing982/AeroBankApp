import {MenuItem, Typography} from "@mui/material";
import Menu from "@mui/material/Menu";
import SubMenu from "./SubMenu";
import {styled} from "@mui/material/styles";
import {useRef, useState} from "react";

function NavigationMenu({anchorEl, isOpen, onClose, handleNavigation, isActive}){
    const [isSubMenuOpen, setSubMenuOpen] = useState(false);
    const subMenuAnchorRef = useRef(null);

    const handleSubMenuNavigate = (path) => {
        onClose();
        handleNavigation(path);
    };

    const handleSubMenuToggle = () => {
        setSubMenuOpen(!isSubMenuOpen);
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


    const createTransactionSubMenuItems = [
        { label: 'Create Transfer', path: '/createTransfer', onNavigate: handleSubMenuNavigate },
        { label: 'Create Withdraw', path: '/createWithdraw', onNavigate: handleSubMenuNavigate },
        { label: 'Create Deposit', path: '/createDeposit', onNavigate: handleSubMenuNavigate },
    ];

    return (
        <Menu
            id="navigation-menu"
            anchorEl={anchorEl}
            anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
            }}
            keepMounted
            transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
            }}
            open={isOpen}
            onClose={onClose}
        >
            <StyledMenuItem onClick={() => handleNavigation("/dashboard")} selected={isActive("/dashboard")}>
                <Typography>Dashboard</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/accounts")} selected={isActive("/accounts")}>
                <Typography>Account Summaries</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/transfers")} selected={isActive("/transfers")}>
                <Typography>Transfers</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/transactionAnalytics")} selected={isActive("/transactionAnalytics")}>
                <Typography>Transaction Analysis</Typography>
            </StyledMenuItem>
            <StyledMenuItem ref={subMenuAnchorRef} onClick={handleSubMenuToggle} selected={isSubMenuOpen}>
                <Typography>Create Transaction</Typography>
            </StyledMenuItem>
            {isSubMenuOpen && (
                <Menu
                    anchorEl={subMenuAnchorRef.current}
                    open={true}
                    onClose={() => setSubMenuOpen(false)}
                    anchorOrigin={{ vertical: 'right', horizontal: 'right' }}
                    transformOrigin={{ vertical: 'top', horizontal: 'left' }}
                >
                    {createTransactionSubMenuItems.map((item, index) => (
                        <MenuItem key={index} onClick={() => item.onNavigate(item.path)}>
                            <Typography>{item.label}</Typography>
                        </MenuItem>
                    ))}
                </Menu>
            )}
            <StyledMenuItem onClick={() => handleNavigation("/billPay")} selected={isActive("/billPay")}>
                <Typography>Bill Pay</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/reports")} selected={isActive("/reports")}>
                <Typography>Reports</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/settings")} selected={isActive("/settings")}>
                <Typography>Settings</Typography>
            </StyledMenuItem>
        </Menu>
    );
}

export default NavigationMenu;