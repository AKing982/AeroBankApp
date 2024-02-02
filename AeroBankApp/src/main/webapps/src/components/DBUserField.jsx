import BasicTextField from "./BasicTextField";

export default function DBUserField({value, onChange})
{
    const label = value ? null : "User Name";
    return(
        <div>
            <BasicTextField value={value} onChange={onChange} height="55" label={label}/>
        </div>
    );
}