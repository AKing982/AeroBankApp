import BasicTextField from "./BasicTextField";

export default function UserTextField({value, onChange, label})
{
    return (
        <div>
            <BasicTextField value={value} onChange={onChange} label={label} height="55"/>
        </div>
    )
}