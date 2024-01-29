import BasicTextField from "./BasicTextField";

export function DepositDescription({description, setDescription})
{
    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    return (
        <div>
            <label htmlFor="deposit-description" className="deposit-description-label">Deposit Description: </label>
            <div className="description-textfield">
                <BasicTextField label="Description" value={description} height="55" onChange={handleDescriptionChange}/>
            </div>
        </div>
    )
}