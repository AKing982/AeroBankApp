import BasicTextField from "./BasicTextField";

export default function UserTextField({value, onChange, label})
{
    const boolLabel = value ? null : label;
    return (
        <div>
            <BasicTextField value={value} onChange={onChange} label={boolLabel} height="55"/>
        </div>
    )
}