import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";

export default function RoleSelectBox({value, onChange})
{
    const selectValue = value == null ? '' : value;
    return (
        <Box sx={{ minWidth: 90 }}>
            <FormControl fullWidth>
                <InputLabel id="demo-simple-select-label">Role</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={selectValue}
                    label="Age"
                    onChange={onChange}
                >
                    <MenuItem value="USER">User</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                    <MenuItem value="MANAGER">Manager</MenuItem>
                    <MenuItem value="TELLER">Teller</MenuItem>
                    <MenuItem value="AUDITOR">Auditor</MenuItem>
                </Select>
            </FormControl>
        </Box>
    );
}