import AccountSelect from "./AccountSelect";
import {useState} from "react";
import '../TransferView.css';

export default function TransferView()
{
    const [fromAccountCodes, setFromAccountCodes] = useState([]);
    const [toAccountCodes, setToAccountCodes] = useState([]);

    return (
        <div className="transfer-view-container">
            <header className="transfer-view-header">
                {/* Header content, if any */}
            </header>
            <div className="transfer-view-main">
                <form className="transfer-form">
                    <div className="transfer-row">
                        <div className="transfer-from-account">
                            <label htmlFor="fromAccount">From Account:</label>
                            <AccountSelect accounts={fromAccountCodes} />
                        </div>
                        <div className="transfer-to-account">
                            <label htmlFor="toAccount">To Account:</label>
                            <AccountSelect accounts={toAccountCodes} />
                        </div>
                    </div>
                    <div className="transfer-row">
                        <div className="transfer-amount">
                            <label htmlFor="amount">Amount:</label>
                            <input type="number" name="amount" />
                        </div>
                        <div className="transfer-description">
                            <label htmlFor="description">Description:</label>
                            <input type="text" name="description" />
                        </div>
                    </div>
                    <div className="transfer-submit-button">
                        <button type="submit">Transfer</button>
                    </div>
                </form>
            </div>
        </div>
    );
}