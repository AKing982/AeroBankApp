import AccountBox from "./AccountBox";
import '../AccountBox.css';
import './TransactionView.css';
import TitledPane from './TitledPane';

export default function TransactionView({})
{
    return (
        <div className="transaction-view-container">
            <header className="transaction-view-header">
            </header>
            <div className="account-list-body">
                <AccountBox accountCode={"A1"} balance={3600} pending={5000} available={2200} />
                <AccountBox accountCode={"A2"} balance={5400} pending={0} available={0}/>
                <AccountBox accountCode={"A3"} balance={9800} pending={0} available={0}/>
                <AccountBox accountCode={"A4"} balance={1414} pending={2868} available={867}/>
            </div>
            <div className="vertical-line"></div>
            <div>
                <TitledPane title="Pending Transactions" initiallyExpanded={true}/>
            </div>
            <div className="transaction-view-table-body">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Description</th>
                        <th>Balance</th>
                        <th>Date</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>Data 1</td>
                        <td>Data 2</td>
                        <td>Data 3</td>
                        <td>Data 4</td>
                    </tr>
                    <tr>
                        <td>Data 5</td>
                        <td>Data 6</td>
                        <td>Data 7</td>
                        <td>Data 8</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div className="transaction-view-footer">
            </div>
        </div>
    );
}