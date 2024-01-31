import VerticalTabs from "./VerticalTab";

export default function SettingsView()
{
    return (
        <div className="settings-view-container">
            <header className="settings-view-header">
            </header>
            <div className="settings-tabs">
                <VerticalTabs />
            </div>
        </div>
    );
}