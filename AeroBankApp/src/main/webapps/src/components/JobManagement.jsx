import {Button, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import '../JobManagement.css';
export default function JobManagement()
{
    return (
        <div>
            <Button variant="contained" color="primary">Add Job</Button>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Job Name</TableCell>
                        <TableCell>Group</TableCell>
                        <TableCell>Type</TableCell>
                        <TableCell>Actions</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {/* Map over jobs and create a row for each */}
                </TableBody>
            </Table>
            {/* Dialog for Add/Edit Job */}
            {/* Snackbar for notifications */}
        </div>
    );
}