import {useState} from "react";

export default function HomeTab({ children })
{
    const [activeTab, setActiveTab] = useState(children[0].props.label);

    const handleClick = (e, newActiveTab) => {
        e.preventDefault();
        setActiveTab(newActiveTab);
    }

    return (
        <div>
            <ul className="tabs">
                {children.map((tab) => {
                    const label = tab.props.label;
                    return (
                        <li className={label === activeTab ? 'active' : ''} key={label}>
                            <a href="#" onClick={(e) => handleClick(e, label)}>{label}</a>
                        </li>
                    );
                })}
            </ul>
        <div className="tab-content">
            {children.map((one) => {
                if(one.props.label === activeTab) return <div key={one.props.label}>{one.props.children}</div>
            })}
        </div>
        </div>
    )
}