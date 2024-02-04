import {
    Button,
    Checkbox,
    Grid,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    MenuItem,
    Paper,
    Select
} from "@mui/material";
import {useState} from "react";

export default function AccountTransferList({ allAccounts = [], userAccounts = [], setUserAccounts }) {
    const [selectedUser, setSelectedUser] = useState('');
    const [checked, setChecked] = useState([]);

    // Filter accounts based on the selected user
    const availableAccounts = allAccounts.filter(account => !userAccounts.includes(account));

    const handleToggle = (value) => () => {
        // Toggle selection
        const currentIndex = checked.indexOf(value);
        const newChecked = [...checked];

        if (currentIndex === -1) {
            newChecked.push(value);
        } else {
            newChecked.splice(currentIndex, 1);
        }

        setChecked(newChecked);
    };

    const handleCheckedRight = () => {
        // Move selected items to userAccounts
        setUserAccounts(userAccounts.concat(checked.filter(item => availableAccounts.includes(item))));
        setChecked(checked.filter(item => !availableAccounts.includes(item)));
    };

    const handleCheckedLeft = () => {
        // Remove selected items from userAccounts
        setUserAccounts(userAccounts.filter(item => !checked.includes(item)));
        setChecked(checked.filter(item => userAccounts.includes(item)));
    };

    const customList = (title, items) => (
        <Paper style={{ width: 200, height: 230, overflow: 'auto' }}>
            <List dense component="div" role="list">
                {items.map((value) => {
                    const labelId = `transfer-list-item-${value}-label`;

                    return (
                        <ListItem key={value} role="listitem" button onClick={handleToggle(value)}>
                            <ListItemIcon>
                                <Checkbox
                                    checked={checked.indexOf(value) !== -1}
                                    tabIndex={-1}
                                    disableRipple
                                    inputProps={{ 'aria-labelledby': labelId }}
                                />
                            </ListItemIcon>
                            <ListItemText id={labelId} primary={value} />
                        </ListItem>
                    );
                })}
            </List>
        </Paper>
    );

    return (
        <div>
            <Select
                value={selectedUser}
                onChange={(e) => setSelectedUser(e.target.value)}
                displayEmpty
                fullWidth
            >
                <MenuItem value="">Select a User</MenuItem>
                {/* Populate with user list */}
                {/* ... other users */}
            </Select>

            <Grid container spacing={2} justifyContent="center" alignItems="center">
                <Grid item>{customList('Available Accounts', availableAccounts)}</Grid>
                <Grid item>
                    <Grid container direction="column" alignItems="center">
                        <Button
                            variant="outlined"
                            size="small"
                            onClick={handleCheckedRight}
                            disabled={checked.length === 0}
                            aria-label="move selected right"
                        >
                            &gt;
                        </Button>
                        <Button
                            variant="outlined"
                            size="small"
                            onClick={handleCheckedLeft}
                            disabled={checked.length === 0}
                            aria-label="move selected left"
                        >
                            &lt;
                        </Button>
                    </Grid>
                </Grid>
                <Grid item>{customList('User Accounts', userAccounts)}</Grid>
            </Grid>
        </div>
    );
}