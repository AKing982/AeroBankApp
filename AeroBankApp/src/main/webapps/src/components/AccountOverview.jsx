import {Box} from "@mui/system";
import {Grid, Link, Paper, Typography} from "@mui/material";
import {useEffect, useState} from "react";


function TimeGreeting({name}) {
    const [greeting, setGreeting] = useState('');

    useEffect(() => {
        const updateGreeting = () => {
            const now = new Date();
            const hours = now.getHours();
            let currentGreeting = '';

            if(hours >= 5 && hours < 12)
            {
                currentGreeting = 'Good Morning';
            }
            else if(hours >= 12 && hours < 17)
            {
                currentGreeting = 'Good Afternoon';
            }
            else if(hours >= 17 || hours < 5)
            {
                currentGreeting = 'Good Evening';
            }

            setGreeting(currentGreeting);
        };

        updateGreeting();

        const timerID = setInterval(updateGreeting, 3600000);

        return function cleanup()
        {
            clearInterval(timerID);
        }
    }, []);


    return (
        <span style={{
            color: '#555', // Slightly darker text for the greeting
            textShadow: '1px 1px 1px #eee' // Subtle shadow for depth
        }}>
            {greeting},
            <span style={{ color: '#000', fontWeight: 'bold' }}>{name}</span>
        </span>
    );

}
function AccountOverview({ firstName, totalBalance, recentTransaction, alerts }){


    return ( <Paper elevation={3} sx={{
           margin: 2,
           padding: 2,
           backgroundColor: 'rgba(255, 255, 255, 0.95)',
           backdropFilter: 'blur(10px)',
           border: '1px solid #ccc',
           borderRadius: '8px',
           boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)'
       }}>
           <Typography variant="h6" component="div" sx={{
               color: '#333',
               fontWeight: 'medium',
               padding: '8px 0',
               borderBottom: '2px solid #ccc',
               marginBottom: '12px',
               textAlign: 'center'
           }}>
               <TimeGreeting name={firstName} />
           </Typography>
           <Box sx={{ padding: '16px' }}>
               <Typography variant="body1" sx={{ fontWeight: 'bold', marginBottom: '8px' }}>
                   Total Balance: {totalBalance}
               </Typography>
               <Typography variant="body2" sx={{ marginBottom: '8px' }}>
                   Recent Activity: {recentTransaction}
               </Typography>
               {alerts && (
                   <Typography variant="body2" sx={{ color: 'red' }}>
                       Alert: {alerts}
                   </Typography>
               )}
               <Grid container spacing={2}>
                   <Grid item xs={6}>
                       <Link href="#" underline="hover">View Statements</Link>
                   </Grid>
                   <Grid item xs={6}>
                       <Link href="#" underline="hover">Transfer Funds</Link>
                   </Grid>
               </Grid>
           </Box>
       </Paper>
   );
}

export default AccountOverview;