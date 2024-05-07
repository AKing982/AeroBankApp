import { LineChart } from '@mui/x-charts/LineChart';

const ChartComponent = ({data}) => {
    // Assuming 'data' is passed as an array of objects with 'name' and 'amount' keys
    const transformDataForChart = () => {
        const xAxisData = data.map(item => parseFloat(item.name)); // Transform 'name' to a float if it represents a numeric value
        const seriesData = data.map(item => item.amount);

        return {
            xAxis: [{ data: xAxisData }],
            series: [{ data: seriesData }],
        };
    };

    const { xAxis, series } = transformDataForChart();

    return (
        <LineChart
            xAxis={xAxis}
            series={series}
            width={400}
            height={300}
        />
    );
}

export default ChartComponent;