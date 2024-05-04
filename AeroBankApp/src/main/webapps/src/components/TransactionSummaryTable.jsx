import {
    Checkbox,
    FormControlLabel, IconButton, Modal, Paper,
    Switch, Table,
    TableBody,
    TableCell,
    TableContainer, TableHead,
    TablePagination,
    TableRow, TableSortLabel, Toolbar, Tooltip, Typography
} from "@mui/material";
import {Box} from "@mui/system";
import PropTypes from "prop-types";
import {visuallyHidden} from "@mui/utils";
import FilterListIcon from "@mui/icons-material/FilterList";
import DeleteIcon from "@mui/icons-material/Delete";
import {alpha} from "@mui/material/styles";
import {rows} from "./EnhancedTable";
import React, {useEffect, useState} from "react";
import TransactionSummaryFilters from "./TransactionSummaryFilters";
import Dialog from "@mui/material/Dialog";

function descendingComparator(a, b, orderBy) {
    if (b[orderBy] < a[orderBy]) {
        return -1;
    }
    if (b[orderBy] > a[orderBy]) {
        return 1;
    }
    return 0;
}

function getComparator(order, orderBy) {
    return order === 'desc'
        ? (a, b) => descendingComparator(a, b, orderBy)
        : (a, b) => -descendingComparator(a, b, orderBy);
}

// Since 2020 all major browsers ensure sort stability with Array.prototype.sort().
// stableSort() brings sort stability to non-modern browsers (notably IE11). If you
// only support modern browsers you can replace stableSort(exampleArray, exampleComparator)
// with exampleArray.slice().sort(exampleComparator)
function stableSort(array, comparator) {
    const stabilizedThis = array.map((el, index) => [el, index]);
    stabilizedThis.sort((a, b) => {
        const order = comparator(a[0], b[0]);
        if (order !== 0) {
            return order;
        }
        return a[1] - b[1];
    });
    return stabilizedThis.map((el) => el[0]);
}

const headCells = [
    {
        id: 'description',
        numeric: false,
        disablePadding: true,
        label: 'Description',
    },
    {
        id: 'amount',
        numeric: true,
        disablePadding: false,
        label: 'Amount',
    },
    {
        id: 'scheduledDate',
        numeric: true,
        disablePadding: false,
        label: 'Date Scheduled',
    },
    {
        id: 'scheduledTime',
        numeric: true,
        disablePadding: false,
        label: 'Time Scheduled',
    },
    {
        id: 'status',
        numeric: true,
        disablePadding: false,
        label: 'Status',
    },
];

function EnhancedTableHead(props) {
    const { onSelectAllClick, order, orderBy, numSelected, rowCount, onRequestSort } =
        props;
    const createSortHandler = (property) => (event) => {
        onRequestSort(event, property);
    };

    return (
        <TableHead>
            <TableRow>
                <TableCell padding="checkbox">
                    <Checkbox
                        color="primary"
                        indeterminate={numSelected > 0 && numSelected < rowCount}
                        checked={rowCount > 0 && numSelected === rowCount}
                        onChange={onSelectAllClick}
                        inputProps={{
                            'aria-label': 'select all desserts',
                        }}
                    />
                </TableCell>
                {headCells.map((headCell) => (
                    <TableCell
                        key={headCell.id}
                        align={headCell.numeric ? 'right' : 'left'}
                        padding={headCell.disablePadding ? 'none' : 'normal'}
                        sortDirection={orderBy === headCell.id ? order : false}
                    >
                        <TableSortLabel
                            active={orderBy === headCell.id}
                            direction={orderBy === headCell.id ? order : 'asc'}
                            onClick={createSortHandler(headCell.id)}
                        >
                            {headCell.label}
                            {orderBy === headCell.id ? (
                                <Box component="span" sx={visuallyHidden}>
                                    {order === 'desc' ? 'sorted descending' : 'sorted ascending'}
                                </Box>
                            ) : null}
                        </TableSortLabel>
                    </TableCell>
                ))}
            </TableRow>
        </TableHead>
    );
}

EnhancedTableHead.propTypes = {
    numSelected: PropTypes.number.isRequired,
    onRequestSort: PropTypes.func.isRequired,
    onSelectAllClick: PropTypes.func.isRequired,
    order: PropTypes.oneOf(['asc', 'desc']).isRequired,
    orderBy: PropTypes.string.isRequired,
    rowCount: PropTypes.number.isRequired,
};

function EnhancedTableToolbar({numSelected, onFilterClick}) {
    return (
        <Toolbar>
            {numSelected > 0 ? (
                <Typography color="inherit" variant="subtitle1" component="div">
                    {numSelected} selected
                </Typography>
            ) : (
                <Typography variant="h6" id="tableTitle" component="div">
                    Transaction History
                </Typography>
            )}
            <Tooltip title="Filter list">
                <IconButton onClick={onFilterClick}>
                    <FilterListIcon />
                </IconButton>
            </Tooltip>
        </Toolbar>
    );
}

EnhancedTableToolbar.propTypes = {
    numSelected: PropTypes.number.isRequired,
};

