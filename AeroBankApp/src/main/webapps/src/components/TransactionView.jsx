import AccountBox from "./AccountBox";
import '../AccountBox.css';
import './TransactionView.css';
import TableView from "./TableView";
import ListView from "./AccountListView";
import CollapsiblePanel from "./CollapsiblePanel";
import {Divider, Grid} from "@mui/material";
import {useEffect, useState} from "react";
import axios from "axios";
import TransactionTable from "./TransactionTable";
import PendingTransactionsTable from "./PendingTransactionsTable";
import {Box} from "@mui/system";


export default function TransactionView()
{
    const [accountID, setAccountID] = useState(0);

    return (
        <Box className="transaction-view-container" sx={{ flexGrow: 100}}>
            <Grid container spacing={2}>
                <Grid item xs={12} md={6} className="account-list-body">
                    <ListView updateAccountID={setAccountID} />
                    <Divider orientation="vertical" flexItem />
                </Grid>
                <Grid item xs={12} md={6} className="transaction-view-right">
                    <Box sx={{ maxWidth: 'auto', margin: 'auto', width: '100%' }}>
                        <PendingTransactionsTable />
                        <TransactionTable accountID={accountID} />
                    </Box>
                </Grid>
            </Grid>
            <Box className="transaction-view-footer" sx={{ pt: 2 }}>
                {/* Footer content can go here */}
            </Box>
        </Box>
    );
}