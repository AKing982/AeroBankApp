import BasicTextField from "./BasicTextField";

export function DepositDescription({value, onChange})
{
    return (
        <div>
            <label htmlFor="deposit-description" className="deposit-description-label">Deposit Description: </label>
            <div className="description-textfield">
                <BasicTextField label="Description" value={value} height="55" onChange={onChange}/>
            </div>
        </div>
    );
}