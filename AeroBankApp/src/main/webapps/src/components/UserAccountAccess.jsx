import {Button, Grid} from "@mui/material";
import './TransferList';
import {useState} from "react";
import TransferList from "./TransferList";
import AccountTransferList from "./AccountTransferList";

export default function UserAccountAccess() {
    const [saved, setSaved] = useState(false);

    const initialLeftItems = ['Account1', 'Account2', 'Account3', 'Account4']; // Accounts available for access
    const initialRightItems = ['Account5', 'Account6']; // Accounts currently accessed by the user

    // State to manage the items in both lists
    const [leftItems, setLeftItems] = useState(initialLeftItems);
    const [rightItems, setRightItems] = useState(initialRightItems);

    const user = sessionStorage.getItem('username');



    const handleSaveChanges = () => {
        // Logic to save changes goes here
        // Example: Send leftItems and rightItems to the backend to update user account access
        console.log('Saving changes:', { leftItems, rightItems });
        setSaved(true); // Update the saved state to indicate that changes have been saved
        // Reset the saved state after a short delay
        setTimeout(() => setSaved(false), 3000);
    };

    return (
        <Grid container spacing={2} justifyContent="center" alignItems="center">
            <Grid item xs={12}>
                <AccountTransferList
                    allAccounts={initialLeftItems.concat(initialRightItems)} // Combine both lists for the component
                    userAccounts={rightItems}
                    setUserAccounts={setRightItems}
                />
            </Grid>
            <Grid item>
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSaveChanges}
                    disabled={saved}
                >
                    Save Changes
                </Button>
                {saved && <p>Changes saved!</p>}
            </Grid>
        </Grid>
    );
}