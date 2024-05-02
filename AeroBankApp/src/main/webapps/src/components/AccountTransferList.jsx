import {
    Button,
    Checkbox, FormControl,
    Grid, InputLabel,
    List,
    ListItem,
    ListItemIcon,
    ListItemText,
    MenuItem,
    Paper,
    Select
} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";

export default function AccountTransferList() {
    const [selectedAvailableUser, setSelectedAvailableUser] = useState('');
    const [selectedUserAccount, setSelectedUserAccount] = useState('');
    const [checked, setChecked] = useState([]);
    const [usernameList, setUsernameList] = useState([]);
    const [allAccounts, setAllAccounts] = useState([]);
    const [userAccounts, setUserAccounts] = useState([]);

    useEffect(() => {
        fetchAvailableUsers();
    }, []);

    const fetchAvailableUsers = async() => {
        try {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/users/user-names-list`);
            if(response.status === 200) {
                setUsernameList(response.data);
            } else {
                console.log('Failed to fetch available users due to response status: ', response.status);
            }
        } catch(error) {
            console.error('Unable to fetch available users:', error);
        }
    };

    useEffect(() => {
        if (selectedAvailableUser) {
            fetchAccountList(selectedAvailableUser, setAllAccounts);
        }
    }, [selectedAvailableUser]);

    useEffect(() => {
        if (selectedUserAccount) {
            fetchAccountList(selectedUserAccount, setUserAccounts);
        }
    }, [selectedUserAccount]);

    const fetchAccountList = async (user, setAccounts) => {
        try {
            const response = await axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/${user}`);
            if(response.status === 200) {
                console.log('Account List: ', response.data);
                setAccounts(response.data);
            }
        } catch(error) {
            console.error(`Unable to fetch Account List for user ${user} due to error: `, error);
        }
    };

    const handleToggle = (value) => () => {
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
        const newAllAccounts = allAccounts.filter(item => !checked.includes(item));
        const newUserAccounts = [...userAccounts, ...checked];
        setUserAccounts(newUserAccounts);
        setAllAccounts(newAllAccounts);
        setChecked([]);
    };

    const handleCheckedLeft = () => {
        const newUserAccounts = userAccounts.filter(item => !checked.includes(item));
        const newAllAccounts = [...allAccounts, ...checked];
        setUserAccounts(newUserAccounts);
        setAllAccounts(newAllAccounts);
        setChecked([]);
    };

    const handleUserChange = (userType) => (event) => {
        const { value } = event.target;
        if (userType === 'available') {
            setSelectedAvailableUser(value);
            setSelectedUserAccount(userAccounts.includes(value) ? '' : selectedUserAccount);
        } else {
            setSelectedUserAccount(value);
            setSelectedAvailableUser(allAccounts.includes(value) ? '' : selectedAvailableUser);
        }
    };

    const customList = (title, items, selectedUser, handleChange) => (
        <div>
            <FormControl fullWidth>
                <InputLabel id={`${title}-user-select-label`}>{`Select User for ${title}`}</InputLabel>
                <Select
                    labelId={`${title}-user-select-label`}
                    value={selectedUser}
                    onChange={handleChange}
                    displayEmpty
                >
                    <MenuItem value="">
                        <em>None</em>
                    </MenuItem>
                    {usernameList.map(user => (
                        <MenuItem key={user.username} value={user.username} disabled={user.username === (title === 'Available Accounts' ? selectedUserAccount : selectedAvailableUser)}>
                            {user.username}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>
            <Paper style={{ width: 200, height: 230, overflow: 'auto', marginTop: 8 }}>
                <List dense component="div" role="list">
                    {items.map((account) => {
                        const labelId = `transfer-list-item-${account.id}-label`;

                        return (
                            <ListItem key={account.id} role="listitem" button onClick={handleToggle(account.id)}>
                                <ListItemIcon>
                                    <Checkbox
                                        checked={checked.indexOf(account.id) !== -1}
                                        tabIndex={-1}
                                        disableRipple
                                        inputProps={{ 'aria-labelledby': labelId }}
                                    />
                                </ListItemIcon>
                                <ListItemText id={labelId} primary={`${account.accountName} - ${account.balance}`} />
                            </ListItem>
                        );
                    })}
                </List>
            </Paper>
        </div>
    );

    return (
        <Grid container spacing={2} direction="column" alignItems="center">
            <Grid item container spacing={2} justifyContent="center">
                <Grid item>
                    {customList('Available Accounts', allAccounts, selectedAvailableUser, handleUserChange('available'))}
                </Grid>
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
                <Grid item>
                    {customList('User Accounts', userAccounts, selectedUserAccount, handleUserChange('user'))}
                </Grid>
            </Grid>
        </Grid>
    );
}