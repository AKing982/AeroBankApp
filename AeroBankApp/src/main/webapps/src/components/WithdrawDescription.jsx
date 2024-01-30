import BasicTextField from "./BasicTextField";

export default function WithdrawDescription({value, onChange})
{
    return (
        <div>
            <label htmlFor="withdraw-description-field" className="withdraw-description-label">Withdraw Description</label>
            <div className="withdraw-description-field">
                <BasicTextField label="Description" value={value} height="55" onChange={onChange}/>
            </div>
        </div>
    );
}
