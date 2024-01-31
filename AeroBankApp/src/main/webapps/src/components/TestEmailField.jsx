import BasicTextField from "./BasicTextField";

export default function TestEmailField({value, onChange})
{
    return(
        <BasicTextField height="55" value={value} onChange={onChange} label="Test Email"/>
    );
}