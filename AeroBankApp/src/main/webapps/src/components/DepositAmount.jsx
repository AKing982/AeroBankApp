import BasicTextField from "./BasicTextField";

export default function DepositAmount({value, onChange})
{
    return (
        <div>
            <label htmlFor="deposit-amount" className="deposit-amount-label">Deposit Amount: </label>
            <div className="amount-textfield">
                <BasicTextField label="Amount" height="55" value={value} onChange={onChange}/>
            </div>

        </div>
    );
}