export default function TransactionSummaryTable() {

    const [order, setOrder] = useState('asc');
    const [orderBy, setOrderBy] = useState('calories');
    const [selected, setSelected] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [dense, setDense] = useState(false);
    const [filterModalOpen, setFilterModalOpen] = useState(false);
    const [dialogOpen, setDialogOpen] = useState(false);
    const [rows, setRows] = useState([]); // Assume initial empty or fetched from somewhere else
    const [allData, setAllData] = useState([]); // This holds all unfiltered data

    const handleRequestSort = (event, property) => {
        const isAsc = orderBy === property && order === 'asc';
        setOrder(isAsc ? 'desc' : 'asc');
        setOrderBy(property);
    };

    const handleFilterClick = () => {
        setDialogOpen(true);
    };

    // Set rows directly from the filters applied
    const applyFilters = (filteredData) => {
        console.log('Filters: ', filteredData);
        setRows(filteredData);
        handleCloseDialog();
    };

    const handleSelectAllClick = (event) => {
        if (event.target.checked) {
            const newSelected = rows.map((n) => n.description);
            setSelected(newSelected);
            return;
        }
        setSelected([]);
    };

    const handleClick = (event, name) => {
        const selectedIndex = selected.indexOf(name);
        let newSelected = [];

        if (selectedIndex === -1) {
            newSelected = newSelected.concat(selected, name);
        } else if (selectedIndex === 0) {
            newSelected = newSelected.concat(selected.slice(1));
        } else if (selectedIndex === selected.length - 1) {
            newSelected = newSelected.concat(selected.slice(0, -1));
        } else if (selectedIndex > 0) {
            newSelected = newSelected.concat(
                selected.slice(0, selectedIndex),
                selected.slice(selectedIndex + 1),
            );
        }
        setSelected(newSelected);
    };

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    const handleChangeDense = (event) => {
        setDense(event.target.checked);
    };

    const isSelected = (name) => selected.indexOf(name) !== -1;

    const emptyRows = rowsPerPage - Math.min(rowsPerPage, rows.length - page * rowsPerPage);

    const handleCloseDialog = () => {
        setDialogOpen(false);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <Paper sx={{ width: '100%', mb: 2 }}>
                <EnhancedTableToolbar numSelected={selected.length} onFilterClick={handleFilterClick} />
                <TableContainer>
                    <Table
                        sx={{ minWidth: 750 }}
                        aria-labelledby="tableTitle"
                        size={dense ? 'small' : 'medium'}
                    >
                        <EnhancedTableHead
                            numSelected={selected.length}
                            order={order}
                            orderBy={orderBy}
                            onSelectAllClick={handleSelectAllClick}
                            onRequestSort={handleRequestSort}
                            rowCount={rows.length}
                        />
                        <TableBody>
                            {rows.length > 0 ? (
                                rows.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage).map((row, index) => {
                                    const isItemSelected = isSelected(row.description);
                                    const labelId = `enhanced-table-checkbox-${index}`;
                                    return (
                                        <TableRow
                                            hover
                                            onClick={(event) => handleClick(event, row.description)}
                                            role="checkbox"
                                            aria-checked={isItemSelected}
                                            tabIndex={-1}
                                            key={row.description}
                                            selected={isItemSelected}
                                        >
                                            <TableCell padding="checkbox">
                                                <Checkbox
                                                    checked={isItemSelected}
                                                    inputProps={{ 'aria-labelledby': labelId }}
                                                />
                                            </TableCell>
                                            <TableCell component="th" id={labelId} scope="row" padding="none">
                                                {row.description}
                                            </TableCell>
                                            <TableCell align="right">{row.amount}</TableCell>
                                            <TableCell align="right">{row.scheduledDate}</TableCell>
                                            <TableCell align="right">{row.scheduledTime}</TableCell>
                                            <TableCell align="right">{row.status}</TableCell>
                                        </TableRow>
                                    );
                                })
                            ) : (
                                <TableRow style={{ height: 53 * rowsPerPage }}>
                                    <TableCell colSpan={6} align="center">No data available</TableCell>
                                </TableRow>
                            )}
                            {emptyRows > 0 && rows.length > 0 && (
                                <TableRow style={{ height: 53 * emptyRows }}>
                                    <TableCell colSpan={6} />
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
                <TablePagination
                    rowsPerPageOptions={[5, 10, 25]}
                    component="div"
                    count={rows.length}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onPageChange={handleChangePage}
                    onRowsPerPageChange={handleChangeRowsPerPage}
                />
                <FormControlLabel
                    control={<Switch checked={dense} onChange={handleChangeDense} />}
                    label="Dense padding"
                />
            </Paper>
            <Dialog
                open={dialogOpen}
                onClose={handleCloseDialog}
                aria-labelledby="filter-dialog-title"
            >
                <Box sx={{ p: 2 }}>
                    <Typography id="filter-dialog-title" variant="h6" component="h2">
                        Filters
                    </Typography>
                    <TransactionSummaryFilters applyFilters={applyFilters} />
                </Box>
            </Dialog>
        </Box>
    );

}
