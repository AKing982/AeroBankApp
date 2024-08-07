import BasicTextField from "./BasicTextField";

export default function ServerField({value, onChange})
{
    const label = value ? null : "Server";
    return (
        <div>
            <div className="server-textfield">
                <BasicTextField label={label} height="55" value={value} onChange={onChange}/>
            </div>
        </div>
    )
}