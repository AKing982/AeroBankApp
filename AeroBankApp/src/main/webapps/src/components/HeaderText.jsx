export default function HeaderText({text, info})
{
    return (
        <div className="text">
            <h2>{text}</h2>
            <p>{info}</p>
        </div>
    );
}