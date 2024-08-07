import BasicButton from "./BasicButton";

export default function TestEmailButton({value, onChange})
{
    return (
        <BasicButton text="Test" submit={onChange}/>
    );
}