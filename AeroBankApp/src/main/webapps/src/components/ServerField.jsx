import BasicTextField from "./BasicTextField";

export default function ServerField({value, onChange})
{
    return (
        <div>
            <div className="server-textfield">
                <BasicTextField label="Server" height="55" value={value} onChange={onChange}/>
            </div>
        </div>
    )
}