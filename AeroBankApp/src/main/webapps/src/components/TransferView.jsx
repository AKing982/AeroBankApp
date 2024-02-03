import AccountSelect from "./AccountSelect";
import {useState} from "react";

export default function TransferView()
{
    const [fromAccountCodes, setFromAccountCodes] = useState([]);
    const [toAccountCodes, setToAccountCodes] = useState([]);

    return (
        <div className="transfer-view-container">
            <header className="transfer-view-header">
            </header>
            <div className="transfer-view-main">
                <div className="transfer-row">
                    <div className="transfer-from-accountCode">
                    </div>
                </div>
            </div>
        </div>
    );
}