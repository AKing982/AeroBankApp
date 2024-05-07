import Menu from "@mui/material/Menu";
import {MenuItem, Typography} from "@mui/material";

export default function SubMenu({anchorEl, open, onClose, items}){
    return (
        <Menu
            anchorEl={anchorEl}
            open={Boolean(open)}
            onClose={onClose}
            anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
            transformOrigin={{ vertical: 'top', horizontal: 'left' }}
        >
            {items.map((item, index) => (
                <MenuItem key={index} onClick={() => item.onNavigate(item.path)}>
                    <Typography>{item.label}</Typography>
                </MenuItem>
            ))}
        </Menu>
    );
}
