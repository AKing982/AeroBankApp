import BasicTextField from "./BasicTextField";
import NumberField from "./NumberField";

export default function DepositAmount({value, onChange})
{
    return (
        <div>
            <div className="amount-textfield">
                <NumberField label="Amount" height="55" value={value} onChange={onChange}/>
            </div>
        </div>
    );
}