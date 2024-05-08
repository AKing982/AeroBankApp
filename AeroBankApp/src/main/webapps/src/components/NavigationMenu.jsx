import {MenuItem, Typography} from "@mui/material";
import Menu from "@mui/material/Menu";
import SubMenu from "./SubMenu";
import {styled} from "@mui/material/styles";
import {useRef, useState} from "react";

function NavigationMenu({anchorEl, isOpen, onClose, handleNavigation, isActive, userRole}){
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
        background: 'linear-gradient(to bottom, #666666 0%, #4d4d4d 100%)', // Medium grey gradient
        color: 'white', //// Set text color to white for high contrast
        padding: '5px 10px',
        '&:first-of-type': {
           // borderTop: 'none', // Explicitly remove border from the first item
        },
        '&:not(:first-of-type)': {
           // borderTop: '1px solid #FFFFFF', // Apply top border to all except the first item
        },
        '&:hover': {
            background: 'linear-gradient(to bottom, #4d4d4d 0%, #333333 100%)', // Darker grey gradient on hover
            textDecoration: 'none',
        },
        '& .MuiTypography-root': {
            fontWeight: 'bold',
            color: 'white', // Ensure text in typography also uses white color
        }
    }));

    const submenuStyles = {
        position: 'absolute',
        left: '100%',
        top: 0,
        zIndex: 1300
    };

    // Styling for the Menu component to remove padding
    const StyledMenu = styled(Menu)({
        '& .MuiPaper-root': {
            paddingTop: 0,  // Removes padding at the top of the Menu
            paddingBottom: 0,  // Removes padding at the bottom of the Menu
            margin: 0,  // Ensure no external margin affects layout
            overflow: 'hidden'  // Prevents unwanted overflow
        }
    });


    const createTransactionSubMenuItems = [
        { label: 'Create Transfer', path: '/transfers', onNavigate: handleSubMenuNavigate },
        { label: 'Create Withdraw', path: '/withdraws', onNavigate: handleSubMenuNavigate },
        { label: 'Create Deposit', path: '/deposits', onNavigate: handleSubMenuNavigate },
    ];

    return (
        <StyledMenu
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
            {/* Common menu items visible to all roles */}
            <StyledMenuItem onClick={() => handleNavigation("/dashboard")} selected={isActive("/dashboard")}>
                <Typography>Dashboard</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/accounts")} selected={isActive("/accounts")}>
                <Typography>Account Summaries</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/transfers")} selected={isActive("/transfers")}>
                <Typography>Transfers</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/transactions")} selected={isActive("/transactions")}>
                <Typography>Transactions</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/transactionAnalytics")} selected={isActive("/transactionAnalytics")}>
                <Typography>Transaction Analysis</Typography>
            </StyledMenuItem>
            <StyledMenuItem onClick={() => handleNavigation("/billPay")} selected={isActive("/billPay")}>
                <Typography>Bill Pay</Typography>
            </StyledMenuItem>
            <StyledMenuItem ref={subMenuAnchorRef} onClick={handleSubMenuToggle} selected={isSubMenuOpen}>
                <Typography>Create Transaction</Typography>
            </StyledMenuItem>
            {isSubMenuOpen && (
                <Menu
                    anchorEl={subMenuAnchorRef.current}
                    open={true}
                    onClose={() => setSubMenuOpen(false)}
                    anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
                    transformOrigin={{ vertical: 'top', horizontal: 'right' }}
                    PaperProps={{ style: submenuStyles }}
                >
                    {createTransactionSubMenuItems.map((item, index) => (
                        <MenuItem key={index} onClick={() => item.onNavigate(item.path)}>
                            <Typography>{item.label}</Typography>
                        </MenuItem>
                    ))}
                </Menu>
            )}


            {/* Reports visible to all except Auditors */}
            {userRole !== 'AUDITOR' && (
                <StyledMenuItem onClick={() => handleNavigation("/reports")} selected={isActive("/reports")}>
                    <Typography>Reports</Typography>
                </StyledMenuItem>
            )}

            {/* Admin and Auditor specific menu items */}
            {userRole === 'ADMIN' || userRole === 'AUDITOR' && (
                <StyledMenuItem onClick={() => handleNavigation("/auditLogs")} selected={isActive("/auditLogs")}>
                    <Typography>Audit Logs</Typography>
                </StyledMenuItem>
            )}

            {/* Settings menu item visible only to Admin */}
            {userRole === 'ADMIN' && (
                <StyledMenuItem onClick={() => handleNavigation("/settings")} selected={isActive("/settings")}>
                    <Typography>Settings</Typography>
                </StyledMenuItem>
            )}

            {/* Help menu item visible to everyone except Auditors */}
            {userRole !== 'AUDITOR' && (
                <StyledMenuItem onClick={() => handleNavigation("/help")} selected={isActive("/help")}>
                    <Typography>Help</Typography>
                </StyledMenuItem>
            )}
        </StyledMenu>
    );
}

export default NavigationMenu;