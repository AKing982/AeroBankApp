
import { PieChart, Pie, Cell, BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts';
import {Paper, Typography} from "@mui/material";

function TransactionSummaryStats({title, data, type}){
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

    const renderChart = (type, data) => {
        switch (type) {
            case 'pie':
                return (
                    <PieChart>
                        <Pie data={data} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={60} fill="#8884d8">
                            {data.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                        </Pie>
                        <Tooltip />
                    </PieChart>
                );
            case 'bar':
                return (
                    <BarChart data={data}>
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Bar dataKey="value" fill="#8884d8" />
                    </BarChart>
                );
            case 'line':
                return (
                    <LineChart data={data}>
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Line type="monotone" dataKey="value" stroke="#8884d8" />
                    </LineChart>
                );
            default:
                return null;
        }
    };

    return (
        <Paper elevation={3} style={{ padding: '20px', marginBottom: '20px' }}>
            <Typography variant="h6">{title}</Typography>
            <ResponsiveContainer width="100%" height={200}>
                {renderChart(type, data)}
            </ResponsiveContainer>
        </Paper>
    );
}

export default TransactionSummaryStats;