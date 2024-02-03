import {DataGrid} from "@mui/x-data-grid";
import * as React from "react";
import {useEffect, useState} from "react";
import axios from "axios";

const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'Description', headerName: 'Description', width: 130 },
    { field: 'Debit', headerName: 'Debit', width: 130 },
    {
        field: 'Credit',
        headerName: 'Credit',
        type: 'number',
        width: 90,
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



export default function WithdrawTable({selectedAccount})
{
    const [rows, setRows] = useState([]);


    return (
        <div style={{ height: 400, width: '100%' }}>
            <DataGrid
                style={{backgroundColor: 'darkgrey', color:'black'}}
                rows={rows}
                columns={columns}
                initialState={{
                    pagination: {
                        paginationModel: { page: 0, pageSize: 5 },
                    },
                }}
                pageSizeOptions={[5, 10]}
                checkboxSelection
            />
        </div>
    );
}