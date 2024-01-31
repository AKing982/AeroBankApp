import BasicTextField from "./BasicTextField";

export default function DatabaseNameField({value, onChange})
{
    return (
        <div>
            <BasicTextField value={value} onChange={onChange} height="55" label="Database Name"/>
        </div>
    );
}