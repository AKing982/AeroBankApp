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

export default function DashBoard()
{
    const user = sessionStorage.getItem('username');
    const [accounts, setAccounts] = useState([]);

    useEffect(() => {
        axios.get(`http://localhost:8080/AeroBankApp/api/accounts/data/${user}`)
            .then(response => {
                setAccounts(response.data);
            })
            .catch(error => {
                console.log(`There was an error fetching accounts for:${user} `);
            })
    }, [])

    return (
        <Grid container spacing={2}>
            {/* Top Left Quadrant: Account Summaries with Collapsible Panel */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Account Summaries</Typography>
                    {/* Map through accounts to display in Accordion */}
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
                    {/* Repeat for other accounts */}
                </Paper>
            </Grid>

            {/* Top Right Quadrant: Recent Transactions */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Recent Transactions</Typography>
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

                        </TableBody>
                    </Table>
                </Paper>
            </Grid>

            {/* Bottom Left Quadrant: Scheduled Payments */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Scheduled Payments</Typography>
                    {/* Content for Scheduled Payments */}
                </Paper>
            </Grid>

            {/* Bottom Right Quadrant: Bills Due */}
            <Grid item xs={12} md={6}>
                <Paper style={{ padding: 16 }}>
                    <Typography variant="h6">Bills Due</Typography>
                    {/* Content for Bills Due */}
                </Paper>
            </Grid>
        </Grid>
    );
}