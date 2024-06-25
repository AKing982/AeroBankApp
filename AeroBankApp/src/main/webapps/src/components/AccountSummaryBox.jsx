import MoreVertIcon from "@mui/icons-material/MoreVert";
import {Avatar, IconButton, ListItem, ListItemAvatar, ListItemText, Typography} from "@mui/material";
import {Box} from "@mui/system";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

function AccountSummaryBox({id, name, balance, color, onSelect})
{
    const [isHovered, setIsHovered] = useState(false);

    const getLighterShade = (color, percent) => {
        const num = parseInt(color.replace("#",""), 16),
            amt = Math.round(2.55 * percent),
            R = (num >> 16) + amt,
            G = (num >> 8 & 0x00FF) + amt,
            B = (num & 0x0000FF) + amt;
        return "#" + (0x1000000 + (R<255?R<1?0:R:255)*0x10000 + (G<255?G<1?0:G:255)*0x100 + (B<255?B<1?0:B:255)).toString(16).slice(1);
    };

    return (
        <Box
            onClick={() => onSelect(id)}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            sx={{
                borderRadius: 2,
                overflow: 'hidden',
                height: 120,
                mb: 2,
                background: `linear-gradient(135deg, ${color} 0%, ${getLighterShade(color, 30)} 100%)`,
                transition: 'transform 0.3s, box-shadow 0.3s',
                '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: '0 8px 16px rgba(0,0,0,0.2)',
                },
                position: 'relative',
            }}
           >
            <Box
                sx={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    right: 0,
                    bottom: 0,
                    backgroundImage: 'radial-gradient(circle at 10% 20%, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 80%)',
                }}
            >
        <ListItem
            sx={{
                height: '100%',
                color: 'white'
            }}
        >
            <ListItemAvatar>
                <Avatar sx={{ bgcolor: 'white', color: color }}>
                    {id}
                </Avatar>
            </ListItemAvatar>
            <ListItemText
                primary={
                <Typography variant="h6" fontWeight="bold">
                    {name}
                </Typography>
                }
                secondary={
                    <Typography variant="body1" color="rgba(255,255,255,0.9)">
                        Balance: ${balance}
                    </Typography>
                }
                primaryTypographyProps={{ fontWeight: 'bold', variant: 'body1' }}
                secondaryTypographyProps={{ variant: 'body2' }}
            />
            <IconButton
                edge="end"
                aria-label="more options"
                sx={{color: 'white'}}>
                <MoreVertIcon/>
            </IconButton>
        </ListItem>
        </Box>
        </Box>
    );
}
export default AccountSummaryBox;