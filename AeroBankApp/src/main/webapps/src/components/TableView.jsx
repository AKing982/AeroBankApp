export default function TableView({data})
{
    return (
        <div className="transaction-view-table-body">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Description</th>
                    <th>Balance</th>
                    <th>Debit</th>
                    <th>Credit</th>
                    <th>Date</th>
                </tr>
                </thead>
                <tbody>
                {data.map((item) => (
                    <tr key={item.id}>
                        <td>{item.id}</td>
                        <td>{item.description}</td>
                        <td>{item.balance}</td>
                        <td>{item.debit}</td>
                        <td>{item.credit}</td>
                        <td>{item.date}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

