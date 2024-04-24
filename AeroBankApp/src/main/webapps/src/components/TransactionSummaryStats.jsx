import {Grid, Paper, Typography} from "@mui/material";

function TransactionSummaryStats({title, value}){
    return (
        <Paper elevation={3} sx={{
            padding: 2,
            margin: 1,
            width: '100%',
            background: 'linear-gradient(145deg, #6a11cb, #2575fc)', // Example gradient
            color: '#fff' // White text for better contrast
        }}>
            <Typography variant="h6">{title}</Typography>
            <Typography>{value}</Typography>
        </Paper>
    );
}

export default TransactionSummaryStats;