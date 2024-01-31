import BasicTextField from "./BasicTextField";

export default function DBUserField({value, onChange})
{
    return(
        <div>
            <BasicTextField value={value} onChange={onChange} height="55" label="User Name"/>
        </div>
    );
}