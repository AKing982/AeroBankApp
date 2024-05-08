import React from 'react';
import { DataGrid } from '@mui/x-data-grid';


function PaymentGraph({ data }) {
    const columns = [
        { field: 'name', headerName: 'Payee', width: 300 },
        { field: 'lastPaid', headerName: 'Last Paid', width: 180 },
        { field: 'nextPaymentDue', headerName: 'Next Due', width: 150 },
        { field: 'amount', headerName: 'Amount ($)', width: 110, type: 'number' },
    ];

    const rows = data.map((item, index) => ({
        id: index,
        name: item.name,
        lastPaid: item.lastPaid,
        nextPaymentDue: item.nextPaymentDue,
        amount: parseFloat(item.amount),
    }));

    return (
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid rows={rows} columns={columns} pageSize={5} />
        </div>
    );
}

export default PaymentGraph;