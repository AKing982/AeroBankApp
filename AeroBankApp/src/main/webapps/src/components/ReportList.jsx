import {Button, Collapse, List, ListItemButton, ListItemText, Paper, TextField} from "@mui/material";
import {ExpandLess, ExpandMore} from "@mui/icons-material";
import React, {useState} from 'react';
import {Box} from "@mui/system";

function ReportList({reports, setReports, onSelect}){
    const [open, setOpen] = useState({});
    const [newFolderName, setNewFolderName] = useState('');

    const handleClick = (id) => {
        setOpen(prevOpen => ({
            ...prevOpen,
            [id]: !prevOpen[id]
        }));
    };

    const handleAddFolder = () => {
        if(!newFolderName.trim()){
            return;
        }
        const newFolder = {
            id: `folder-${new Date().getTime()}`,
            title: newFolderName,
            children: []
        };
        setReports([...reports, newFolder]);
        setNewFolderName('');
    };

    const handleDeleteFolder = (folderId) => {
        // If the folder name is empty, simply return
        const updatedReports = reports.filter(report => report.id !== folderId);
        setReports(updatedReports);
    };

    const renderListItems = (items) => {
        return items.map(item => (
            <React.Fragment key={item.id}>
                <ListItemButton onClick={() => item.children ? handleClick(item.id) : onSelect(item.id)}>
                    <ListItemText primary={item.title}/>
                    {item.children ? (open[item.id] ? <ExpandLess /> : <ExpandMore />) : null}
                </ListItemButton>
                {item.children && (
                    <Collapse in={open[item.id]} timeout="auto" unmountOnExit>
                        <List component="div" disablePadding>
                            {renderListItems(item.children)}
                        </List>
                    </Collapse>
                )}
            </React.Fragment>
        ));
    };

    return (
        <Paper style={{ width: 250, maxHeight: '100vh', overflow: 'auto' }}>
            <Box sx={{ p: 2 }}>
                <TextField
                    fullWidth
                    value={newFolderName}
                    onChange={e => setNewFolderName(e.target.value)}
                    placeholder="New folder name"
                    variant="outlined"
                    size="small"
                />
                <Button onClick={handleAddFolder} variant="contained" color="primary" sx={{ mt: 1 }}>
                    Add Folder
                </Button>
            </Box>
            <List component="nav" aria-labelledby="nested-list-subheader">
                {renderListItems(reports)}
            </List>
        </Paper>
    );
    // return (
    //     <Paper style={{ width: 250, maxHeight: '100vh', overflow: 'auto' }}>
    //         <List component="nav" aria-labelledby="nested-list-subheader">
    //             {reports.map(report => (
    //                 <React.Fragment key={report.id}>
    //                     <ListItemButton onClick={() => handleClick(report.id)}>
    //                         <ListItemText primary={report.title} />
    //                         {report.children ? (open[report.id] ? <ExpandLess /> : <ExpandMore />) : null}
    //                     </ListItemButton>
    //                     {report.children && (
    //                         <Collapse in={open[report.id]} timeout="auto" unmountOnExit>
    //                             <List component="div" disablePadding>
    //                                 {report.children.map(subReport => (
    //                                     <ListItemButton key={subReport.id} sx={{ pl: 4 }} onClick={() => onSelect(subReport.id)}>
    //                                         <ListItemText primary={subReport.title} />
    //                                     </ListItemButton>
    //                                 ))}
    //                             </List>
    //                         </Collapse>
    //                     )}
    //                 </React.Fragment>
    //             ))}
    //         </List>
    //     </Paper>
    // );

}
export default ReportList;