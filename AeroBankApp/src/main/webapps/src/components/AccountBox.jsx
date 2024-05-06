import {useState} from "react";
import {Box} from "@mui/system";
import {Card, CardContent, IconButton, Typography} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";

export default function AccountBox({color, accountCode, accountName, balance, pending, available, fees})
{
    const [expanded, setExpanded] = useState(false);

    const handleClick = () => {
        console.log('Clicked');
    }

    const handleKeyPress = (event) => {
        if (event.key === 'Enter' || event.key === ' ') {
            handleClick();
        }
    }

    const toggleDetails = (event) => {
        // Prevent the card's onClick event from firing when the icon is clicked
        event.stopPropagation();
        setExpanded(!expanded);
    }

    const circleStyle = {
        width: '50px',
        height: '50px',
        borderRadius: '50%',
        backgroundColor: color || '#4CAF50', // Use the color prop if provided, or the default color
        color: 'white',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        marginRight: '15px',
    };

    return (
        <Card
            component="div"
            role="button"
            onClick={handleClick}
            onKeyPress={handleKeyPress}
            tabIndex="0"
            sx={{ display: 'flex', alignItems: 'center', padding: 2, cursor: 'pointer', justifyContent: 'space-between' }}
        >
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <Box sx={circleStyle}>{accountCode}</Box>
                <CardContent>
                    <Typography variant="h6" component="div">{accountName}</Typography>
                    <Typography variant="body1" component="div" sx={{ mb: 1.5 }}>
                        Balance: ${balance}
                    </Typography>
                </CardContent>
            </Box>
            <IconButton
                onClick={toggleDetails}
                aria-expanded={expanded}
                aria-label="show more"
            >
                <ExpandMoreIcon />
            </IconButton>
            {expanded && (
                <CardContent>
                    <Typography variant="body1" component="div" sx={{ mb: 1.5 }}>
                        Pending: ${pending}
                    </Typography>
                    <Typography variant="body1" component="div" sx={{ mb: 1.5 }}>
                        Available: ${available}
                    </Typography>
                    <Typography variant="body1" component="div">
                        Fees: ${fees}
                    </Typography>
                    {/* Include other details as needed */}
                </CardContent>
            )}
        </Card>
    );
}

