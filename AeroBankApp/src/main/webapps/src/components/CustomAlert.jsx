export default function CustomAlert({title, message, isOpen, onClose})
{
    if(!isOpen)
    {
        return null;
    }

    return (
        <div className="custom-alert-overlay">
            <div className="custom-alert-box">
                <div className="custom-alert-header">
                    <span className="custom-alert-title">{title}</span>
                    <button className="custom-alert-close" onClick={onClose}>&times;</button>
                </div>
                <div className="custom-alert-body">
                    <p>{message}</p>
                </div>
                <div className="custom-alert-footer">
                    <button className="button2" onClick={onClose}>OK</button>
                </div>
            </div>
        </div>
    );
}