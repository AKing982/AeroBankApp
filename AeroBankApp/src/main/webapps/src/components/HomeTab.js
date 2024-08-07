import {useState} from "react";

export default function HomeTab({ label, onTabClick, isActive })
{

    return (
        <div className={`home-tab ${isActive ? 'active' : ''}`} onClick={onTabClick}>
            {label}
        </div>
    )
}