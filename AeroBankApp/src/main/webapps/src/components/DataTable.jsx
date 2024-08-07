import * as React from 'react';
import {DataGrid} from '@mui/x-data-grid';
import {useEffect, useState} from "react";
import axios from "axios";
import { Resizable } from 'react-resizable';
import CircularProgress from '@mui/material/CircularProgress';


const columns = [
    { field: 'description', headerName: 'Description', width: 600 },
    { field: 'amount', headerName: 'Amount', width: 180 },
    {
        field: 'Credit',
        headerName: 'Credit',
        type: 'number',
        width:  130,
    },
    {
        field: 'Balance',
        headerName: 'Balance',
        description: 'This column has a value getter and is not sortable.',
        sortable: false,
        width: 160,
        valueGetter: (params) =>
            `${params.row.firstName || ''} ${params.row.lastName || ''}`,
    },
];

const ResizableColumn = ({ onResize, width, ...restProps }) => {
    return (
        <Resizable
            width={width}
            height={0}
            onResize={(_, { size }) => {
                onResize(size.width);
            }}
        >
            <div {...restProps} />
        </Resizable>
    );
};

const mockData = [
    {
        id: 1,
        Description: 'Item 1',
        Debit: 100,
        Credit: 50,
    },
    {
        id: 2,
        Description: 'Item 2',
        Debit: 150,
        Credit: 75,
    },
    // Add more mock data as needed
];


export default function DataTable({selectedAccount, accountID})
{
    const [rows, setRows] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const [columnWidths, setColumnWidths] = useState({
        Description: 700,
        Debit: 130,
        Credit: 130,
        Balance: 160,
    });

    const handleColumnResize = (fieldName, newWidth) => {
        // Update the column widths state when a column is resized
        setColumnWidths((prevWidths) => ({
            ...prevWidths,
            [fieldName]: newWidth,
        }));
    };




    const handleColumnWidthChange = (newColumnWidths) => {
        // Update the column widths state when a column width changes
        setColumnWidths(newColumnWidths);
    };



    useEffect(() => {
        if (selectedAccount) {
            console.log("Selected Account: ", selectedAccount);
            setIsLoading(true);

            axios.get(`http://localhost:8080/AeroBankApp/api/deposits/data/${accountID}`)
                .then(response => {
                    const dataWithIds = response.data.map((item, index) => {
                        return {...item, id: index};
                    });

                    setTimeout(() => {
                        setRows(dataWithIds);
                        console.log("Row Data: ", rows);
                        console.log('Fetching Deposit data: ', response.data);
                        setIsLoading(false);
                    }, 2000)

                })
                .catch(error => {
                    console.error('There has been a problem with your fetch operation: ', error);
                    setIsLoading(false);
                })
                .finally(() => {

                });
        }
    }, [selectedAccount]); //

    return (
        <div style={{ height: 400, width: '100%' }}>
            {isLoading ? (
                <CircularProgress style={{margin: 'auto'}}/>

            ) : (
                <DataGrid
                    style={{backgroundColor: 'darkgrey', color:'black'}}
                    rows={rows}
                    getRowId={(row) => row.userID}
                    columns={columns.map((col) => ({
                        ...col,
                        width: columnWidths[col.field],
                        headerRenderer: (props) => (
                            <ResizableColumn
                                {...props}
                                onResize={(newWidth) => handleColumnResize(col.field, newWidth)}
                            />
                        ),
                    }))}

                    autoHeight
                    initialState={{
                        pagination: {
                            paginationModel: { page: 0, pageSize: 5 },
                        },
                    }}
                    pageSizeOptions={[5, 10]}
                    checkboxSelection
                />
            )}
        </div>
    );
}