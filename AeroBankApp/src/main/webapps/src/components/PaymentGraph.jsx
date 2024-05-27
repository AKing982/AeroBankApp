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
        { field: 'amount', headerName: 'Amount ($)', width: 110, type: 'number', cellClassName: 'greenAmount'},
    ];

    const rows = initialData.map((item, index) => ({
        id: index,
        name: item.payeeName,
        lastPaid: item.lastPayment,
        nextPaymentDue: item.nextPayment,
        amount: item.paymentAmount
    }));

    const customStyles = {
        columnHeader: {
            backgroundColor: '#0E0F52',
            color: 'white',
        },
        row: {
            backgroundColor: '#f5f5f5',
            '&:hover': {
                backgroundColor: '#e0e0e0',
            },
        },
        greenAmount: {
            color: 'green !important',
        },
    };


    return (
        <Paper  style={{
            height: 350,
            width: '100%',
            backgroundColor: 'rgba(255, 255, 255, 0.9)',
            padding: '1em',
            borderRadius: '8px',
            boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
            border: '1px solid #e0e0e0',
        }}>
            <DataGrid rows={rows} columns={columns} pageSize={5} sx={{
                '& .MuiDataGrid-columnHeaders': {
                    backgroundColor: customStyles.columnHeader.backgroundColor,
                    color: customStyles.columnHeader.color,
                    fontSize: '1rem',
                    fontWeight: 'bold',
                    borderBottom: '2px solid #d3d3d3',
                },
                '& .MuiDataGrid-row': customStyles.row,
                '& .MuiDataGrid-cell': {
                    borderBottom: '1px solid #e0e0e0',
                },
                '& .greenAmount': {
                    color: customStyles.greenAmount.color,
                },
                '& .MuiDataGrid-cellContent': {
                    fontSize: '0.9rem',
                    color: '#333',
                },
            }}   />
        </Paper>
    );
}

export default PaymentGraph;