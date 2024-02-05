import {
    Accordion,
    AccordionDetails, AccordionSummary,
    Card,
    Grid,
    List,
    ListItem,
    ListItemText,
    Paper,
    Table, TableBody,
    TableCell,
    TableHead,
    TableRow,
    Typography
} from "@mui/material";
import axios from "axios";
import {useEffect, useState} from "react";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import {Skeleton} from "@mui/lab";

export default function DashBoard()
{
    const user = sessionStorage.getItem('username');
    const [accounts, setAccounts] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/${user}`)
            .then(response => {
                setAccounts(response.data);
            })
            .catch(error => {
                console.log(`There was an error fetching accounts for:${user} `);
            })
            .finally(() => {
             
            })
    }, [])

    return (
        <Grid container spacing={2}>
            {/* Top Left Quadrant: Account Summaries with Collapsible Panel */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Account Summaries</Typography>
                    {isLoading ? ( // Display Skeleton when loading
                        <Skeleton variant="rectangular" height={100} />
                    ) : (
                        <Accordion>
                            <AccordionSummary
                                expandIcon={<ExpandMoreIcon />}
                                aria-controls="panel-content"
                                id="panel-header"
                            >
                                <Typography>Account 1</Typography>
                            </AccordionSummary>
                            <AccordionDetails>
                                <List>
                                    {/* Details of Account 1 */}
                                </List>
                            </AccordionDetails>
                        </Accordion>
                    )}
                    {/* Repeat for other accounts */}
                </Paper>
            </Grid>

            {/* Top Right Quadrant: Recent Transactions */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Recent Transactions</Typography>
                    {isLoading ? ( // Display Skeleton when loading
                        <Skeleton variant="rectangular" height={300} />
                    ) : (
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Date</TableCell>
                                    <TableCell>Description</TableCell>
                                    <TableCell>Amount</TableCell>
                                    {/* Add more table headers as needed */}
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {/* Map through transactions or add Skeleton rows */}
                            </TableBody>
                        </Table>
                    )}
                </Paper>
            </Grid>

            {/* Bottom Left Quadrant: Scheduled Payments */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Scheduled Payments</Typography>
                    {isLoading ? ( // Display Skeleton when loading
                        <Skeleton variant="rectangular" height={200} />
                    ) : (
                        {/* Content for Scheduled Payments */}
                    )}
                </Paper>
            </Grid>

            {/* Bottom Right Quadrant: Bills Due */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Bills Due</Typography>
                    {isLoading ? ( // Display Skeleton when loading
                        <Skeleton variant="rectangular" height={200} />
                    ) : (
                        {/* Content for Bills Due */}
                    )}
                </Paper>
            </Grid>
        </Grid>
    );
}