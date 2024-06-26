import {
    Button,
    Divider,
    Grid,
    IconButton, MenuItem, Select, Table, TableBody,
    InputAdornment,
    TableCell,
    TableContainer,
    TableHead,
    TableRow, TextField,
    Typography, Switch, FormControlLabel, FormGroup, Paper
} from "@mui/material";
import {Box} from "@mui/system";
import RemoveIcon from "@mui/icons-material/Remove";
import AddIcon from "@mui/icons-material/Add";
import {HourglassTop} from "@mui/icons-material";
import {ClockIcon} from "@mui/x-date-pickers";
import {useNavigate} from "react-router-dom";

function TransfersContentPage({viewHistory})
{
    const navigate = useNavigate();
    const handleViewHistory = () => {
        navigate('/billPay/Payments/PaymentHistory');
    }

    return (
            <Box sx={{flexGrow: 1, p: 3 }}>
                <Typography variant="h6" align="left" gutterBottom>
                    Transfers
                </Typography>
                <Grid container spacing={3}>
                    <Grid item xs={12} md={8}>
                        <Paper sx={{ p: 2, mb: 2}}>
                            <Box sx={{display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2}}>
                                <Typography variant="h6">Accounts</Typography>
                                <Divider orientation="horizontal" />
                            </Box>
                            <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 2}}>
                                <Button variant="contained" startIcon={<AddIcon />}>
                                    Add Account
                                </Button>
                            </Box>
                            <TableContainer>
                                <Table>
                                    <TableHead>
                                        <TableRow>
                                            <TableCell>From</TableCell>
                                            <TableCell>To</TableCell>
                                            <TableCell>Amount</TableCell>
                                            <TableCell>Date</TableCell>
                                            <TableCell align="right">Action</TableCell>
                                        </TableRow>
                                    </TableHead>
                                    <TableBody>
                                        <TableRow>
                                            <TableCell>
                                                <Select value="primary" fullWidth>
                                                    <MenuItem value="primary">Select from Account</MenuItem>
                                                </Select>
                                            </TableCell>
                                            <TableCell>
                                                <Select value="primary" fullWidth>
                                                    <MenuItem value="primary">Select to Account</MenuItem>
                                                </Select>
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    defaultValue="$0.00"
                                                    InputProps={{
                                                        startAdornment: <InputAdornment position="start">$</InputAdornment>
                                                    }}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <TextField
                                                    type="date"
                                                    defaultValue="2024-06-25"
                                                    InputProps={{
                                                        endAdornment: (
                                                            <InputAdornment position="end">
                                                            </InputAdornment>
                                                        )
                                                    }}
                                                />
                                            </TableCell>
                                            <TableCell>
                                                <FormGroup>
                                                    <FormControlLabel control={<Switch /> } label="Enable AutoPay">
                                                    </FormControlLabel>
                                                </FormGroup>
                                            </TableCell>
                                        </TableRow>
                                    </TableBody>
                                </Table>
                            </TableContainer>
                            <Box sx={{mb: 2}}>
                            </Box>
                            <Box sx={{display: 'flex', justifyContent: 'space-between', mb: 2}}>
                                <Button variant="contained" startIcon={<AddIcon />}>
                                    Add Transfer Entry
                                </Button>
                            </Box>
                            <Divider />
                            <Box sx={{display: 'flex', justifyContent: 'space-between', mt: 2}}>
                                <Box>
                                </Box>
                            </Box>
                            <Box sx={{display: 'flex', justifyContent: 'space-between', mt: 2}}>
                                <Button variant="outlined" startIcon={<HourglassTop />} sx={{mr: 1}}>
                                    View Pending
                                </Button>
                                <Button variant="contained" color="primary">
                                    Submit
                                </Button>
                                <Button variant="outlined" startIcon={<ClockIcon />} sx={{mr: 1}} onClick={handleViewHistory}>
                                    View history
                                </Button>
                            </Box>
                        </Paper>

                    </Grid>
                </Grid>
            </Box>
    );
}

export default TransfersContentPage;