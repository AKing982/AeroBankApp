import BasicTextField from "./BasicTextField";

export default function DepositAmount({deposit, setDeposit})
{
    const handleAmountChange = (event) => {
        setDeposit(event.target.value);
    }

    return (
        <div>
            <label htmlFor="deposit-amount" className="deposit-amount-label">Deposit Amount: </label>
            <div className="amount-textfield">
                <BasicTextField label="Amount" height="55" value={deposit} onChange={handleAmountChange}/>
            </div>

        </div>
    )
}