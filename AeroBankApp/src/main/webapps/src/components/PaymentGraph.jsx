import React, {useEffect, useState} from 'react';
import { DataGrid } from '@mui/x-data-grid';
import {Paper} from "@mui/material";


function PaymentGraph({ initialData }) {
    const [data, setData] = useState(initialData);

    useEffect(() => {
        const socket = new WebSocket('ws://localhost:8080/AeroBankApp/ws');

        socket.onopen = () => {
            console.log('WebSocket connection established');
        };

        socket.onmessage = (event) => {
            try {
                const message = JSON.parse(event.data);
                console.log('Received:', message);
                if(message.type === 'update'){
                    setData(message.data);
                }

            } catch (error) {
                console.error('Error parsing message:', error);
            }
        };

        socket.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        socket.onclose = () => {
            console.log('WebSocket connection closed');
        };

        return () => {
            socket.close();
        };
    }, []);


    const columns = [
        { field: 'name', headerName: 'Payee', width: 300 },
        { field: 'lastPaid', headerName: 'Last Paid', width: 180 },
        { field: 'nextPaymentDue', headerName: 'Next Due', width: 150 },
        { field: 'amount', headerName: 'Amount ($)', width: 110, type: 'number' },
    ];

    const rows = initialData.map((item, index) => ({
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