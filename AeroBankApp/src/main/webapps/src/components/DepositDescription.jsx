export function DepositDescription({description, setDescription})
{
    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    }

    return (
        <div>
            <label htmlFor="deposit-description" className="deposit-description-label">Deposit Description: </label>
            <input
                type="text"
                id="description"
                className="input-field2"
                value={description}
                onChange={handleDescriptionChange}
            />
        </div>
    )
}