import {FormControl, InputLabel, MenuItem, Select} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";

export default function RoleSelectBox({value, onChange})
{
    return (
        <Box sx={{ minWidth: 90 }}>
            <FormControl fullWidth>
                <InputLabel id="demo-simple-select-label">Role</InputLabel>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={value}
                    label="Age"
                    onChange={onChange}
                >
                    <MenuItem value={10}>User</MenuItem>
                    <MenuItem value={20}>Admin</MenuItem>
                    <MenuItem value={30}>Manager</MenuItem>
                    <MenuItem value={40}>Teller</MenuItem>
                    <MenuItem value={50}>Auditor</MenuItem>
                </Select>
            </FormControl>
        </Box>
    );
}