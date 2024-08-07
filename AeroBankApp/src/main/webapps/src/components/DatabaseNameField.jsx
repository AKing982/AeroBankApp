import BasicTextField from "./BasicTextField";

export default function DatabaseNameField({value, onChange})
{
    const label = value ? null : "Database Name";
    return (
        <div>
            <BasicTextField value={value} onChange={onChange} height="55" label={label}/>
        </div>
    );
}