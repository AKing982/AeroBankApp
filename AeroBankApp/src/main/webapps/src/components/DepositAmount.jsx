export default function DepositAmount({deposit, setDeposit})
{
    const handleAmountChange = (event) => {
        setDeposit(event.target.value);
    }

    return (
        <div>
            <label htmlFor="deposit-amount" className="deposit-amount-label">Deposit Amount: </label>
            <input
                type="text"
                id="amount"
                className="input-field2"
                value={deposit}
                onChange={handleAmountChange}
            />
        </div>
    )
}