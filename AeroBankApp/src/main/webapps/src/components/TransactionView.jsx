import AccountBox from "./AccountBox";
import '../AccountBox.css';
import './TransactionView.css';
import TableView from "./TableView";
import ListView from "./AccountListView";
import CollapsiblePanel from "./CollapsiblePanel";
import {Divider} from "@mui/material";


export default function TransactionView()
{
    const data = [
        {id: 1, description: 'Groceries', balance: '1450', debit: '67', credit: null, date: '2024-01-26'},
        {id: 2, description: 'Gas', balance: '1400', debit: null, credit: '50', date: '2024-01-26'}
    ]

    const pending = [

    ]

    return (
        <div className="transaction-view-container">

            <div className="account-list-body">
              <ListView items={<AccountBox accountCode={"A1"} available={4500} balance={5600} pending={15} color="red"/>
              }/>
                <Divider orientation="vertical" variant="fullWidth"/>
            </div>

            <div className="transaction-view-right">
                <CollapsiblePanel title="Pending Transactions" content={<TableView data={data}/>}/>
                <TableView data={data}/>
            </div>
            <div className="transaction-view-footer">
            </div>
        </div>
    );
}