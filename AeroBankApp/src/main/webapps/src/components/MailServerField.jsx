import BasicTextField from "./BasicTextField";

export default function MailServerField({value, onChange})
{
    return (
        <BasicTextField label="Mail Server" height="55" value={value} onChange={onChange}/>
    )
}