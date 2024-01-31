import BasicTextField from "./BasicTextField";

export default function EmailUserField({value, onChange})
{
    return (
        <BasicTextField label="User Name" value={value} onChange={onChange} height="55"/>
    );
}