import React from 'react';
import {
    Card,
    CardContent,
    Typography,
    TextField,
    Button,
    Checkbox,
    FormControlLabel,
    Select,
    MenuItem,
    InputAdornment,
    IconButton
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import AddIcon from '@mui/icons-material/Add';
import InfoIcon from '@mui/icons-material/Info';

export default function BillPaySchedule() {
    // You will need to manage state and handle changes, this is just a static layout

    return (
        <Card raised>
            <CardContent>
                <Typography variant="h6" gutterBottom>
                    Payment Schedule
                </Typography>

                <IconButton aria-label="info" size="small" sx={{ float: 'right' }}>
                    <InfoIcon fontSize="inherit" />
                </IconButton>

                <Typography variant="body2" gutterBottom>
                    Our goal is to deliver your payment securely and quickly.
                </Typography>
                <Typography variant="body2" color="textSecondary">
                    Some payments will process using a single-use, pre-paid card, which means you will not recognize card numbers within payment confirmation communications you receive.
                </Typography>

                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon />}
                    sx={{ marginTop: 2 }}
                >
                    Payee
                </Button>

                {/* ... Other form fields and buttons ... */}

                <TextField
                    fullWidth
                    margin="normal"
                    id="search-payee"
                    label="Payee name or nickname"
                    InputProps={{
                        endAdornment: (
                            <InputAdornment position="end">
                                <IconButton>
                                    <SearchIcon />
                                </IconButton>
                            </InputAdornment>
                        ),
                    }}
                />

                <FormControlLabel
                    control={<Checkbox name="recurring" />}
                    label="Make it recurring"
                />

                <Select
                    labelId="select-account-label"
                    id="select-account"
                    value="Primary Acco..2242"
                    label="Pay from"
                    fullWidth
                    margin="normal"
                >
                    <MenuItem value="Primary Acco..2242">Primary Acco..2242</MenuItem>
                    {/* ... More menu items ... */}
                </Select>

                {/* ... More components for dates, amounts, buttons ... */}

                <div>
                    <Button variant="outlined" sx={{ marginTop: 2 }}>
                        View pending transactions
                    </Button>
                    <Button variant="outlined" sx={{ marginTop: 2 }}>
                        View history
                    </Button>
                </div>

                <div>
                    <Button variant="contained" color="primary" sx={{ marginTop: 2 }}>
                        Review all
                    </Button>
                    <Button variant="contained" color="secondary" sx={{ marginTop: 2 }}>
                        Pay all
                    </Button>
                </div>

            </CardContent>
        </Card>
    );
}