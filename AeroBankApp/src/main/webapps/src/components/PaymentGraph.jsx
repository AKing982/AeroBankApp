import React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import {Paper} from "@mui/material";


function PaymentGraph({ data }) {
    const columns = [
        { field: 'name', headerName: 'Payee', width: 300 },
        { field: 'lastPaid', headerName: 'Last Paid', width: 180 },
        { field: 'nextPaymentDue', headerName: 'Next Due', width: 150 },
        { field: 'amount', headerName: 'Amount ($)', width: 110, type: 'number' },
    ];

    const rows = data.map((item, index) => ({
        id: index,
        name: item.payeeName,
        lastPaid: item.lastPayment,
        nextPaymentDue: item.nextPayment,
        amount: item.paymentAmount
    }));

    return (
        <Paper style={{ height: 350, width: '100%', backgroundColor: 'rgba(255, 255, 255, 0.8)', padding: '1em' }}>
            <DataGrid rows={rows} columns={columns} pageSize={5} />
        </Paper>
    );
}

export default PaymentGraph;